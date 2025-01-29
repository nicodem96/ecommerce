package com.nicola.ecommerce;

import org.springframework.boot.SpringApplication;

public class TestInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
