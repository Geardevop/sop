package com.example.lab7.Controller;

import com.example.lab7.Service.CalculatorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorPriceController {
    @Autowired
    private CalculatorPriceService calculatorPriceService;

    @GetMapping(value = "/getprice/{cost}/{profit}")
    public double  serviceGetProducts(@PathVariable double cost, @PathVariable double profit){
        return  calculatorPriceService.getPrice(cost,profit);
    }
}
