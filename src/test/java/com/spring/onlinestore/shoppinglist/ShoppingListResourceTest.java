package com.spring.onlinestore.shoppinglist;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
//import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.coupon.Coupon;
import com.spring.onlinestore.coupon.CouponRepository;
import com.spring.onlinestore.product.Product;
import com.spring.onlinestore.product.ProductRepository;
import com.spring.onlinestore.user.User;
import com.spring.onlinestore.user.UserRepository;

@AutoConfigureRestDocs // defaults to target/generated-snippets
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class ShoppingListResourceTest {
	
	@Mock
	Set<Product> products = new HashSet<>();
	
	@Mock
	ShoppingList list = new ShoppingList();
	
	@Mock
	User currentUser = new User();
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ShoppingListRepository shoppinglistRepository;
	
	@Mock
	private CouponRepository couponRepository;
	
	@InjectMocks
	ShoppingListResource shoppinglistResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(shoppinglistResource)
				.apply(documentationConfiguration(restDocumentation))
				 .alwaysDo(document("{method-name}", 
						    preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.build();
	}
	
	@Test
	void retrieveListsForUserTest() throws Exception {
		
		ShoppingList list = new ShoppingList();
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		shoppinglistRepository.saveAndFlush(list);
		
		List<ShoppingList> lists = new ArrayList<>();		
		lists = List.of(list);
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(Optional.of(currentUser)).when(userRepository).findByUsername("user");
		doReturn(lists).when(currentUser).getShoppinglists();
		
		this.mockMvc.perform(get("/user/lists"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].list_id").value(10))
				.andExpect(jsonPath("$[0].name").value("wishlist"));
	}
	
	@Test
	void createShoppinglistTest() throws Exception {
		
		ShoppingList list = new ShoppingList();
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(Optional.of(currentUser)).when(userRepository).findByUsername("user");
     	doReturn(list).when(shoppinglistRepository).save(any());
     	
        this.mockMvc.perform(post("/user/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost:8080/user/lists/10"))
				.andExpect(redirectedUrl("http://localhost:8080/user/lists/10"));
	}
	
	@Test
	void editShoppinglistTest() throws Exception {
		
		currentUser.setId(1);
		ShoppingList list = new ShoppingList();
		ShoppingList oldlist = new ShoppingList();
		oldlist.setUser(currentUser);
		oldlist.setList_id(10);
		oldlist.setName("whishlist");
		shoppinglistRepository.saveAndFlush(oldlist);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist_modified");
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(Optional.of(currentUser)).when(userRepository).findByUsername("user");
     	doReturn(Optional.of(oldlist)).when(shoppinglistRepository).findById(10);
     	doReturn(list).when(shoppinglistRepository).save(any());
     	
        this.mockMvc.perform(put("/user/lists/{id}", 10)   	
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(json)
        		.characterEncoding("utf-8")
        		.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list_id").value(10))
				.andExpect(jsonPath("$.name").value("wishlist_modified"));
	}
	
	@Test
	void deleteShoppinglistTest() throws Exception {
	
		currentUser.setId(1);
		
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
		doNothing().when(shoppinglistRepository).delete(list);
		
		this.mockMvc.perform(delete("/user/lists/{id}", 10)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	void getShoppinglistProductsTest() throws Exception {
		
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		Set<Product> products = new HashSet<>();
		Product product = new Product();
		Product product2 = new Product();
		product.setId(100);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		products.add(product);
		product2.setId(101);
		product2.setName("apple");
		product2.setDescription("smartphone2");
		product2.setPrice(1100.0);
		products.add(product2);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
     	doReturn(products).when(list).getProducts();
     	
		this.mockMvc.perform(get("/user/lists/{id}", 10))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(100))
				.andExpect(jsonPath("$[0].name").value("samsung"))
				.andExpect(jsonPath("$[0].description").value("smartphone"))
				.andExpect(jsonPath("$[0].price").value(1000.0))
				.andExpect(jsonPath("$[1].id").value(101))
				.andExpect(jsonPath("$[1].name").value("apple"))
				.andExpect(jsonPath("$[1].description").value("smartphone2"))
				.andExpect(jsonPath("$[1].price").value(1100.0));
	}
	
	@Test
	void addProductToShoppinglistTest() throws Exception {
		
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		Set<Product> products2 = new HashSet<>();
		Product product = new Product();
		Product product2 = new Product();
		product.setId(100);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		products.add(product);
		
		product2.setId(101);
		product2.setName("apple");
		product2.setDescription("smartphone2");
		product2.setPrice(1100.0);

		products2.add(product);
		products2.add(product2);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(product2);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
     	doReturn(Optional.of(product)).when(productRepository).findById(101);
     	doReturn(list).when(shoppinglistRepository).save(any());
     	doReturn(products2).when(list).getProducts();
     	
        this.mockMvc.perform(post("/user/lists/{id}/{id2}", 10, 101)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isCreated())
        		.andExpect(content().string("Product added to list"));
        
		Assertions.assertEquals(products2, list.getProducts());
	}
	
	@Test
	void deleteProductFromShoppinglistTest() throws Exception {
		
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		Product product = new Product();
		Product product2 = new Product();
		product.setId(100);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		product2.setId(101);
		product2.setName("apple");
		product2.setDescription("smartphone2");
		product2.setPrice(1100.0);
		products.add(product);
		products.add(product2);
		
		Set<Product> products2 = new HashSet<>();
		products2.add(product2);
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
     	doReturn(Optional.of(product)).when(productRepository).findById(100);
     	doNothing().when(list).removeProduct(product);
     	doReturn(list).when(shoppinglistRepository).save(any());
     	doReturn(products2).when(list).getProducts();
     	
        this.mockMvc.perform(delete("/user/lists/{id}/{id2}", 10, 100)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(content().string("Product deleted from list"));
        
        Assertions.assertEquals(products2, list.getProducts());
	}
	
	@Test
	void shoppinglistCheckoutTest() throws Exception {
		
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		Product product = new Product();
		Product product2 = new Product();
		product.setId(100);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		product2.setId(101);
		product2.setName("apple");
		product2.setDescription("smartphone2");
		product2.setPrice(1100.0);
		products.add(product);
		products.add(product2);
		Stream<Product> stream = Stream.of(product, product2);
//		Coupon coupon = new Coupon(1, "TOTAL15", 0.85);
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
     	doReturn("wishlist").when(list).getName();
     	doReturn(products).when(list).getProducts();
     	doReturn(stream).when(products).stream();
     	
		this.mockMvc.perform(get("/user/lists/{id}/checkout", 10))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string("Checkout for wishlist" + "\n" 
					+ "Total cost: 2100,00" + "\n" + "\n"
					+ "Do you want to apply a discount coupon?" + "\n"
					+ "POST http://localhost:8080/user/lists/10/checkout with coupon code"));
	}
	
	@Test
	void applyDiscountCouponTest() throws Exception {
		
		currentUser.setId(1);
		list.setUser(currentUser);
		list.setList_id(10);
		list.setName("wishlist");
		Product product = new Product();
		Product product2 = new Product();
		product.setId(100);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		product2.setId(101);
		product2.setName("apple");
		product2.setDescription("smartphone2");
		product2.setPrice(1100.0);
		products.add(product);
		products.add(product2);
		Stream<Product> stream = Stream.of(product, product2);
		Coupon coupon = new Coupon(1, "TOTAL15", 0.85);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(coupon);
		
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn("user").when(authentication).getName();
     	doReturn(currentUser).when(list).getUser();
     	doReturn("user").when(currentUser).getUsername();
     	doReturn(Optional.of(list)).when(shoppinglistRepository).findById(10);
     	doReturn("wishlist").when(list).getName();
     	doReturn(products).when(list).getProducts();
     	doReturn(stream).when(products).stream();
     	doReturn(Optional.of(coupon)).when(couponRepository).findByCode("TOTAL15");
     	
		this.mockMvc.perform(post("/user/lists/{id}/checkout", 10)
		.contentType(MediaType.APPLICATION_JSON)
		.content(json)
		.characterEncoding("utf-8")
		.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string("Checkout for wishlist" + "\n" 
					+ "Total cost: 1785,00" + "\n" + "\n"
					+ "Discount coupon applied!"));
	}

}
