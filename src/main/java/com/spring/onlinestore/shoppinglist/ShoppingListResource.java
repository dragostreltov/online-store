package com.spring.onlinestore.shoppinglist;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

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
import com.spring.onlinestore.coupon.Coupon;
import com.spring.onlinestore.coupon.CouponRepository;
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
	
	@Autowired
	private CouponRepository couponRepository;
	
	double totalCost = 0;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
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
	
	@Transactional
	@DeleteMapping("/user/lists/{id}")
	public ResponseEntity<String> deleteShoppinglist(@PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only delete your list(s)!");

		shoppingList.getProducts().stream().forEach(e -> {
			e.removeShoppinglist(shoppingList);
		});
		shoppinglistRepository.delete(shoppingList);
		
		return ResponseEntity.ok().body("Shopping list deleted");
	}
	
	@Transactional
	@GetMapping("/user/lists/{id}")
	public Set<Product> getShoppinglistProducts(@PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		ShoppingList shoppingList = shoppinglistRepository.findById(id).get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only check your list(s)!");

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
		shoppingList.addProduct(product);
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
		shoppingList.removeProduct(product);
		shoppinglistRepository.save(shoppingList);
		
		return ResponseEntity.ok().body("Product deleted from list");
	}
	
	@GetMapping("/user/lists/{id}/checkout")
	public ResponseEntity<String> shoppinglistCheckout(@PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only checkout your list(s)!");
		
		totalCost = 0;
		totalCost = shoppingList.getProducts().stream().mapToDouble(e -> e.getPrice()).sum();
		
		return ResponseEntity.ok().body("Checkout for " + shoppingList.getName() + "\n" 
										+ "Total cost: " + df.format(totalCost) + "\n" + "\n"
										+ "Do you want to apply a discount coupon?" + "\n"
										+ "POST http://localhost:8080/user/lists/" + id + "/checkout with coupon code");
	}
	
	@PostMapping("/user/lists/{id}/checkout")
	public ResponseEntity<String> applyDiscountCoupon(@PathVariable int id, @RequestBody Coupon coupon) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Optional<ShoppingList> optional2 = shoppinglistRepository.findById(id);
		ShoppingList shoppingList = optional2.get();
		String name = shoppingList.getUser().getUsername();
		if(!Objects.equals(currentPrincipalName, name)) throw new IllegalOperation("You can only checkout your list(s)!");
		
		totalCost = 0;
		totalCost = shoppingList.getProducts().stream().mapToDouble(e -> e.getPrice()).sum();
		
		Optional<Coupon> optional = couponRepository.findByCode(coupon.getCode());
		Coupon c = optional.get();
		
		if(coupon.getPercentage() < 1.0) totalCost *= c.getPercentage();
			else totalCost -= c.getPercentage();
		
		return ResponseEntity.ok().body("Checkout for " + shoppingList.getName() + "\n" 
				+ "Total cost: " + df.format(totalCost) + "\n" + "\n"
				+ "Discount coupon applied!");
	}
}
