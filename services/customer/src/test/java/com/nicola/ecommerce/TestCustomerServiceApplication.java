package com.nicola.ecommerce;

import org.springframework.boot.SpringApplication;

public class TestCustomerServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.from(CustomerApplication::main).with(TestContainersConfiguration.class).run(args);
    }
}
