package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.constraints.EnumNamePattern;
import com.github.lemongrab32.util.EnumNamePatternValidator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.ToString;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record MembershipRequest(
	UUID clientId,

	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	@EnumNamePattern(
		regexp = EnumNamePatternValidator.CATEGORY_PATTERN,
		message = Messages.VALIDATION_CATEGORY_INCORRECT_MESSAGE
	)
	ClientCategory category,

	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	@EnumNamePattern(
		regexp = EnumNamePatternValidator.TYPE_PATTERN,
		message = Messages.VALIDATION_TYPE_INCORRECT_MESSAGE
	)
	ClientType type,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	Integer tariffId,

	@Max(value = 12, message = "Значение поля должно быть <= 12")
	@Min(value = 1, message = "Значение поля должно быть >= 1")
	Integer months,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	Integer hours,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	Double donation
) {
}
