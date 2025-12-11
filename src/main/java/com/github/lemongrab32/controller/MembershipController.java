package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@PostMapping
	public ResponseEntity<MembershipResponse> create(@RequestBody @Validated MembershipRequest request) {
		return new ResponseEntity<>(membershipService.getMembership(request), HttpStatus.CREATED);
	}

	@PostMapping("/calculate")
	public ResponseEntity<CalculationResponse> calculate(@RequestBody @Validated MembershipRequest request) {
		return ResponseEntity.ok(membershipService.calculateMembership(request));
	}

	@GetMapping("/config")
	public ResponseEntity<Map<String, Object>> getProperties() {
		return ResponseEntity.ok(membershipService.getProperties());
	}

	@PutMapping("/config")
	public ResponseEntity<PropertyResponse> setProperty(@RequestBody @Validated PropertyRequest request) {
		return ResponseEntity.ok(membershipService.setProperty(request));
	}

}
