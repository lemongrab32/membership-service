package com.github.lemongrab32.controller.dto;

import com.github.lemongrab32.type.Status;

public record PropertyResponse(
	Status status, String message,
	String name
) {
}
