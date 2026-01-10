package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.model.Tariff;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Сервисный уровень для работы с тарифами
 */
public interface TariffService {

	/**
	 * Получение списка тарифов с указанными параметрами пагинации
	 * @param pageable параметры пагинации
	 * @return страница с указанным количеством тарифов
	 */
	List<TariffDto> getTariffs(Pageable pageable);

	/**
	 * Получение тарифа по его идентификатору
	 * @param id идентификатор тарифа
	 * @return искомый тариф
	 */
	Tariff getTariffById(Integer id);

	/**
	 * Сохранение нового тарифа
	 * @param request данные добавляемого тарифа
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse save(TariffDto request);

	/**
	 * Обновление данных тарифа
	 * @param tariffId идентификатор тарифа
	 * @param request новые данные
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse update(Integer tariffId, TariffDto request);

	/**
	 * Удаление тарифа
	 * @param tariffId идентификатор тарифа
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse delete(Integer tariffId);

}
