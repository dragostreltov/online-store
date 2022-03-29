package com.spring.onlinestore.category;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.subcategory.Subcategory;

@Component
@Entity
public class Category {
	
	@Id
	@GeneratedValue
	@JsonView(CategoryView.SubcatsExcluded.class)
	private Integer id;
	
	@JsonView(CategoryView.SubcatsExcluded.class)
	@Size(min = 2, message = "Name should have at least 2 characters")
	private String name;
	
	@JsonView(CategoryView.SubcatsIncluded.class)
	@OneToMany(mappedBy = "cat", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Subcategory> subcats;

	public Category() {
		super();
	}

	public Category(Integer id, String name, List<Subcategory> subcats) {
		super();
		this.id = id;
		this.name = name;
		this.subcats = subcats;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Subcategory> getSubcats() {
		return subcats;
	}

	public void setCats(List<Subcategory> subcats) {
		this.subcats = subcats;
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, cats=%s]", id, name, subcats);
	}
	
}
