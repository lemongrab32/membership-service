package com.github.lemongrab32.service.impl;

import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
import com.github.lemongrab32.model.Membership;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.MembershipCalculator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultMembershipService implements MembershipService {

	private final MembershipRepository membershipRepository;
	private final MembershipConfigRepository membershipConfigRepository;
	private final TariffService tariffService;
	private final PaymentServiceClient paymentServiceClient = new PaymentServiceClient();
	private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

	@Override
	@Cacheable(value = "CALC_CACHE", key = "#request.category().toString() + " +
		"#request.type().toString() + #request.donation() + #request.months() + " +
		"#request.hours() + #request.tariffId()")
	public CalculationResponse calculateMembership(MembershipRequest request) {
		Tariff tariff = tariffService.getTariffById(request.tariffId());

		if (!tariff.getClientCategory().equals(request.category()) || !tariff.getClientType().equals(request.type())) {
			throw new InvalidClientOptionsException(Messages.INVALID_CLIENT_OPTIONS_MESSAGE);
		}

		var props = getProperties();
		MembershipCalculator calculator = MembershipCalculator.getInstance(request.type(), props);

		return new CalculationResponse(
			Status.SUCCESS, Messages.CALCULATION_SUCCESS_MESSAGE,
			calculator.calculate(request, tariff.getBasePrice())
		);
	}

	@Override
	@Transactional
	public MembershipResponse getMembership(MembershipRequest request) {
		var calcResponse = calculateMembership(request);
		double finalPrice = calcResponse.finalPrice();

		if (request.clientId() != null) {
			paymentServiceClient.createPayment(
				new PaymentRequest(request.clientId(), finalPrice)
			);
		} else {
			throw new InvalidClientOptionsException(Messages.INVALID_CLIENT_OPTIONS_MESSAGE);
		}

		LocalDate startDate = LocalDate.now();
		var membership = Membership.builder()
			.clientId(request.clientId())
			.startDate(startDate)
			.isActive(true)
			.finalPrice(finalPrice)
			.tariffId(request.tariffId())
			.build();
		if (request.hours() == null) {
			membership.setEndDate(startDate.plusMonths(request.months()));
		} else {
			membership.setHoursRemaining(request.hours());
		}

		var saved = membershipRepository.save(membership);

		kafkaTemplate.send(
			"notifications",
			new NotificationRequest(
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

	@Override
	public Map<String, Object> getProperties() {
		return membershipConfigRepository.getProperties();
	}

	@Override
	public PropertyResponse setProperty(PropertyRequest request) {
		return new PropertyResponse(
			Status.SUCCESS,
			Messages.PROPERTY_UPDATE_SUCCESS_MESSAGE,
			membershipConfigRepository.setProperty(request)
		);
	}

}
