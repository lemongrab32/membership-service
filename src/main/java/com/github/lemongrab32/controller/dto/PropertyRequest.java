package com.github.lemongrab32.controller.dto;

import com.github.lemongrab32.type.Messages;
import jakarta.validation.constraints.NotNull;

/**
 * DTO запроса на изменение значения параметра для расчёта абонементов
 * @param name наименование параметра
 * @param value новое значение параметра
 */
public record PropertyRequest(
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	String name,
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	Object value
) {
}
