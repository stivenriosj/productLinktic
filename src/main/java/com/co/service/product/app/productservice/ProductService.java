package com.co.service.product.app.productservice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.co.service.product.app.model.Product;

public interface ProductService {

	  public Product create(Product p);	  
	  public Optional<Product> findById(Long id);	  
	  public Page<Product> list(Pageable p);
	  public Product update(Long id, Product updated);
	  public void delete(Long id);
}
