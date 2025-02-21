package com.nicola.ecommerce;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.nicola.ecommerce.order.OrderResponse;
import com.nicola.ecommerce.stubs.InventoryStubs;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderApplicationTests {

    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.1.0");

    private static WireMockServer wireMockServer;

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        wireMockServer.resetAll();
    }

    @Test
    void shouldSubmitOrder() {
        // Stub customer service response
        WireMock.stubFor(WireMock.get("/api/v1/customers/67a261e7464fbd74570410f4")
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"id\":\"67a261e7464fbd74570410f4\",\"name\":\"Test Customer\"}")));

        String submitOrderJson = """
                {
                     "reference": "order_123",
                     "amount": 1000,
                     "paymentMethod": "CREDIT_CARD",
                     "customerId": "67a261e7464fbd74570410f4",
                     "products": [
                         {
                             "productId": 3,
                             "quantity": 1
                         }
                     ]
                }
                """;

        InventoryStubs.stubInventoryCall(3, 1);

        var orderId = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(Integer.class);

        assertThat(orderId, is(1));
    }

    @Test
    void shouldFindAllOrders() {
        var orders = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(OrderResponse[].class);

        assertThat(orders.length, is(0));
    }

    @Test
    void shouldFindOrderById() {
        var order = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/orders/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(OrderResponse.class);

        assertThat(order.id(), is(1));
        assertThat(order.reference(), is("order_123"));
        assertThat(order.amount().intValue(), is(1000));
        assertThat(order.paymentMethod().toString(), is("CREDIT_CARD"));
        assertThat(order.customerId(), is("67a261e7464fbd74570410f4"));
    }
}