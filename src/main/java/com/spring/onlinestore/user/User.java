package com.spring.onlinestore.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import com.spring.onlinestore.role.Role;
import com.spring.onlinestore.shoppinglist.ShoppingList;

@Entity
@Component
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(unique = true)
	private String username;
	private String password;
    private boolean enabled;
 
    @ManyToOne(fetch=FetchType.EAGER)
    private Role role;
    
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ShoppingList> shoppinglists;
	
	public User() {
		super();
	}

	public User(Integer id, String username, String password, boolean enabled, Role role,
			List<ShoppingList> shoppinglists) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.role = role;
		this.shoppinglists = shoppinglists;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<ShoppingList> getShoppinglists() {
		return shoppinglists;
	}

	public void setShoppinglists(List<ShoppingList> shoppinglists) {
		this.shoppinglists = shoppinglists;
	}
}
