package com.erb.demo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CurrencyConversionService {

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public CurrencyConversionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Double> convertToILS(double amount, String fromCurrency) {
        String url = String.format("%s/%s/pair/%s/ILS", apiUrl, apiKey, fromCurrency);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Double conversionRate = (Double) response.get("conversion_rate");
                    return amount * conversionRate;
                });
    }
}
