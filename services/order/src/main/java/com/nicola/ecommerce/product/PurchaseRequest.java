package com.nicola.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(

    @NotNull(message = "Il prodotto è obbligatorio")
    Integer productId,

    @Positive(message = "La quantità è obbligatoria")
    double quantity
) {

}
