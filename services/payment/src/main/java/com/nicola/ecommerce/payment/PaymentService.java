package com.nicola.ecommerce.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicola.ecommerce.notification.NotificationProducer;
import com.nicola.ecommerce.notification.PaymentNotificationRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        Payment payment = repository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(new PaymentNotificationRequest(
            request.orderReference(), request.amount(), request.paymentMethod(), request.customer().firstName(),
           request.customer().lastName(), request.customer().email()));
        return payment.getId();
    }

}
