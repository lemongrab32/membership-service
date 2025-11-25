package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.MembershipResponse;

public interface MembershipService {

	CalculationResponse calculate(MembershipRequest request);
	MembershipResponse get(MembershipRequest request);

}
