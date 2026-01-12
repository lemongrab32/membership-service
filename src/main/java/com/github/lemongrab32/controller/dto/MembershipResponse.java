package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.type.Status;

/**
 * DTO для ответа на запросы оформления абонемента
 * @param status успешный или ошибочный статус оформления
 * @param message уточняющее сообщение
 * @param tariffId идентификатор тарифа, по которому оформлялся абонемент
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MembershipResponse(Status status, String message, Integer tariffId) {
}
