package com.github.lemongrab32.util.impl;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
import com.github.lemongrab32.util.MembershipCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PrivateMembershipCalculator extends MembershipCalculator {

	@Value("${membership.private.hour-multiplier: 0.47}")
	private double hourMultiplier;
	@Value("${membership.private.mid-multiplier: 0.05}")
	private double midMultiplier;
	@Value("${membership.private.max-multiplier: 0.1}")
	private double maxMultiplier;

	@Override
	public double calculate(MembershipRequest request, Double basePrice) {
		if (request.hours() > 0) {
			return calcHourly(request, basePrice);
		}

		return calcMonthly(request, basePrice);
	}

	private double calcHourly(MembershipRequest request, Double basePrice) {
		return request.hours() * (basePrice + (basePrice * hourMultiplier) / 84);
	}

	private double calcMonthly(MembershipRequest request, Double basePrice) {
		int months = request.months();

		if (months == 1) {
			return basePrice;
		} else if (months >= 3 && months <= 6) {
			return basePrice - (basePrice * midMultiplier);
		} else {
			return basePrice - (basePrice * maxMultiplier);
		}
	}

}
