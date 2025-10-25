package com.co.service.product.productimple;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.co.service.product.helpers.ProductRepository;
import com.co.service.product.model.Product;
import com.co.service.product.productservice.ProductService;

@Service
public class ProductServiceImple implements ProductService{
	

	  private final ProductRepository repo;
	  
	  public ProductServiceImple(ProductRepository repo){
		  this.repo = repo; 
	  }

	  public Product create(Product p){ 
		  return repo.save(p); 
	  }
	  
	  public Optional<Product> findById(Long id){
		  return repo.findById(id); 
	  }
	  
	  public Page<Product> list(Pageable p){ 
		  return repo.findAll(p); 
	  }
	  
	  public Product update(Long id, Product updated){
	      Product existing = repo.findById(id).orElseThrow();
	      existing.setName(updated.getName());
	      existing.setDescription(updated.getDescription());
	      existing.setPrice(updated.getPrice());
	      return repo.save(existing);
	  }
	  public void delete(Long id){ 
		  repo.deleteById(id); 
	  }
}