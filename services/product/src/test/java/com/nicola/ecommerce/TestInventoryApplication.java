package com.nicola.ecommerce;

import org.springframework.boot.SpringApplication;

public class TestInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
