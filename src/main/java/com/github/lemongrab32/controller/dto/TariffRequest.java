package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record TariffRequest(
	@Size(min = 2, max = 100)
	@NotNull
	String name,

	@Positive
	@NotNull
	Double basePrice,

	@NotNull
	ClientCategory category,

	@NotNull
	ClientType type
) {}
