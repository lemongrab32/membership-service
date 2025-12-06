package com.github.lemongrab32.client;

import com.github.lemongrab32.controller.dto.PaymentRequest;
import com.github.lemongrab32.controller.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service-client", url = "${client.payment.url}")
public interface PaymentServiceClient {

	@PostMapping
	void createPayment(@RequestBody PaymentRequest paymentRequest);

}
