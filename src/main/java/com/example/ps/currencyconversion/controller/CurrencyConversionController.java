package com.example.ps.currencyconversion.controller;

import com.example.ps.currencyconversion.bean.CurrencyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment env;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        HashMap<String, String> uriVars = new HashMap<>();
        uriVars.put("from", from);
        uriVars.put("to", to);
        CurrencyConversion exchange = new RestTemplate()
                .getForEntity(
                        "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversion.class,
                        uriVars
                ).getBody();
        Assert.notNull(exchange, "Unable to to get exchange info.");

        return new CurrencyConversion(exchange.getId(), from, to, exchange.getConversionMultiple(), quantity,
                quantity.multiply(exchange.getConversionMultiple()), env.getProperty("local.server.port"));
    }
}
