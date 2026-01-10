package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class PropertyController {

	private final PropertyService propertyService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getProperties() {
		return ResponseEntity.ok(propertyService.getProperties());
	}

	@PutMapping
	public ResponseEntity<PropertyResponse> setProperty(@RequestBody @Validated PropertyRequest request) {
		return ResponseEntity.ok(propertyService.setProperty(request));
	}

}
