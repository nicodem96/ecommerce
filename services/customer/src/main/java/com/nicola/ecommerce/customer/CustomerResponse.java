package com.nicola.ecommerce.customer;


public record CustomerResponse(

    String id,

    String nome,

    String cognome,

    String email,

    Indirizzo indirizzo
) {

}
