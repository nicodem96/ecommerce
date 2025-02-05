package com.nicola.ecommerce.stubs;

import lombok.experimental.UtilityClass;

import com.github.tomakehurst.wiremock.client.WireMock;

@UtilityClass
public class InventoryStubs {
    public void stubInventoryCall(String productId, Integer quantity) {
        WireMock.stubFor(WireMock.put(WireMock.urlPathMatching("/api/v1/inventory/" + productId))
            .withPort(8030)
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"productId\":\"" + productId + "\",\"available\":true,\"message\":\"Product available\"}]")));
    }
}

