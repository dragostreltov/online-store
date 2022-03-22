package com.spring.onlinestore.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// EXCLUDED FROM BUILD - ONLY ADDED FOR TESTING OF PRODUCT

@RestController
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	@GetMapping(path="/products")
	public List<Product> retrieveAllProducts(){
		return service.listAllProducts();
	}
	
	@GetMapping(path="/products/{id}")
	public Product retrieveOneProduct(@PathVariable int id) {
		Product product = service.findOne(id);
		if(product.getId() == null) throw new RuntimeException("id - " + id);
		return product;
	}
	
	@PostMapping(path="/products")
	public Product createProduct(@RequestBody Product product) {
		return service.save(product);
	}
	
	@DeleteMapping(path="/products/{id}")
	public void deleteUser(@PathVariable int id) {
		Product product = service.deleteById(id);
		if(product.getId() == null) throw new RuntimeException("id - " + id);
	}
}
