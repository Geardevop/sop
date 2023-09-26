package com.example.lab7.POJO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.security.SecureRandom;

@Data
@Document("Product")
public class Product implements Serializable {
    @Id
    private String productId;
    private String productName;
    private Double productCost;
    private Double productProfit;
    private Double productPrice;
    public Product() {

    }

    public Product(
                   String productName,
                   Double productCost,
                   Double productProfit,
                   Double productPrice) {
        this.productName = productName;
        this.productCost = productCost;
        this.productProfit = productProfit;
        this.productPrice = productPrice;
    }
    public Product(String productName, Double productCost, Double productProfit){
        this.productName = productName;
        this.productCost = productCost;
        this.productProfit = productProfit;
    }

    public Product(String productId, String productName, Double productCost, Double productProfit, Double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productCost = productCost;
        this.productProfit = productProfit;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductCost() {
        return productCost;
    }

    public void setProductCost(Double productCost) {
        this.productCost = productCost;
    }

    public Double getProductProfit() {
        return productProfit;
    }

    public void setProductProfit(Double productProfit) {

        this.productProfit = productProfit;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}
