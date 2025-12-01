package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.MembershipResponse;
import com.github.lemongrab32.controller.dto.PropertyRequest;

import java.util.Map;

public interface MembershipService {

	/**
	 * Расчёт стоимости абонемента
	 * @param request параметры для расчёта
	 * @return результаты расчёта
	 */
	CalculationResponse calculate(MembershipRequest request);

	/**
	 * Оформление абонемента
	 * @param request параметры для расчёта стоимости и проведения платежа
	 * @return результаты процесса оформления
	 */
	MembershipResponse get(MembershipRequest request);

	/**
	 * Получить все параметры для расчёта стоимости абонемента (размеры скидок и т. п.)
	 * @return ассоциативный массив со всеми параметрами
	 */
	Map<String, Object> getProperties();

	/**
	 * Задать значение параметра (размера скидки или одно из граничных значений пожертвований)
	 * @param request название параметра и новое значение
	 * @return обновлённое значение параметра
	 */
	String setProperty(PropertyRequest request);

}
