package com.nicola.ecommerce.product;

public record ProductStockUpdateRequest(

    Integer productId,

    Integer quantity
) {

}
