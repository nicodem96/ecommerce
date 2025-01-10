package com.nicola.ecommerce.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PutExchange;

import com.nicola.ecommerce.product.ProductStockUpdateRequest;
import com.nicola.ecommerce.product.ProductStockUpdateResponse;

public interface InventoryClient {

    @PutExchange
    List<ProductStockUpdateResponse> updateStockIfAvailable(@RequestBody List<ProductStockUpdateRequest> requests);

}
