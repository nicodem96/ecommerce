package com.nicola.ecommerce.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nicola.ecommerce.exception.ProductPurchaseException;
import com.nicola.ecommerce.inventory.InventoryClient;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {
        Product product = mapper.toProduct(request);
        return productRepository.save(product).getId();
    }


    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        List<Integer> productIds = request.stream()
            .map(ProductPurchaseRequest::productId)
            .toList();
        List<Product> storedProducts = productRepository.findAllById(productIds);
        if(productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("Uno o piu prodotti non esistono");
        }
        Map<Integer, ProductPurchaseRequest> requestMap = request.stream()
            .collect(Collectors.toMap(ProductPurchaseRequest::productId, Function.identity()));
        List<ProductStockUpdateRequest> inventoryRequests = new ArrayList<>();
        for(Product product : storedProducts) {
            ProductPurchaseRequest productRequest = requestMap.get(product.getId());
            if (productRequest.quantity() <= 0) {
                throw new ProductPurchaseException("Quantita non valida per il prodotto con ID: " + productRequest.productId());
            }
            inventoryRequests.add(new ProductStockUpdateRequest(productRequest.productId(), productRequest.quantity()));
        }
        List<ProductStockUpdateResponse> responses = inventoryClient.updateStockIfAvailable(inventoryRequests);
        for(ProductStockUpdateResponse response : responses) {
            if (!response.success()) {
                throw new ProductPurchaseException("Stock insufficiente per il prodotto con ID:: " + response.productId());
            }
        }
        return storedProducts.stream()
            .map(product -> {
                ProductPurchaseRequest productRequest = requestMap.get(product.getId());
                return mapper.toProductPurchaseResponse(product, productRequest.quantity());
        }).toList();
    }


    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
            .map(mapper::toProductResponse).orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID:: " + productId));
    }


    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
            .map(mapper::toProductResponse)
            .toList();
    }

}
