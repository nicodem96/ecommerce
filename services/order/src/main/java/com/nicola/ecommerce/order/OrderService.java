package com.nicola.ecommerce.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicola.ecommerce.customer.CustomerClient;
import com.nicola.ecommerce.customer.CustomerResponse;
import com.nicola.ecommerce.exception.BusinessException;
import com.nicola.ecommerce.kafka.OrderConfirmation;
import com.nicola.ecommerce.kafka.OrderProducer;
import com.nicola.ecommerce.orderline.OrderLineRequest;
import com.nicola.ecommerce.orderline.OrderLineService;
import com.nicola.ecommerce.payment.PaymentClient;
import com.nicola.ecommerce.payment.PaymentRequest;
import com.nicola.ecommerce.product.ProductClient;
import com.nicola.ecommerce.product.PurchaseRequest;
import com.nicola.ecommerce.product.PurchaseResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderMapper mapper;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;


    public Integer createOrder(OrderRequest request) {
        CustomerResponse customer = customerClient.findCustomerById(request.customerId())
            .orElseThrow(() -> new BusinessException("Impossibile creare l'ordine: Non è stato possibile trovare il cliente"));
        List<PurchaseResponse> purchasedProducts;
        try {
            purchasedProducts = productClient.purchaseProducts(request.products());
        } catch (RuntimeException e) {
            throw new BusinessException("Impossibile creare l'ordine: Non è stato possibile acquistare i prodotti");
        }
        Order order = orderRepository.save(mapper.toOrder(request));
        for(PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(new OrderLineRequest(order.getId(), purchaseRequest.productId(), purchaseRequest.quantity()));
        }
        PaymentRequest paymentRequest = new PaymentRequest(request.amount(), request.paymentMethod(), order.getId(),
             order.getReference(), customer);
        try {
            paymentClient.createPayment(paymentRequest);
        } catch (RuntimeException e) {
            throw new BusinessException("Impossibile creare l'ordine: Non è stato possibile effettuare il pagamento");
        }
        orderProducer.sendOrderConfirmation(
            new OrderConfirmation(request.reference(), request.amount(), request.paymentMethod(), customer, purchasedProducts)
        );
        return order.getId();
    }


    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
            .stream()
            .map(mapper::fromOrder)
            .toList();
    }


    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
            .map(mapper::fromOrder)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Ordine non trovato con ID: %d", orderId)));
    }
}
