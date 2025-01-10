package com.nicola.ecommerce.inventory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.nicola.ecommerce.product.ProductStockUpdateRequest;
import com.nicola.ecommerce.product.ProductStockUpdateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping(path = "/{product-id}")
    public ResponseEntity<List<ProductStockUpdateResponse>> updateStockIfAvailable(@RequestBody List<ProductStockUpdateRequest> requests) {
        return ResponseEntity.ok(inventoryService.updateStockIfAvailable(requests));
    }

}
