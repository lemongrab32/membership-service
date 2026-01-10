package com.github.lemongrab32.util.impl;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.util.MembershipCalculator;
import lombok.AllArgsConstructor;

/**
 * Калькулятор, рассчитывающий стоимость абонементов корпоративных клиентов
 */
@AllArgsConstructor
public class EnterpriseMembershipCalculator extends MembershipCalculator {

	private final Double midMultiplier;
	private final Double maxMultiplier;
	private final Double top;
	private final Double bottom;

	@Override
	public double calculate(MembershipRequest request, Double basePrice) {
		double donation = request.donation();

		if (donation >= top) {
			return request.months() * (basePrice - basePrice * maxMultiplier);
		} else if (donation >= bottom) {
			return request.months() * (basePrice - basePrice * midMultiplier);
		} else {
			return request.months() * basePrice;
		}
	}

}
