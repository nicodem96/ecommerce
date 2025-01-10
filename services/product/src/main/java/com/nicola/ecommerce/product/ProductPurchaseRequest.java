package com.nicola.ecommerce.product;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(

    @NotNull(message = "ID obbligatorio")
    Integer productId,

    @NotNull(message = "quantità obbligatoria")
    Integer quantity
) {

}
