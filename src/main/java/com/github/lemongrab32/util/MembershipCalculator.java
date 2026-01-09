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
					(Double) props.get(MembershipConfig.PRIVATE_HOUR_MULTIPLIER),
					(Double) props.get(MembershipConfig.PRIVATE_MID_DISCOUNT),
					(Double) props.get(MembershipConfig.PRIVATE_MAX_DISCOUNT)
				);
			}
			case ENTERPRISE ->  {
				return new EnterpriseMembershipCalculator(
					(Double) props.get(MembershipConfig.ENTERPRISE_MID_DISCOUNT),
					(Double) props.get(MembershipConfig.ENTERPRISE_MAX_DISCOUNT),
					(Double) props.get(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM),
					(Double) props.get(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP)
				);
			}
			default -> {
				return null;
			}
		}
	}

	abstract public double calculate(MembershipRequest request, Double basePrice);

}
