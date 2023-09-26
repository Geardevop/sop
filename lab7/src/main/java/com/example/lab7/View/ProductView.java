package com.example.lab7.View;

import com.example.lab7.POJO.Product;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("ProductView")
public class ProductView extends VerticalLayout {
    TextField productName;
    ComboBox productList;
    NumberField cost, profit, price;
    Button add, update, delete, clear;
    Double previousCostValue = 0.0;
    Double previousProfitValue = 0.0;
    String productId;
    public ProductView(){
        productName = new TextField("Product Name:");
        productName.setValue("");
        productList = new ComboBox<String>("Product List:");
        cost = new NumberField("Product Cost:");
        cost.setValue(0.0);
        profit = new NumberField("Product Profit:");
        profit.setValue(0.0);
        price = new NumberField("Product Price:");
        price.setEnabled(false);
        VerticalLayout verticalLayout = new VerticalLayout();
        add = new Button("Add Product");
        update = new Button("Update Product");
        delete = new Button("Delete Product");
        clear = new Button("Clear Product");
        productName.addClassName("wide-input");
        cost.addClassName("wide-input");
        profit.addClassName("wide-input");
        price.addClassName("wide-input");
        verticalLayout.add(add, update,delete, clear);
        this.add(productList, productName, cost, profit, price, verticalLayout);

        productList.addAttachListener(e->{
            ArrayList<Product> products = getAllProduct();
            List<String> productNames = products.stream()
                    .map(Product::getProductName)
                    .collect(Collectors.toList());
            productList.setItems(productNames);

        });

        productList.addValueChangeListener(e -> {
            String selectedProductName = e.getValue().toString();
            Optional<Product> selectProduct = getAllProduct()
                    .stream().filter(product -> product.getProductName().equals(selectedProductName)).findFirst();
            if(selectProduct.isPresent()){
                this.productId = selectProduct.get().getProductId();
                productName.setValue(selectProduct.get().getProductName());
                cost.setValue(selectProduct.get().getProductCost());
                profit.setValue(selectProduct.get().getProductProfit());
                price.setValue(selectProduct.get().getProductPrice());
            }
            // Rest of your code to update other fields based on the selected product name
        });
        cost.addFocusListener(e->{
            Double currentCostValue = cost.getValue();
            if (!Objects.equals(currentCostValue, previousCostValue)) {
                previousCostValue = currentCostValue;
                Double replyProductPrice = WebClient.create()
                        .get()
                        .uri("http://localhost:8080/getprice/" + cost.getValue() + "/" +profit.getValue())
                        .retrieve().bodyToMono(Double.class).block();
                price.setValue(replyProductPrice);
            }
        });


        profit.addFocusListener(e->{
            Double currentProfit = profit.getValue();
            if(!Objects.equals(currentProfit, previousCostValue)) {
                previousProfitValue = currentProfit;
                Double replyProductPrice = WebClient.create()
                        .get()
                        .uri("http://localhost:8080/getprice/" + cost.getValue() + "/" + profit.getValue())
                        .retrieve().bodyToMono(Double.class).block();
                price.setValue(replyProductPrice);
            }
        });

        add.addClickListener(e->{
            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addProduct")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(this.createProduct()))
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
            Notification notification = Notification.show(output.toString());
            notification.setPosition(Notification.Position.BOTTOM_END);
            this.setProduct();
        });
        update.addClickListener(e->{
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("id", productId);
            formData.add("productName", productName.getValue());
            formData.add("productCost", cost.getValue().toString());
            formData.add("productProfit", profit.getValue().toString());
            formData.add("productPrice", price.getValue().toString());
            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateProduct")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
            Notification notification = Notification.show(output.toString());
            notification.setPosition(Notification.Position.BOTTOM_END);
            this.setProduct();
        });

        delete.addClickListener(e->{
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("id", productId);
            formData.add("productName", productName.getValue());
            formData.add("productCost", cost.getValue().toString());
            formData.add("productProfit", profit.getValue().toString());
            formData.add("productPrice", price.getValue().toString());
            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteProduct")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
            Notification notification = Notification.show(output.toString());
            notification.setPosition(Notification.Position.BOTTOM_END);
            this.setProduct();
        });
        clear.addClickListener(e->{
            productName.setValue("");
            cost.setValue(0.0);
            price.setValue(0.0);
            profit.setValue(0.0);

        });
    }
    public MultiValueMap<String, String> createProduct(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("productName", productName.getValue());
        formData.add("productCost", cost.getValue().toString());
        formData.add("productProfit", profit.getValue().toString());
        formData.add("productPrice", price.getValue().toString());
        return  formData;
    }

    public ArrayList<Product> getAllProduct(){
        ParameterizedTypeReference<ArrayList<Product>> responseProductType=
                new ParameterizedTypeReference<ArrayList<Product>>() {};
        ArrayList<Product> allProduct = WebClient.create().get()
                .uri("http://localhost:8080/getAllProduct")
                .retrieve()
                .bodyToMono(responseProductType)
                .block();
        return  allProduct;
    };
    public void setProduct(){
        getAllProduct();
        ArrayList<Product> products = getAllProduct();
        List<String> productNames = products.stream()
                .map(Product::getProductName)
                .collect(Collectors.toList());
        productList.setItems(productNames);
    };

}
