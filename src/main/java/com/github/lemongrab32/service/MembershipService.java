package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.*;

import java.util.Map;

public interface MembershipService {

	/**
	 * Расчёт стоимости абонемента
	 * @param request параметры для расчёта
	 * @return результаты расчёта
	 */
	CalculationResponse calculateMembership(MembershipRequest request);

	/**
	 * Оформление абонемента
	 * @param request параметры для расчёта стоимости и проведения платежа
	 * @return результаты процесса оформления
	 */
	MembershipResponse getMembership(MembershipRequest request);

}
