package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

/**
 * DTO сообщения для отправки данных в платёжный сервис
 * @param clientId идентификатор клиента
 * @param finalPrice рассчитанная стоимость абонемента
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record PaymentRequest(UUID clientId, Double finalPrice) {}
