package com.erb.demo.controller;

import com.erb.demo.service.impl.CurrencyConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private  CurrencyConversionService conversionService;

    public CurrencyController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/convert")
    public Double convert(@RequestParam double amount, @RequestParam String from) {
        return conversionService.convertToILS(amount, from).block();

    }
}
