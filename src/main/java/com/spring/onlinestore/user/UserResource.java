package com.spring.onlinestore.user;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.onlinestore.exception.IllegalOperation;
import com.spring.onlinestore.role.Role;
import com.spring.onlinestore.role.RoleRepository;

@RestController
public class UserResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostMapping("/user")
	public ResponseEntity<Object> register(@Valid @RequestBody User user) {
		Optional<Role> role = roleRepository.findByName("ROLE_USER");
		user.setRole(role.get());
		user.setEnabled(true);
		userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}").buildAndExpand(user.getUsername()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/user/{name}")
	public ResponseEntity<String> editUser(@PathVariable String name,@Valid @RequestBody User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only edit your account!");
		
		Optional<Role> role = roleRepository.findByName("ROLE_USER");
		Optional<User> optional = userRepository.findByUsername(name);
		User dbuser = optional.get();

		user.setRole(role.get());
		user.setEnabled(true);
		user.setId(dbuser.getId());
		userRepository.save(user);
		return ResponseEntity.ok().body("User edited");
	}
	
	@DeleteMapping("/user/{name}")
	public ResponseEntity<String> deleteUser(@PathVariable String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only delete your account!");
		
		Optional<User> optional = userRepository.findByUsername(name);
		User dbuser = optional.get();
		userRepository.deleteById(dbuser.getId());
		return ResponseEntity.ok().body("User deleted");
	}

}
