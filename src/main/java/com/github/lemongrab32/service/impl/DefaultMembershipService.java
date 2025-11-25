package com.github.lemongrab32.service.impl;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.MembershipResponse;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.MembershipCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMembershipService implements MembershipService {

	private final MembershipRepository membershipRepository;
	private final TariffService tariffService;

	@Override
	@Cacheable(value = "CALC_CACHE", key = "#request.category() + #request.type() + #request.donation() + #request.months() + #request.hours() + #request.tariffId()")
	public CalculationResponse calculate(MembershipRequest request) {
		Tariff tariff = tariffService.getTariffById(request.tariffId());

		if (!tariff.getClientCategory().equals(request.category()) || !tariff.getClientType().equals(request.type())) {
			throw new InvalidClientOptionsException("Invalid client request");
		}

		MembershipCalculator calculator = MembershipCalculator.getInstance(request.type());

		return new CalculationResponse(
			Status.SUCCESS, Messages.CALCULATION_SUCCESS_MESSAGE,
			calculator.calculate(request, tariff.getBasePrice())
		);
	}

	@Override
	public MembershipResponse get(MembershipRequest request) {
		return null;
	}

}
