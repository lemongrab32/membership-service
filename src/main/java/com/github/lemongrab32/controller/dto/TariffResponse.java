package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.lemongrab32.type.Status;

/**
 * DTO для ответа на запросы управления тарифами
 * @param status успешный или ошибочный статус операции
 * @param message уточняющее сообщение
 * @param tariffId идентификатор тарифа
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TariffResponse(Status status, String message, Integer tariffId) {
}
