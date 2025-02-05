package com.nicola.ecommerce;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

import com.nicola.ecommerce.stubs.InventoryStubs;
import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderApplicationTests {

    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.1.0");
    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                     "reference": "order_123",
                     "amount": 1000,
                     "paymentMethod": "CREDIT_CARD",
                     "customerId": "cust_123",
                     "products": [
                         {
                             "productId": "iphone_15",
                             "quantity": 1
                         }
                     ]
                }
                """;

        InventoryStubs.stubInventoryCall("iphone_15", 1);
        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, is("1"));
    }

    @Test
    void shouldFindAllOrders() {
        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(responseBodyString, is("[]"));
    }

    @Test
    void shouldFindOrderById() {
        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/orders/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(responseBodyString, is("{\"id\":1,\"reference\":\"order_123\",\"amount\":1000,\"paymentMethod\":\"CREDIT_CARD\",\"customerId\":\"cust_123\"}"));
    }
}