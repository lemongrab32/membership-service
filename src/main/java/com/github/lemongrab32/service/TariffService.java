package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.TariffRequest;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.model.Tariff;
import org.springframework.data.domain.Page;
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
	List<TariffRequest> getTariffs(Pageable pageable);

	/**
	 * Сохранение нового тарифа
	 * @param request данные добавляемого тарифа
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse save(TariffRequest request);

	/**
	 * Обновление данных тарифа
	 * @param tariffId идентификатор тарифа
	 * @param request новые данные
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse update(Integer tariffId, TariffRequest request);

	/**
	 * Удаление тарифа
	 * @param tariffId идентификатор тарифа
	 * @return dto с информацией о выполнении операции
	 */
	TariffResponse delete(Integer tariffId);

}
