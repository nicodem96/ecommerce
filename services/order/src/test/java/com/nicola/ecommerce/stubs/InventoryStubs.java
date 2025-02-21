package com.nicola.ecommerce.stubs;

import lombok.experimental.UtilityClass;

import com.github.tomakehurst.wiremock.client.WireMock;

@UtilityClass
public class InventoryStubs {
    public void stubInventoryCall(Integer productId, Integer quantity) {
        WireMock.stubFor(WireMock.put("/api/v1/inventory")
            .withRequestBody(WireMock.containing("\"productId\":" + productId))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"productId\":" + productId +
                         ",\"available\":true,\"quantity\":" + quantity +
                         ",\"message\":\"Product available\"}]")));
    }
}

