package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.type.Status;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record CalculationResponse(Status status, String message, Double finalPrice) {
}
