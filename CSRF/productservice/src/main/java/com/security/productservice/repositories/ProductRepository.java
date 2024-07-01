package com.security.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.productservice.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
