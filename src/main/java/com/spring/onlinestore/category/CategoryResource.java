package com.spring.onlinestore.category;

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
public class CategoryResource {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	
	
	
	@GetMapping(path="/categs")
	public List<Category> retrieveAllCategories(){
		return categoryRepository.findAll();
	}
	
	@GetMapping(path="/categs/{id}")
	public Optional<Category> retrieveCategory(@PathVariable int id) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(optional.isEmpty()) throw new RuntimeException("id - " + id);
		return optional;
	}
	
	@PostMapping(path="/categs")
	public ResponseEntity<Object> createCategory(@RequestBody Category categ) {
		Category savedCateg = categoryRepository.save(categ);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCateg.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(path="/categs/{id}")
	public ResponseEntity<Object> editCategory(@PathVariable int id, @RequestBody Category categ) {
		Category savedCateg = categoryRepository.save(categ);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCateg.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/categs")
	public void deleteCategory(@PathVariable int id) {
		categoryRepository.deleteById(id);	
	}
	
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
	
}
