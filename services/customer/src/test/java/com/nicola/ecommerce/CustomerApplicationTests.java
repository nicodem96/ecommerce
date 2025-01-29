package com.nicola.ecommerce;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.4");
	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateCustomer() {
		String requestBody = """
				{
					"firstName": "Mario",
					"lastName": "Rossi",
					"email": "mario@email.it"
				}
				""";

		RestAssured.given()
			.contentType("application/json")
			.body(requestBody)
			.when().post("/api/v1/customer")
			.then().statusCode(201)
			.body("id", Matchers.notNullValue())
			.body("firstName", Matchers.equalTo("Mario"))
			.body("lastName", Matchers.equalTo("Rossi"))
			.body("email", Matchers.equalTo("mario@email.it"));
	}

}
