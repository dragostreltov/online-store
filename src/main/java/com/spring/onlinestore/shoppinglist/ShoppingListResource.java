package com.spring.onlinestore.shoppinglist;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;
import com.spring.onlinestore.exception.IllegalOperation;
import com.spring.onlinestore.product.Product;
import com.spring.onlinestore.product.ProductRepository;
import com.spring.onlinestore.user.User;
import com.spring.onlinestore.user.UserRepository;

@RestController
public class ShoppingListResource {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShoppingListRepository shoppinglistRepository;
	
//	private static final Logger log = LoggerFactory.getLogger(ShoppingListResource.class);
	
	@JsonView(ShoppingListView.ProductsExcluded.class)
	@GetMapping("/user/lists")
	public List<ShoppingList> retrieveListsForUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Optional<User> optional = userRepository.findByUsername(currentPrincipalName);
		User user = optional.get();
		return user.getShoppinglists();
	}
	
	@PostMapping("/user/lists")
	public ResponseEntity<Object> createShoppinglist(@RequestBody ShoppingList list) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Optional<User> optional = userRepository.findByUsername(currentPrincipalName);
		User user = optional.get();
		
		list.setUser(user);
		shoppinglistRepository.save(list);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(list.getList_id()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@JsonView(ShoppingListView.ProductsExcluded.class)	
	@PutMapping("/user/lists/{id}")
	public ShoppingList editShoppinglist(@PathVariable int id, @RequestBody ShoppingList list) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Optional<User> optional = userRepository.findByUsername(currentPrincipalName);
		User user = optional.get();
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		
		list.setUser(user);
		list.setProducts(shoppingList.getProducts());
		list.setList_id(id);
		
		return shoppinglistRepository.save(list);
	}
	
	@DeleteMapping("/user/lists/{id}")
	public ResponseEntity<String> deleteShoppinglist(@PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only delete your list(s)!");
		
		shoppinglistRepository.delete(shoppingList);	
		
		return ResponseEntity.ok().body("Shopping list deleted");
	}
	
	@GetMapping("/user/lists/{id}")
	public Set<Product> getShoppinglistProducts(@PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		ShoppingList shoppingList = shoppinglistRepository.findById(id).get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only check your list(s)!");

//		All lists are shown for a product
//		Product p = productRepository.findById(10111).get();
//		Set<ShoppingList> set = p.getShoppinglists();
//		set.stream().forEach(e -> log.info(e.toString()));
		
//		Only first product is shown for a list
		return shoppingList.getProducts();
	}
	
	@PostMapping("/user/lists/{id}/{id2}")
	public ResponseEntity<String> addProductToShoppinglist(@PathVariable int id, @PathVariable int id2) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only edit your list(s)!");
		
		Optional<Product> optionalp = productRepository.findById(id2);
		Product product = optionalp.get();
		shoppingList.getProducts().add(product);
		shoppinglistRepository.save(shoppingList);
		
		return ResponseEntity.created(null).body("Product added to list");
	}
	
	@DeleteMapping("/user/lists/{id}/{id2}")
	public ResponseEntity<String> deleteProductFromShoppinglist(@PathVariable int id, @PathVariable int id2) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only edit your list(s)!");
		
		Optional<Product> optionalp = productRepository.findById(id2);
		Product product = optionalp.get();
		shoppingList.getProducts().remove(product);
		shoppinglistRepository.save(shoppingList);
		
		return ResponseEntity.ok().body("Product deleted from list");
	}
	
}
