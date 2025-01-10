package com.nicola.ecommerce.notification;

import java.math.BigDecimal;

import com.nicola.ecommerce.payment.PaymentMethod;

public record PaymentNotificationRequest(

    String orderReference,

    BigDecimal amount,

    PaymentMethod paymentMethod,

    String customerFirstName,

    String customerLastName,

    String customerEmail

) {
}
