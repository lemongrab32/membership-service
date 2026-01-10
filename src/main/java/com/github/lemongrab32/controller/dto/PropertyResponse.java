package com.github.lemongrab32.controller.dto;

import com.github.lemongrab32.type.Status;

/**
 * DTO для ответа на запросы изменения параметра для расчёта абонементов
 * @param status статус изменения
 * @param message уточняющее сообщение
 * @param name наименование изменяемого параметра
 */
public record PropertyResponse(Status status, String message, String name) {
}
