package com.nicola.ecommerce.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(

    Integer id,

    @NotNull(message = "Nome obbligatorio")
    String name,

    @NotNull(message = "Campo descrizione richiesto")
    String description,

    @Positive(message = "Il prezzo deve essere maggiore di zero")
    BigDecimal price,

    @NotNull(message = "Campo categoria richiesto")
    Integer categoryId

) {
}
