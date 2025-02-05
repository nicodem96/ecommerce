package com.nicola.ecommerce.orderline;

import org.springframework.stereotype.Service;

import com.nicola.ecommerce.order.Order;

@Service
public class OrderLineMapper {
    
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
            .id(request.orderId())
            .productId(request.productId())
            .order(
                Order.builder()
                    .id(request.orderId())
                    .build()
            )
            .quantity(request.quantity())
            .version(request.version())
            .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
            orderLine.getId(),
            orderLine.getQuantity(),
            orderLine.getVersion()
        );
    }
}
