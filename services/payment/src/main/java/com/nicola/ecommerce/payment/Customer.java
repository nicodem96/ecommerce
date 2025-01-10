package com.nicola.ecommerce.payment;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Validated
public record Customer(
    String id,

    @NotNull(message = "Campo obbligatorio")
    String firstName,

    @NotNull(message = "Campo obbligatorio")
    String lastName,

    @Email(message = "Formato non corretto")
    @NotNull(message = "Campo obbligatorio")
    String email
) {
}
