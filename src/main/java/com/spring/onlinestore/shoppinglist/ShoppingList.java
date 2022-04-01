package com.spring.onlinestore.shoppinglist;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.product.Product;
import com.spring.onlinestore.user.User;

@Entity
@Component
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(ShoppingListView.ProductsExcluded.class)
	private Integer list_id;
    
	@JsonView(ShoppingListView.ProductsExcluded.class)
    private String name;
    
	@JsonView(ShoppingListView.ProductsIncluded.class)
	@ManyToMany(mappedBy = "shoppinglists", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Product> products = new HashSet<>();
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private User user;
    
	public ShoppingList() {
		super();
	}

	public ShoppingList(Integer list_id, String name, Set<Product> products, User user) {
		super();
		this.list_id = list_id;
		this.name = name;
		this.products = products;
		this.user = user;
	}
	
	public Integer getList_id() {
		return list_id;
	}

	public void setList_id(Integer list_id) {
		this.list_id = list_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
