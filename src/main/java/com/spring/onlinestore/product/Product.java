package com.spring.onlinestore.product;

import javax.persistence.Column;
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
	
	@Column(name = "name")
	private String a_name;
	
	@Column(name = "description")
	private String b_description;
	
	@Column(name = "price")
	private Double c_price;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore	
	private Subcategory subcat;
	
	public Product() {
		super();
	}

	public Product(Integer id, String name, String description, Double price) {
		super();
		this.id = id;
		this.a_name = name;
		this.b_description = description;
		this.c_price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return a_name;
	}

	public void setName(String name) {
		this.a_name = name;
	}

	public String getDescription() {
		return b_description;
	}

	public void setDescription(String description) {
		this.b_description = description;
	}

	public Double getPrice() {
		return c_price;
	}

	public void setPrice(Double price) {
		this.c_price = price;
	}
	
	public Subcategory getSubcat() {
		return subcat;
	}

	public void setSubcat(Subcategory subcat) {
		this.subcat = subcat;
	}

	@Override
	public String toString() {
		return String.format("Product [id=%s, name=%s, description=%s, price=%s]", id, a_name, b_description, c_price);
	}
	
}
