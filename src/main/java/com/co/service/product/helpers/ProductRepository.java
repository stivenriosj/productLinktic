package com.co.service.product.helpers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co.service.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}