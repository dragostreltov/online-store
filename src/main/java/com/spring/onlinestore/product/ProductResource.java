package com.spring.onlinestore.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.exception.NotFoundException;
import com.spring.onlinestore.subcategory.Subcategory;
import com.spring.onlinestore.subcategory.SubcategoryRepository;

@RestController
public class ProductResource {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
//	private static final Logger log = LoggerFactory.getLogger(ProductResource.class);
	
	@JsonView(ProductView.DescriptionExcluded.class)
	@GetMapping("/categs/*/sub/{id}/products")
	public CollectionModel<Product> retrieveAllProducts(@PathVariable int id){
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		 if(!optional.isPresent()) throw new NotFoundException("Subcategory id - " + id);
		
		 Subcategory sub = optional.get();
		 List<Product> list = sub.getProds();
		 
		 list.stream().forEach(e -> {
			 e.add(linkTo(methodOn(this.getClass()).retrieveProduct(e.getId())).withSelfRel());
		 });
		 
		 return CollectionModel.of(list);
	}
	
	@GetMapping("/categs/*/sub/*/products/{id}")
	public EntityModel<Product> retrieveProduct(@PathVariable int id) {
		Optional<Product> optional = productRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Product id - " + id);

		Subcategory subcat = optional.get().getSubcat();
		EntityModel<Product> res = EntityModel.of(optional.get());
		
		// HATEOAS - check similar products
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllProducts(subcat.getId()));
		res.add(linkTo.withRel("check-similar-products"));
		
		// HATEOAS - add product to shopping list
		// TO-DO
		
		return res;
		
	}
	
	@PostMapping("/categs/*/sub/{id}/products")
	public ResponseEntity<Object> createProduct(@PathVariable int id, @Valid @RequestBody Product prod) {
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Subcategory id - " + id);
		
		Subcategory sub = optional.get();
		prod.setSubcat(sub);
		
		productRepository.save(prod);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prod.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/categs/*/sub/{id}/products/{id2}")
	public Product editProduct(@PathVariable int id, @PathVariable int id2, @Valid @RequestBody Product prod) {
		Optional<Subcategory> optional = subcategoryRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Subcategory id - " + id);
		
		Subcategory sub = optional.get();
		prod.setSubcat(sub);
		
		Optional<Product> optional2 = productRepository.findById(id2);
		if(!optional2.isPresent()) throw new NotFoundException("Product id - " + id2);
		Product product = optional2.get();
		
		prod.setId(id2);
		prod.setShoppinglists(product.getShoppinglists());
		
		return productRepository.save(prod);
	}
	
	@DeleteMapping("/categs/*/sub/*/products/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id) {
		Optional<Product> optional = productRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Product id - " + id);
		Product prod = optional.get();
		productRepository.delete(prod);
		return ResponseEntity.ok().body("Product deleted");
	}
	
}
