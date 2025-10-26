package com.co.service.product.app.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co.service.product.app.model.Product;
import com.co.service.product.app.jsonapi.*;
import com.co.service.product.app.productservice.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService svc;
  public ProductController(ProductService svc){ this.svc = svc; }

  private Map<String,Object> toAttributes(Product p){
     Map<String,Object> a = new HashMap<>();
     a.put("name", p.getName());
     a.put("description", p.getDescription());
     a.put("price", p.getPrice());
     return a;
  }

 
  private ResponseEntity<Map<String,Object>> single(Product p){
     var data = new JsonApiData<>("products", p.getId().toString(), toAttributes(p));
     return ResponseEntity.ok(Map.of("data", data));
  }

  @PostMapping(produces = "application/json")
  public ResponseEntity<Map<String,Object>> create(@RequestBody Map<String,Object> payload) {
      Map<String,Object> attributes = (Map<String, Object>)payload.getOrDefault("attributes", payload);
      Product p = new Product();
      p.setName((String)attributes.get("name"));
      p.setDescription((String)attributes.get("description"));
      p.setPrice(new BigDecimal(attributes.get("price").toString()));
      Product saved = svc.create(p);
      return ResponseEntity.status(HttpStatus.CREATED).body(single(saved).getBody());
  }
  

  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<Map<String,Object>> get(@PathVariable Long id){
    Product p = svc.findById(id).orElseThrow();
    return single(p);
  }

  @PatchMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<Map<String,Object>> update(@PathVariable Long id, @RequestBody Map<String,Object> payload){
	Map<String,Object> attributes = (Map<String, Object>)payload.getOrDefault("attributes", payload);
    Product u = new Product();
    u.setName((String)attributes.get("name"));
    u.setDescription((String)attributes.get("description"));
    if(attributes.get("price")!=null) u.setPrice(new BigDecimal(attributes.get("price").toString()));
    Product updated = svc.update(id, u);
    return single(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    svc.delete(id);
    return ResponseEntity.noContent().build();
  }

  @SuppressWarnings("unchecked")
  @GetMapping(produces = "application/json")
  public ResponseEntity<Map<String,Object>> list(@RequestParam(defaultValue="0") int page,
                                              @RequestParam(defaultValue="10") int size){
    Page<Product> p = svc.list(PageRequest.of(page,size));
	List<Object> data = p.stream()
      .map(prod -> new JsonApiData("products", prod.getId().toString(), toAttributes(prod)))
      .collect(Collectors.toList());
    Map<String,Object> meta = Map.of("totalElements", p.getTotalElements(), "totalPages", p.getTotalPages());
    return ResponseEntity.ok(Map.of("data", data, "meta", meta));
  }
}