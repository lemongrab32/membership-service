package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO сообщения для отправки данных в Kafka
 * @param finalPrice рассчитанная стоимость абонемента
 * @param membershipId идентификатор оформленного абонемента
 * @param startDate дата начала действия абонемента
 * @param endDate дата окончания действия абонемента
 * @param hoursRemaining количество оставшихся часов (абонементы с почасовой оплатой)
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record NotificationRequest(
	Double finalPrice,
	Long membershipId,
	LocalDate startDate,
	LocalDate endDate,
	Integer hoursRemaining
) {}
