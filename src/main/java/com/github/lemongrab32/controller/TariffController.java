package com.github.lemongrab32.controller;

import com.github.lemongrab32.controller.dto.TariffRequest;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

	@GetMapping
	public ResponseEntity<List<TariffRequest>> getTariffs(
		@RequestParam("offset") int offset, @RequestParam("limit") int limit
	) {
		return ResponseEntity.ok(tariffService.getTariffs(PageRequest.of(offset, limit)));
	}

	@PostMapping
	public ResponseEntity<TariffResponse> createTariff(@RequestBody @Validated TariffRequest request) {
		return new ResponseEntity<>(tariffService.save(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TariffResponse> updateTariff(
		@PathVariable Integer id, @RequestBody @Validated TariffRequest request
	) {
		return ResponseEntity.ok(tariffService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<TariffResponse> deleteTariff(@PathVariable Integer id) {
		return ResponseEntity.ok(tariffService.delete(id));
	}

}
