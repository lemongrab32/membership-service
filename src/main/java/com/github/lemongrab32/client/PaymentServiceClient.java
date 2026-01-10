package com.github.lemongrab32.client;

import com.github.lemongrab32.controller.dto.PaymentRequest;
import com.github.lemongrab32.controller.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Компонент для имитации обращения к API платёжного сервиса
 */
@Slf4j
public class PaymentServiceClient {

	public void createPayment(PaymentRequest request) {
		log.info("Платёж создан для запроса {}", request);
	}

}
