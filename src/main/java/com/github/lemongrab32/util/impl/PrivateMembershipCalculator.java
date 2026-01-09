package com.github.lemongrab32.util.impl;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.util.MembershipCalculator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrivateMembershipCalculator extends MembershipCalculator {

	private final Double hourMultiplier;
	private final Double midMultiplier;
	private final Double maxMultiplier;

	@Override
	public double calculate(MembershipRequest request, Double basePrice) {
		if (request.hours() != null && request.hours() > 0) {
			return calcHourly(request, basePrice);
		}

		return calcMonthly(request, basePrice);
	}

	private double calcHourly(MembershipRequest request, Double basePrice) {
		return request.hours() * (basePrice + (basePrice * hourMultiplier) / 84);
	}

	private double calcMonthly(MembershipRequest request, Double basePrice) {
		int months = request.months();

		if (months >= 1 && months < 3) {
			return months * basePrice;
		} else if (months >= 3 && months <= 6) {
			return months * (basePrice - basePrice * midMultiplier);
		} else {
			return months * (basePrice - basePrice * maxMultiplier);
		}
	}

}
