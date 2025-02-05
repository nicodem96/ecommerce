package com.nicola.ecommerce.orderline;

public record OrderLineRequest(
    Integer id,
    Integer orderId,
    Integer productId,
    double quantity,
    Integer version
) {

}
