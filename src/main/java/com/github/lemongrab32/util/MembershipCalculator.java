package com.github.lemongrab32.util;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.util.impl.EnterpriseMembershipCalculator;
import com.github.lemongrab32.util.impl.PrivateMembershipCalculator;

public abstract class MembershipCalculator {

	public static MembershipCalculator getInstance(ClientType type) {
		switch (type) {
			case PRIVATE -> {
				return new PrivateMembershipCalculator();
			}
			case ENTERPRISE ->  {
				return new EnterpriseMembershipCalculator();
			}
			default -> {
				return new PrivateMembershipCalculator();
			}
		}
	}

	abstract public double calculate(MembershipRequest request, Double basePrice);

}
