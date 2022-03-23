package com.spring.onlinestore.product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.onlinestore.subcategory.Subcategory;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private Double price;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore	
	private Subcategory subcat;
	
	public Product() {
		super();
	}

	public Product(Integer id, String name, String description, Double price) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Subcategory getSubcat() {
		return subcat;
	}

	public void setSubcat(Subcategory subcat) {
		this.subcat = subcat;
	}

	@Override
	public String toString() {
		return String.format("Product [id=%s, name=%s, description=%s, price=%s]", id, name, description, price);
	}
	
}
