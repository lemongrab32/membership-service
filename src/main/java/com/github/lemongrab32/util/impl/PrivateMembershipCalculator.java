package com.github.lemongrab32.util.impl;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.util.MembershipCalculator;
import lombok.AllArgsConstructor;

/**
 * Калькулятор, рассчитывающий стоимость абонементов частных клиентов
 */
@AllArgsConstructor
public class PrivateMembershipCalculator extends MembershipCalculator {

	private final Double hourMultiplier;
	private final Double midMultiplier;
	private final Double maxMultiplier;

	@Override
	public double calculate(MembershipRequest request, Double basePrice) {
		if (request.hours() != null && request.hours() > 0) {
			return calcHourly(request, basePrice);
		}

		return calcMonthly(request, basePrice);
	}

	/**
	 * Расчёт стоимости абонемента с почасовой оплатой
	 * @param request {@link MembershipRequest} тело запроса
	 * @param basePrice базовая стоимость 1 месяца занятий по тарифу
	 * @return рассчитанная стоимость абонемента
	 */
	private double calcHourly(MembershipRequest request, Double basePrice) {
		return request.hours() * (basePrice * hourMultiplier);
	}

	/**
	 * Расчёт стоимости абонемента по месячному плану
	 * @param request {@link MembershipRequest} тело запроса
	 * @param basePrice базовая стоимость 1 месяца занятий по тарифу
	 * @return рассчитанная стоимость абонемента
	 */
	private double calcMonthly(MembershipRequest request, Double basePrice) {
		int months = request.months();

		if (months >= 1 && months < 3) {
			return months * basePrice;
		} else if (months >= 3 && months <= 6) {
			return months * (basePrice - basePrice * midMultiplier);
		} else {
			return months * (basePrice - basePrice * maxMultiplier);
		}
	}

}
