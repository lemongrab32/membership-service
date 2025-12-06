package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record NotificationRequest(
	Double finalPrice,
	Long membershipId,
	LocalDate startDate,
	LocalDate endDate,
	Integer hoursRemaining
) {}
