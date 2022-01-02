package com.example.ps.currencyconversion.controller;

import com.example.ps.currencyconversion.bean.CurrencyConversion;
import com.example.ps.currencyconversion.webclient.CurrencyExchangeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyExchangeClient currencyExchangeClient;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        CurrencyConversion exchange = currencyExchangeClient.retrieveExchangeValue(from, to);
        Assert.notNull(exchange, "Unable to to get exchange info.");

        return new CurrencyConversion(exchange.getId(), from, to, exchange.getConversionMultiple(), quantity,
                quantity.multiply(exchange.getConversionMultiple()), exchange.getEnvironment());
    }
}
