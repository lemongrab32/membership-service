package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.exception.PaginationException;
import com.github.lemongrab32.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов на управление тарифами
 */
@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

	private final TariffService tariffService;

	/**
	 * Обработка запросов на получения списка тарифов с пагинацией
	 * @param offset номер страницы списка
	 * @param limit количество элементов на странице
	 * @return список тарифов по указанным параметрам пагинации
	 */
	@GetMapping
	public ResponseEntity<List<TariffDto>> getTariffs(
		@RequestParam(name = "offset", defaultValue = "0") int offset,
		@RequestParam(name = "limit", defaultValue = "10") int limit
	) {
		PageRequest request;
		try {
			request = PageRequest.of(offset, limit);
		} catch (IllegalArgumentException ex) {
			throw new PaginationException("Заданы некорректные параметры пагинации");
		}
		return ResponseEntity.ok(tariffService.getTariffs(request));
	}

	/**
	 * Обработка запросов на добавление нового тарифа
	 * @param request {@link TariffDto} тело запроса
	 * @return {@link TariffResponse} с кодом ответа 201
	 */
	@PostMapping
	public ResponseEntity<TariffResponse> createTariff(@RequestBody @Validated TariffDto request) {
		return new ResponseEntity<>(tariffService.save(request), HttpStatus.CREATED);
	}

	/**
	 * Обработка запросов на обновление тарифа
	 * @param id идентификатор обновляемого тарифа
	 * @param request {@link TariffDto} тело запроса
	 * @return {@link TariffResponse} с кодом ответа 200
	 */
	@PutMapping("/{id}")
	public ResponseEntity<TariffResponse> updateTariff(
		@PathVariable Integer id, @RequestBody @Validated TariffDto request
	) {
		return ResponseEntity.ok(tariffService.update(id, request));
	}

	/**
	 * Обработка запросов на удаление тарифа
	 * @param id идентификатор обновляемого тарифа
	 * @return {@link TariffResponse} с кодом ответа 200
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<TariffResponse> deleteTariff(@PathVariable Integer id) {
		return ResponseEntity.ok(tariffService.delete(id));
	}

}
