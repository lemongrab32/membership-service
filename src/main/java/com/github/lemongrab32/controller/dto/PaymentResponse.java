package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

/**
 * DTO ответа от платёжного сервиса
 * @param status статус проведения платежа
 * @param message уточняющее сообщение
 * @param paymentId идентификатор платежа
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record PaymentResponse(String status, String message, UUID paymentId) {}
