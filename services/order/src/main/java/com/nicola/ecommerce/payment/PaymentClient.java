package com.nicola.ecommerce.payment;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentClient {

    @PostExchange("/api/v1/payment")
    Integer requestOrderPayment(@RequestBody PaymentRequest request);

}
