package com.nicola.ecommerce.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.nicola.ecommerce.payment.PaymentClient;

@Configuration
public class PaymentClientConfig {

    @Value("${application.config.payment-url}")
    private String paymentServiceUrl;

    @Bean
    public PaymentClient paymentClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(paymentServiceUrl)
            .requestFactory(getClientRequestFactory()).build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PaymentClient.class);
    }

    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings clientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.defaults()
            .withConnectTimeout(Duration.ofSeconds(3))
            .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactoryBuilder.simple().build(clientHttpRequestFactorySettings);
    }
}
