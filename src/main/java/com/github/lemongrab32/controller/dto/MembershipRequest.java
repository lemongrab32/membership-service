package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.constraints.ValueOfEnum;
import com.github.lemongrab32.util.ValueOfEnumValidator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * DTO запроса на расчёт стоимости абонемента или его оформления
 * @param clientId идентификатор клиента
 * @param clientCategory возрастная категория клиента
 * @param clientType тип клиента
 * @param tariffId идентификатор тарифа, по которому оформляется абонемент
 * @param months срок действия оформляемого абонемента в месяцах
 * @param hours количество часов для абонемента с почасовой оплатой (частные клиенты)
 * @param donation сумма пожертвований компании (корпоративные клиенты)
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record MembershipRequest(
	UUID clientId,

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
	String clientType,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	@NotNull(message = Messages.VALIDATION_NOT_NULL_MESSAGE)
	Integer tariffId,

	@Max(value = 12, message = "Значение поля должно быть <= 12")
	@Min(value = 1, message = "Значение поля должно быть >= 1")
	Integer months,

	@Min(value = 1, message = "Значение поля должно быть >= 1")
	@Max(value = 8, message = "Значение поля должно быть <= 8")
	Integer hours,

	@Positive(message = Messages.VALIDATION_POSITIVE_MESSAGE)
	Double donation
) {
}
