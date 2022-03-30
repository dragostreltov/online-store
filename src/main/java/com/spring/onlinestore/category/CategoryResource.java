package com.spring.onlinestore.category;

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
import com.spring.onlinestore.exception.NotFoundException;


@RestController
public class CategoryResource {
	
	@Autowired
	private CategoryRepository categoryRepository;

		
	@JsonView(CategoryView.SubcatsExcluded.class)
	@GetMapping(path="/categs")
	public List<Category> retrieveAllCategories(){
		return categoryRepository.findAll();
	}
	
	@PostMapping(path="/categs")
	public ResponseEntity<Object> createCategory(@Valid @RequestBody Category categ) {
		Category savedCateg = categoryRepository.save(categ);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCateg.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@JsonView(CategoryView.SubcatsExcluded.class)
	@PutMapping(path="/categs/{id}")
	public Category editCategory(@PathVariable int id, @Valid @RequestBody Category categ) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(optional.isEmpty()) throw new NotFoundException("Category id - " + id);
		Category cat = optional.get();
		categ.setCats(cat.getSubcats());
		categ.setId(id);
		return categoryRepository.save(categ);
	}
	
	@DeleteMapping(path="/categs/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable int id) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(optional.isEmpty()) throw new NotFoundException("Category id - " + id);
		categoryRepository.deleteById(id);
		return ResponseEntity.ok().body("Category deleted");
	}
	
}
