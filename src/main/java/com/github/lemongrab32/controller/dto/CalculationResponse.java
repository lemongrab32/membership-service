package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.type.Status;

/**
 * DTO для ответа на запросы расчёта стоимости абонемента
 * @param status успешный или ошибочный статус расчёта
 * @param message уточняющее сообщение
 * @param finalPrice рассчитанная стоимость абонемента
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record CalculationResponse(Status status, String message, Double finalPrice) {
}
