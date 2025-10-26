package com.co.service.product.app.helpers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co.service.product.app.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
