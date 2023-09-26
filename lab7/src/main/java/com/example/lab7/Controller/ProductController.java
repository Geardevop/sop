package com.example.lab7.Controller;

import com.example.lab7.POJO.Product;
import com.example.lab7.Service.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping(value = "addProduct")
    public boolean serviceAddProduct(@RequestBody MultiValueMap<String, String> formData){
        Map<String, String> map = formData.toSingleValueMap();
        Product product = new Product(map.get("productName"),
                Double.parseDouble(map.get("productCost")),
                Double.parseDouble(map.get("productProfit")),
                Double.parseDouble(map.get("productPrice")));
        try {
            rabbitTemplate.convertSendAndReceive(
                    "ProductExchange",
                    "add",product);
            return  true;
        }catch (Exception e){
            return  false;
        }
    }
    @PostMapping(value = "updateProduct")
    public boolean serviceUpdateProduct(@RequestBody MultiValueMap<String, String> formData){
        Map<String, String> map = formData.toSingleValueMap();
        Product product = new Product(
                map.get("id"),
                map.get("productName"),
                Double.parseDouble(map.get("productCost")),
                Double.parseDouble(map.get("productProfit")),
                Double.parseDouble(map.get("productPrice"))
        );
        if(map.get("id") == null){
            return  false;
        }
        try {
            rabbitTemplate.convertSendAndReceive(
                    "ProductExchange",
                    "update",product);
            return  true;
        }catch (Exception e){
            return  false;
        }
    }
    @PostMapping(value = "deleteProduct")
    public boolean serviceDelateProduct(@RequestBody MultiValueMap<String, String> formData){
        Map<String, String> map = formData.toSingleValueMap();
        Product product = new Product(
                map.get("id"),
                map.get("productName"),
                Double.parseDouble(map.get("productCost")),
                Double.parseDouble(map.get("productProfit")),
                Double.parseDouble(map.get("productPrice"))
        );
        try {
            rabbitTemplate.convertSendAndReceive(
                    "ProductExchange",
                    "delete",product);
            return  true;

        }catch (Exception e){
            return  false;
        }
    }
    @PostMapping(value = "getProductName")
    public boolean serviceGetProductName(Product product){
        try {
            rabbitTemplate.convertSendAndReceive(
                    "ProductExchange",
                    "getname",product);
            return  true;
        }catch (Exception e){
            return  false;
        }
    }
    @GetMapping(value = "getAllProduct")
    public ArrayList<Product> serviceGetAllProduct(){
        Object replyListProduct=  rabbitTemplate.convertSendAndReceive(
                "ProductExchange",
                "getal","");
        ArrayList<Product> productArrayList;
        try {
            productArrayList = ((ArrayList<Product>) replyListProduct);
        }catch (Exception e){
            productArrayList = new ArrayList<Product>();
        }
        return  productArrayList;
    }

}
