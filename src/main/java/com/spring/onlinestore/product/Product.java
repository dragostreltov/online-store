package com.spring.onlinestore.product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.shoppinglist.ShoppingList;
import com.spring.onlinestore.subcategory.Subcategory;

@Entity
public class Product extends RepresentationModel<Product>{
	@Id
	@GeneratedValue
	@JsonView(ProductView.DescriptionExcluded.class)
	private Integer id;
	
	@JsonView(ProductView.DescriptionExcluded.class)
	@Size(min = 2, message = "Name should have at least 2 characters")
	private String name;
	
	@JsonView(ProductView.DescriptionView.class)
	@Size(min = 2, message = "Description should have at least 2 characters")
	private String description;
	
	@JsonView(ProductView.DescriptionExcluded.class)
	private Double price;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore	
	private Subcategory subcat;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "Shopping_Product", 
            joinColumns = { @JoinColumn(name = "id") }, 
            inverseJoinColumns = { @JoinColumn(name = "list_id") })
	@JsonIgnore
    private Set<ShoppingList> shoppinglists = new HashSet<>();
	
	public Product() {
		super();
	}

	public Product(Integer id, @Size(min = 2, message = "Name should have at least 2 characters") String name,
			@Size(min = 2, message = "Description should have at least 2 characters") String description, Double price,
			Subcategory subcat, Set<ShoppingList> shoppinglists) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.subcat = subcat;
		this.shoppinglists = shoppinglists;
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

	public Set<ShoppingList> getShoppinglists() {
		return shoppinglists;
	}

	public void setShoppinglists(Set<ShoppingList> shoppinglists) {
		this.shoppinglists = shoppinglists;
	}

    public void addShoppinglist(ShoppingList list) {
    	this.shoppinglists.add(list);
    	list.getProducts().add(this);
    }
	
    public void removeShoppinglist(ShoppingList list) {
    	this.shoppinglists.remove(list);
    	list.getProducts().remove(this);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(description, id, name, price, shoppinglists, subcat);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Product))
			return false;
		Product other = (Product) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(price, other.price)
				&& Objects.equals(shoppinglists, other.shoppinglists) && Objects.equals(subcat, other.subcat);
	}

	@Override
	public String toString() {
		return String.format("Product [id=%s, name=%s, description=%s, price=%s]", id, name, description, price);
	}
	
}
