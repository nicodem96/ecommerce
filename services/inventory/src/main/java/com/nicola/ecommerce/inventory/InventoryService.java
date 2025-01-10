package com.nicola.ecommerce.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.nicola.ecommerce.exception.ProductNotFoundException;
import com.nicola.ecommerce.product.ProductStockUpdateRequest;
import com.nicola.ecommerce.product.ProductStockUpdateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<ProductStockUpdateResponse> updateStockIfAvailable(List<ProductStockUpdateRequest> requests) {
        List<ProductStockUpdateResponse> stockUpdateResponses = new ArrayList<>();
        for(ProductStockUpdateRequest request : requests) {
            Inventory inventoryProduct = inventoryRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + request.productId()));
            if(inventoryProduct.getQuantity() < request.quantity()) {
                stockUpdateResponses.add(new ProductStockUpdateResponse(request.productId(), false, "Insufficient stock"));
            } else {
                inventoryProduct.setQuantity(inventoryProduct.getQuantity() - request.quantity());
                inventoryRepository.save(inventoryProduct);
                stockUpdateResponses.add(new ProductStockUpdateResponse(request.productId(), true, "Product available"));
            }
        }
        return stockUpdateResponses;
    }
}

