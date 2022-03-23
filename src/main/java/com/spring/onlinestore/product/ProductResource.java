package com.spring.onlinestore.product;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.onlinestore.subcategory.Subcategory;
import com.spring.onlinestore.subcategory.SubcategoryRepository;

@RestController
public class ProductResource {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	
	
	@GetMapping("/categs/sub/{id}/products")
	public List<Product> retrieveAllProducts(@PathVariable int id){
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		 if(!optional.isPresent()) {
			 throw new RuntimeException("id - " + id);
		 }
		 return optional.get().getProds();
		 
		 // HATEOAS - check product x
	}
	
	@GetMapping("/categs/sub/*/products/{id}")
	public Optional<Product> retrieveProduct(@PathVariable int id) {
		return productRepository.findById(id);
		
		// HATEOAS - add product to shopping list
	}
	
	@PostMapping("/categs/sub/{id}/products")
	public ResponseEntity<Object> createProduct(@PathVariable int id, @RequestBody Product prod) {
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		if(!optional.isPresent()) throw new RuntimeException("id - " + id);
		
		Subcategory sub = optional.get();
		prod.setSubcat(sub);
		
		productRepository.save(prod);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prod.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/categs/sub/{id}/products/{id2}")
	public Product editProduct(@PathVariable int id, @PathVariable int id2, @RequestBody Product prod) {
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		 if(!optional.isPresent()) {
			 throw new RuntimeException("id - " + id);
		 }
		Subcategory sub = optional.get();
		prod.setSubcat(sub);
		prod.setId(id2);
		return productRepository.save(prod);
	}
	
	@DeleteMapping("/categs/sub/*/products/{id}")
	public void deleteProduct(@PathVariable int id) {
		productRepository.deleteById(id);
	}
	
}
