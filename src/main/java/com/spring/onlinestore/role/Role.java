package com.spring.onlinestore.role;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import com.spring.onlinestore.user.User;

@Component
@Entity
public class Role {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
    
    private String name;
    
    @OneToMany(mappedBy = "role")
    private List<User> users;
    
    public Role() {
    	super();
    }

	public Role(Integer id, String name, List<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.users = users;
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
