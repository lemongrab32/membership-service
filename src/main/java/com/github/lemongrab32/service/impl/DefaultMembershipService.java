package com.github.lemongrab32.service.impl;

import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.PropertyService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.MembershipCalculator;
import com.github.lemongrab32.util.mapper.MembershipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Стандартная реализация сервиса {@link MembershipService}
 */
@Service
@RequiredArgsConstructor
public class DefaultMembershipService implements MembershipService {

	private final MembershipRepository membershipRepository;
	private final MembershipConfigRepository membershipConfigRepository;
	private final TariffService tariffService;
	private final PropertyService propertyService;
	private final PaymentServiceClient paymentServiceClient = new PaymentServiceClient();
	private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;
	private final MembershipMapper mapper;

	@Override
	@Cacheable(value = "CALC_CACHE", key = "#request.clientCategory().toString() + " +
		"#request.clientType().toString() + #request.donation() + #request.months() + " +
		"#request.hours() + #request.tariffId()")
	public CalculationResponse calculateMembership(MembershipRequest request) {
		Tariff tariff = tariffService.getTariffById(request.tariffId());

		if (!tariff.getClientCategory().equals(ClientCategory.valueOf(request.clientCategory())) ||                          // категория или тип клиента не соответствуют данным тарифа
			!tariff.getClientType().equals(ClientType.valueOf(request.clientType()))) {
			throw new InvalidClientOptionsException(Messages.INVALID_CLIENT_OPTIONS_MESSAGE);
		}

		var props = propertyService.getProperties();                                                                         // получение параметров для расчёта
		MembershipCalculator calculator = MembershipCalculator.getInstance(ClientType.valueOf(request.clientType()), props); // вызов фабричного метода для получения калькулятора на основании типа клиента

		return new CalculationResponse(
			Status.SUCCESS, Messages.CALCULATION_SUCCESS_MESSAGE,
			calculator.calculate(request, tariff.getBasePrice())
		);
	}

	@Override
	@Transactional
	public MembershipResponse getMembership(MembershipRequest request) {
		var calcResponse = calculateMembership(request);                                                                     // расчёт стоимости абонемента
		double finalPrice = calcResponse.finalPrice();

		if (request.clientId() != null) {
			paymentServiceClient.createPayment(
				new PaymentRequest(request.clientId(), finalPrice)                                                           // отправка запроса в платёжный сервис
			);
		} else {
			throw new InvalidClientOptionsException(Messages.INVALID_CLIENT_OPTIONS_MESSAGE);
		}

		var membership = mapper.toMembership(request);

		LocalDate startDate = LocalDate.now();
		membership.setStartDate(startDate);
		if (request.hours() == null) {
			membership.setEndDate(startDate.plusMonths(request.months()));
		} else {
			membership.setHoursRemaining(request.hours());
		}

		membership.setActive(true);
		membership.setFinalPrice(finalPrice);

		var saved = membershipRepository.save(membership);

		kafkaTemplate.send(                                                                                                  // отправка данных об оформленном
			"notifications",                                                                                                 // абонементе в очередь сообщений для последующего
			new NotificationRequest(                                                                                         // формирования уведомления клиенту
				saved.getFinalPrice(),
				saved.getId(),
				saved.getStartDate(),
				saved.getEndDate(),
				saved.getHoursRemaining()
			)
		);

		return new MembershipResponse(
			Status.SUCCESS,
			Messages.MEMBERSHIP_SUCCESS_MESSAGE,
			request.tariffId()
		);
	}

}
