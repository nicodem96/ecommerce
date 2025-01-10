package com.nicola.ecommerce.payment;

import java.math.BigDecimal;

import com.nicola.ecommerce.customer.CustomerResponse;
import com.nicola.ecommerce.order.PaymentMethod;

public record PaymentRequest(

    BigDecimal amount,

    PaymentMethod paymentMethod,

    Integer orderId,

    String orderReference,

    CustomerResponse customer

) {
}