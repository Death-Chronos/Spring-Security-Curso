package com.security.productservice.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.security.productservice.dto.Coupon;
import com.security.productservice.models.Product;
import com.security.productservice.repositories.ProductRepository;

@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    ProductRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    // Pegando o valor do application.properties
    @Value("${couponService.url}")
    private String couponServiceUrl;

    @PostMapping("/products")
    public Product create(@RequestBody Product product) {
        Coupon coupon = restTemplate.getForObject(couponServiceUrl + product.getCouponCode(), Coupon.class);
        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));

        return repo.save(product);
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        Optional<Product> op = repo.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
        
    }
}
