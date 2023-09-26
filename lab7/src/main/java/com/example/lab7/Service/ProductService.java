package com.example.lab7.Service;

import com.example.lab7.POJO.Product;
import com.example.lab7.Repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @CacheEvict(value = "myProduct")
    @RabbitListener(queues = "AddProductQueue")
    public boolean addProduct(Product product){
        productRepository.insert(product);
        return  true;
    }
    @RabbitListener(queues = "UpdateProductQueue")
    @CachePut(value = "myProduct")
    public  boolean updateProduct(Product product){
        productRepository.save(product);
        return  true;
    }
    @CacheEvict(value = "myProduct")
    @RabbitListener(queues = "DeleteProductQueue")
    public boolean deleteProduct(Product product){
        try{
            productRepository.delete(product);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    @Cacheable(value = "myProduct")
    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName(Product product){

        return productRepository.findByName(product.getProductName());
    }
}
