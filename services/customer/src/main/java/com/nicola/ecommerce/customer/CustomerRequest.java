package com.nicola.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(

    String id,

    @NotNull(message = "Campo obbligatorio")
    String firstName,

    @NotNull(message = "Campo obbligatorio")
    String lastName,

    @Email(message = "Email non valida")
    @NotNull(message = "Campo obbligatorio")
    String email,

    Indirizzo address) {
}

