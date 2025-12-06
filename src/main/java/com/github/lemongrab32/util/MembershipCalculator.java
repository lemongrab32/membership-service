package com.github.lemongrab32.util;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.type.MembershipConfig;
import com.github.lemongrab32.util.impl.EnterpriseMembershipCalculator;
import com.github.lemongrab32.util.impl.PrivateMembershipCalculator;

import java.util.Map;

public abstract class MembershipCalculator {

	public static MembershipCalculator getInstance(ClientType type, Map<String, Object> props) {
		switch (type) {
			case PRIVATE -> {
				return new PrivateMembershipCalculator(
					(Double) props.getOrDefault(MembershipConfig.PRIVATE_HOUR_MULTIPLIER, 0.47),
					(Double) props.getOrDefault(MembershipConfig.PRIVATE_MID_DISCOUNT, 0.05),
					(Double) props.getOrDefault(MembershipConfig.PRIVATE_MAX_DISCOUNT, 0.1)
				);
			}
			case ENTERPRISE ->  {
				return new EnterpriseMembershipCalculator(
					(Double) props.getOrDefault(MembershipConfig.ENTERPRISE_MID_DISCOUNT, 0.05),
					(Double) props.getOrDefault(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, 0.1),
					(Double) props.getOrDefault(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM, 10000.0),
					(Double) props.getOrDefault(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP, 50000.0)
				);
			}
			default -> {
				return null;
			}
		}
	}

	abstract public double calculate(MembershipRequest request, Double basePrice);

}
