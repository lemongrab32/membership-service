package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.type.Status;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record PaymentResponse(Status status, String message, UUID paymentId) {}
