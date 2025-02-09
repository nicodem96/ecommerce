package com.nicola.ecommerce.payment;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentClient {

    @PostExchange("/create")
    Integer createPayment(@RequestBody PaymentRequest request);

}
