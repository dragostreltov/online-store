package com.spring.onlinestore.user;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.onlinestore.role.Role;
import com.spring.onlinestore.role.RoleRepository;

@RestController
public class UserResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostMapping("/user")
	public ResponseEntity<Object> register(@RequestBody User user) {
		Optional<Role> role = roleRepository.findByName("ROLE_USER");
		user.setRole(role.get());
		user.setEnabled(true);
		userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}").buildAndExpand(user.getUsername()).toUri();
		return ResponseEntity.created(location).build();
	}

}
