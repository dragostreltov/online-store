package com.spring.onlinestore.subcategory;

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

import com.spring.onlinestore.category.Category;
import com.spring.onlinestore.category.CategoryRepository;

@RestController
public class SubcategoryResource {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@GetMapping(path="/categs/{id}/sub")
	public List<Subcategory> retrieveSubcategories(@PathVariable int id){
		 Optional<Category> cat = categoryRepository.findById(id);
		 if(!cat.isPresent()) {
			 throw new RuntimeException("id - " + id);
		 }
		 
		 return cat.get().getSubcats();
	}
	
	@PostMapping(path="/categs/{id}/sub")
	public ResponseEntity<Object> createSubcategory(@PathVariable int id, @RequestBody Subcategory sub) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(!optional.isPresent()) throw new RuntimeException("id - " + id);
		
		Category cat = optional.get();
		sub.setCat(cat);
		
		subcategoryRepository.save(sub);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sub.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(path="/categs/{id}/sub/{id2}")
	public Subcategory editSubcategory(@PathVariable int id, @PathVariable int id2, @RequestBody Subcategory sub) {
		Optional<Category> optional = categoryRepository.findById(id);
		 if(!optional.isPresent()) {
			 throw new RuntimeException("id - " + id);
		 }
		Category cat = optional.get();
		sub.setCat(cat);
		sub.setId(id2);
		return subcategoryRepository.save(sub);
	}
	
	@DeleteMapping(path="/categs/*/sub/{id}")
	public void deleteSubcategory(@PathVariable int id) {
		subcategoryRepository.deleteById(id);
	}
}
