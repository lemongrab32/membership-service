package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.constraints.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO запроса на добавление нового или обновление существующего тарифа
 * @param name наименование тарифа
 * @param basePrice базовая стоимость 1 месяца занятий по тарифу
 * @param clientCategory возрастная категория клиента
 * @param clientType тип клиента
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record TariffDto(
	@Size(min = 2, max = 100, message = "Количество символов в имени должно быть от 2 до 100")
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	String name,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	Double basePrice,

	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	@ValueOfEnum(
		enumClass = ClientCategory.class,
		message = Messages.VALIDATION_CATEGORY_INCORRECT_MESSAGE
	)
	String clientCategory,

	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	@ValueOfEnum(
		enumClass = ClientType.class,
		message = Messages.VALIDATION_TYPE_INCORRECT_MESSAGE
	)
	String clientType

) {}
