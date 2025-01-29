package com.nicola.ecommerce.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.nicola.ecommerce.email.EmailService;
import com.nicola.ecommerce.kafka.order.OrderConfirmation;
import com.nicola.ecommerce.kafka.payment.PaymentConfirmation;
import com.nicola.ecommerce.notification.Notification;
import com.nicola.ecommerce.notification.NotificationRepository;
import com.nicola.ecommerce.notification.NotificationType;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationsConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumerPaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException{
        log.info(String.format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        repository.save(Notification.builder()
            .notificationType(NotificationType.PAYMENT_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .paymentConfirmation(paymentConfirmation)
            .build());

        String customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();

        emailService.sendPaymentSuccessEmail(paymentConfirmation.customerEmail(), customerName, paymentConfirmation.amount(),
            paymentConfirmation.orderReference());

    }

    @KafkaListener(topics = "order-topic")
    public void consumerOrderSuccessNotification(OrderConfirmation orderConfirmation) throws MessagingException{
        log.info(String.format("Consuming the message from order-topic Topic:: %s", orderConfirmation));
        repository.save(Notification.builder()
            .notificationType(NotificationType.ORDER_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .orderConfirmation(orderConfirmation)
            .build());

        String customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();

        emailService.sendOrderConfirmationEmail(orderConfirmation.customer().email(), customerName, orderConfirmation.totalAmount(),
            orderConfirmation.orderReference(), orderConfirmation.products());
    }

}
