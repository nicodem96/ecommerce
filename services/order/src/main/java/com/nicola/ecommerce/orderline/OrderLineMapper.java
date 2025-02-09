package com.nicola.ecommerce.orderline;

import org.springframework.stereotype.Service;

import com.nicola.ecommerce.order.Order;

@Service
public class OrderLineMapper {
    
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
            .productId(request.productId())
            .order(
                Order.builder()
                    .id(request.orderId())
                    .build()
            )
            .quantity(request.quantity())
            .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
            orderLine.getId(),
            orderLine.getQuantity()
        );
    }
}
