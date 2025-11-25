package com.github.lemongrab32.util.impl;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.util.MembershipCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseMembershipCalculator extends MembershipCalculator {

	@Value("${membership.enterprise.max-multiplier: 0.05}")
	private double midMultiplier;
	@Value("${membership.enterprise.max-multiplier: 0.1}")
	private double maxMultiplier;
	@Value("${membership.enterprise.top: 50000}")
	private int top;
	@Value("${membership.enterprise.bottom: 10000}")
	private int bottom;

	@Override
	public double calculate(MembershipRequest request, Double basePrice) {
		double donation = request.donation();

		if (donation >= top) {
			return request.months() * (basePrice - basePrice * maxMultiplier);
		} else if (donation >= bottom &&  donation < top) {
			return request.months() * (basePrice - basePrice * midMultiplier);
		} else {
			return request.months() * basePrice;
		}
	}

}
