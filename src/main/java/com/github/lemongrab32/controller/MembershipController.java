package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.MembershipResponse;
import com.github.lemongrab32.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@PostMapping
	public ResponseEntity<MembershipResponse> create(MembershipRequest request) {
		return new ResponseEntity<>(membershipService.get(request), HttpStatus.CREATED);
	}

	@GetMapping("/calculate")
	public ResponseEntity<CalculationResponse> calculate(MembershipRequest request) {
		return ResponseEntity.ok(membershipService.calculate(request));
	}

}
