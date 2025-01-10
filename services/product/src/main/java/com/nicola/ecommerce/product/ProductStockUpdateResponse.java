package com.nicola.ecommerce.product;

public record ProductStockUpdateResponse(

    Integer productId,

    boolean success,

    String message

) {

}
