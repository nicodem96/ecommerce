package com.nicola.ecommerce.kafka;

import java.math.BigDecimal;
import java.util.List;

import com.nicola.ecommerce.customer.CustomerResponse;
import com.nicola.ecommerce.order.PaymentMethod;
import com.nicola.ecommerce.product.PurchaseResponse;

public record OrderConfirmation(

    String orderReference,

    BigDecimal totalAmount,

    PaymentMethod paymentMethod,

    CustomerResponse customer,

    List<PurchaseResponse> products

) {
}