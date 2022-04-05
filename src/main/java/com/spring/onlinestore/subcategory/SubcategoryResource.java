package com.spring.onlinestore.subcategory;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.category.Category;
import com.spring.onlinestore.category.CategoryRepository;
import com.spring.onlinestore.exception.NotFoundException;

@RestController
public class SubcategoryResource {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@JsonView(SubcategoryView.ProductsExcluded.class)
	@GetMapping("/categs/{id}/sub")
	public List<Subcategory> retrieveAllSubcategories(@PathVariable int id){
		 Optional<Category> optional = categoryRepository.findById(id);
		 if(!optional.isPresent()) throw new NotFoundException("Category id - " + id);
		 Category cat = optional.get();
		 
		 return cat.getSubcats();
	}
	
	@PostMapping("/categs/{id}/sub")
	public ResponseEntity<Object> createSubcategory(@PathVariable int id, @Valid @RequestBody Subcategory sub) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Category id - " + id);
		
		Category cat = optional.get();
		sub.setCat(cat);
		
		subcategoryRepository.save(sub);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sub.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@JsonView(SubcategoryView.ProductsExcluded.class)
	@PutMapping("/categs/{id}/sub/{id2}")
	public Subcategory editSubcategory(@PathVariable int id, @PathVariable int id2, @Valid @RequestBody Subcategory sub) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(!optional.isPresent()) throw new NotFoundException("Category id - " + id);
		
		Category cat = optional.get();
		sub.setCat(cat);
		
		Optional<Subcategory> optional2 = subcategoryRepository.findById(id2);
		if(!optional2.isPresent()) throw new NotFoundException("Subcategory id - " + id2);
		Subcategory subcat = optional2.get();
		
		sub.setProds(subcat.getProds());
		sub.setId(id2);
		return subcategoryRepository.save(sub);
	}
	
	@DeleteMapping(path="/categs/*/sub/{id}")
	public ResponseEntity<String> deleteSubcategory(@PathVariable int id) {
		Optional<Subcategory> optional2 = subcategoryRepository.findById(id);
		if(!optional2.isPresent()) throw new NotFoundException("Subcategory id - " + id);
		
		subcategoryRepository.deleteById(id);
		return ResponseEntity.ok().body("Subcategory deleted");
	}
}
