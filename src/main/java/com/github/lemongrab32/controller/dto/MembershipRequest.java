package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record MembershipRequest(
	UUID clientId,

	ClientCategory category,

	ClientType type,

	@Positive
	Integer tariffId,

	@Max(value = 12)
	@Min(value = 1)
	Integer months,

	@Positive
	Integer hours,

	@Positive
	Double donation
) {
}
