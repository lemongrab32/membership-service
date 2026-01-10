package com.github.lemongrab32.controller.handler;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
import com.github.lemongrab32.exception.PaginationException;
import com.github.lemongrab32.exception.TariffNotFoundException;
import com.github.lemongrab32.exception.UnknownPropertyException;
import com.github.lemongrab32.type.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

/**
 * Обработчик ошибок контроллеров для отправки корректных ответов
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Обработка ошибок валидации
	 * @param ex тело ошибки
	 * @return список полей и соответствующих им ошибок валидации
	 */
	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<?> handleWebExchangeBindException(WebExchangeBindException ex) {
		var map = ex.getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid value"
			));

		return ResponseEntity.badRequest().body(map);
	}

	/**
	 * Обработка ошибок валидации
	 * @param ex тело ошибки
	 * @return список полей и соответствующих им ошибок валидации с кодом ответа 400
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		var map = ex.getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid value"
			));

		return ResponseEntity.badRequest().body(map);
	}

	/**
	 * Обработка ошибок, связанных с некорректными данными клиента
	 * @param ex тело ошибки
	 * @return {@link CalculationResponse} с текстом ошибки и кодом ответа 400
	 */
	@ExceptionHandler(InvalidClientOptionsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CalculationResponse handleInvalidClientOptionsException(InvalidClientOptionsException ex) {
		return new CalculationResponse(Status.ERROR, ex.getMessage(), null);
	}

	/**
	 * Обработка ошибок, связанных с отсутствием искомого тарифа или с указанием некорректного идентификатора
	 * @param ex тело ошибки
	 * @return {@link TariffResponse} с текстом ошибки и кодом ответа 400
	 */
	@ExceptionHandler(TariffNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public TariffResponse handleTariffNotFoundException(TariffNotFoundException ex) {
		return new TariffResponse(Status.ERROR, ex.getMessage(), ex.getTariffId());
	}

	@ExceptionHandler(PaginationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public TariffResponse handlePaginationException(PaginationException ex) {
		return new TariffResponse(Status.ERROR, ex.getMessage(), null);
	}

	/**
	 * Обработка ошибок, связанных с указанием некорректного наименования параметра
	 * @param ex тело ошибки
	 * @return {@link PropertyResponse} с текстом ошибки и кодом ответа 400
	 */
	@ExceptionHandler(UnknownPropertyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public PropertyResponse handleUnknownPropertyException(UnknownPropertyException ex) {
		return new PropertyResponse(
			Status.ERROR, ex.getMessage(), ex.getPropertyName()
		);
	}

}
