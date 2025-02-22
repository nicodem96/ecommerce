package com.nicola.ecommerce.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicola.ecommerce.exception.ProductNotFoundException;
import com.nicola.ecommerce.product.ProductStockUpdateRequest;
import com.nicola.ecommerce.product.ProductStockUpdateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<ProductStockUpdateResponse> updateStockIfAvailable(List<ProductStockUpdateRequest> requests) {
        List<ProductStockUpdateResponse> stockUpdateResponses = new ArrayList<>();
        for(ProductStockUpdateRequest request : requests) {
            Inventory inventoryProduct = inventoryRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + request.productId()));
            if(inventoryProduct.getQuantity() < request.quantity()) {
                stockUpdateResponses.add(new ProductStockUpdateResponse(request.productId(), false, "Stock Insufficiente per il prodotto"));
            } else {
                inventoryProduct.setQuantity(inventoryProduct.getQuantity() - request.quantity());
                inventoryRepository.save(inventoryProduct);
                stockUpdateResponses.add(new ProductStockUpdateResponse(request.productId(), true, "Prodotto Disponibile"));
            }
        }
        return stockUpdateResponses;
    }
}

