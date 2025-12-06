package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.MembershipResponse;
import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@PostMapping
	public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest request) {
		return new ResponseEntity<>(membershipService.get(request), HttpStatus.CREATED);
	}

	@PostMapping("/calculate")
	public ResponseEntity<CalculationResponse> calculate(@RequestBody MembershipRequest request) {
		return ResponseEntity.ok(membershipService.calculate(request));
	}

	@GetMapping("/config")
	public ResponseEntity<Map<String, Object>> getProperties() {
		return ResponseEntity.ok(membershipService.getProperties());
	}

	@PutMapping("/config")
	public ResponseEntity<String> setProperty(@RequestBody PropertyRequest request) {
		return ResponseEntity.ok(membershipService.setProperty(request));
	}

}
