package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.controller.dto.PropertyResponse;

import java.util.Map;

/**
 * Сервисный уровень для работы с параметрами для расчёта
 */
public interface PropertyService {

	/**
	 * Получить все параметры для расчёта стоимости абонемента (размеры скидок и т. п.)
	 * @return ассоциативный массив со всеми параметрами
	 */
	Map<String, Object> getProperties();

	/**
	 * Задать значение параметра (размера скидки или одно из граничных значений пожертвований)
	 *
	 * @param request название параметра и новое значение
	 * @return обновлённое значение параметра
	 */
	PropertyResponse setProperty(PropertyRequest request);

}
