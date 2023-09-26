package com.example.lab7.Service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    public double getPrice(double productCost, double profit){
        double price = productCost + profit;
        return  price;
    }
}
