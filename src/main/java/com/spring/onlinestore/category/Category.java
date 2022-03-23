package com.spring.onlinestore.category;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	private String name;
	
	@OneToMany(mappedBy = "cat")
	private List<Subcategory> subcats;

	public Category() {
		super();
	}

	public Category(Integer id, String name, List<Subcategory> cats) {
		super();
		this.id = id;
		this.name = name;
		this.subcats = cats;
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

	public void setCats(List<Subcategory> cats) {
		this.subcats = cats;
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, cats=%s]", id, name, subcats);
	}
	
}
