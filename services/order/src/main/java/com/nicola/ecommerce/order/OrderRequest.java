package com.nicola.ecommerce.order;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nicola.ecommerce.product.PurchaseRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record OrderRequest(

    Integer id,

    String reference,

    @Positive(message = "L'importo deve essere maggiore di zero")
    BigDecimal amount,

    @NotNull(message = "Il metodo di pagamento deve essere specificato")
    PaymentMethod paymentMethod,

    @NotBlank(message = "Specificare l'ID del cliente")
    String customerId,

    @NotEmpty(message = "Inserire almeno un prodotto")
    List<PurchaseRequest> products

) {


}
