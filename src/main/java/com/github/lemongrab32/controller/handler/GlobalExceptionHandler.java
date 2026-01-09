package com.github.lemongrab32.controller.handler;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.exception.InvalidClientOptionsException;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<?> handleWebExchangeBindException(WebExchangeBindException ex) {
		var map = ex.getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid value"
			));

		return ResponseEntity.badRequest().body(map);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		var map = ex.getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid value"
			));

		return ResponseEntity.badRequest().body(map);
	}

	@ExceptionHandler(InvalidClientOptionsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CalculationResponse handleInvalidClientOptionsException(InvalidClientOptionsException ex) {
		return new CalculationResponse(Status.ERROR, ex.getMessage(), null);
	}

	@ExceptionHandler(TariffNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public TariffResponse handleTariffNotFoundException(TariffNotFoundException ex) {
		return new TariffResponse(Status.ERROR, ex.getMessage(), ex.getTariffId());
	}

	@ExceptionHandler(UnknownPropertyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public PropertyResponse handleUnknownPropertyException(UnknownPropertyException ex) {
		return new PropertyResponse(
			Status.ERROR, ex.getMessage(), ex.getPropertyName()
		);
	}

}
