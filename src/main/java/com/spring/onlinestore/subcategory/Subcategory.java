package com.spring.onlinestore.subcategory;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.category.Category;
import com.spring.onlinestore.product.Product;

@Component
@Entity
public class Subcategory {
	@Id
	@GeneratedValue
	@JsonView(SubcategoryView.ProductsExcluded.class)
	private Integer id;
	
	@JsonView(SubcategoryView.ProductsExcluded.class)
	@Size(min = 2, message = "Name should have at least 2 characters")
	private String name;
	
	@JsonView(SubcategoryView.ProductsIncluded.class)
	@OneToMany(mappedBy = "subcat", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Product> prods;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Category cat;
	
	
	public Subcategory() {
		super();
	}

	public Subcategory(Integer id, String name, List<Product> prods) {
		super();
		this.id = id;
		this.name = name;
		this.prods = prods;
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

	public List<Product> getProds() {
		return prods;
	}

	public void setProds(List<Product> prods) {
		this.prods = prods;
	}

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

	@Override
	public String toString() {
		return String.format("Subcategory [id=%s, name=%s, prods=%s]", id, name, prods);
	}
	
}
