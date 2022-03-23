package com.spring.onlinestore.category;

import java.net.URI;
import java.util.List;

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


@RestController
public class CategoryResource {
	
	@Autowired
	private CategoryRepository categoryRepository;

		
	@JsonView(CategoryView.SubcatsExcluded.class)
	@GetMapping(path="/categs")
	public List<Category> retrieveAllCategories(){
		return categoryRepository.findAll();
	}

	/* USE ONLY FOR TESTING
	
	@GetMapping(path="/categs/{id}")
	public Optional<Category> retrieveCategory(@PathVariable int id) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(optional.isEmpty()) throw new RuntimeException("id - " + id);
		return optional;
	}
	
	*/
	
	@PostMapping(path="/categs")
	public ResponseEntity<Object> createCategory(@RequestBody Category categ) {
		Category savedCateg = categoryRepository.save(categ);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCateg.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(path="/categs/{id}")
	public Category editCategory(@PathVariable int id, @RequestBody Category categ) {
		categ.setId(id);
		return categoryRepository.save(categ);
	}
	
	@DeleteMapping(path="/categs/{id}")
	public void deleteCategory(@PathVariable int id) {
		categoryRepository.deleteById(id);	
	}
	
}
