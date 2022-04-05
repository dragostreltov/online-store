package com.spring.onlinestore.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.shoppinglist.ShoppingList;
import com.spring.onlinestore.subcategory.Subcategory;
import com.spring.onlinestore.subcategory.SubcategoryRepository;

@AutoConfigureRestDocs // defaults to target/generated-snippets
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class ProductResourceTest {
	
	@Mock
	Subcategory sub;
	
	@Mock
	SubcategoryRepository subcategoryRepository;
	
	@Mock
	ProductRepository productRepository;
	
	@InjectMocks
	ProductResource productResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(productResource)
				.apply(documentationConfiguration(restDocumentation))
				 .alwaysDo(document("{method-name}", 
						    preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.build();
	}
	
	@Test
	final void retrieveAllProductsTest() throws Exception {
		sub.setId(1);
		Set<ShoppingList> shoppinglists = new HashSet<>();
		
		Product product = new Product(10, "samsung", "smartphone", 1000.0, sub, shoppinglists);
		Product product2 = new Product(11, "apple", "smartphone 2", 900.0, sub, shoppinglists);
		
		productRepository.saveAndFlush(product);
		productRepository.saveAndFlush(product2);
		
		List<Product> list = List.of(product, product2);
//		 list.stream().forEach(e -> {
//			 e.add(linkTo(methodOn(ProductResource.class).retrieveProduct(e.getId())).withSelfRel());
//		 });
		 
		doReturn(Optional.of(sub)).when(subcategoryRepository).findById(1);
		doReturn(list).when(sub).getProds();
		
		CollectionModel<Product> listWithLinks = CollectionModel.of(list);
//		CollectionModel<Product> products = productResource.retrieveAllProducts(1);
		Assertions.assertEquals(listWithLinks, productResource.retrieveAllProducts(1));
        
		this.mockMvc.perform(get("/categs/*/sub/{id}/products", 1))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	final void retrieveProductTest() throws Exception {
		
		Set<ShoppingList> shoppinglists = new HashSet<>();
		// For HATEOAS link
		Subcategory subcat = new Subcategory();
		subcat.setId(2);
		/////////////////////////
		Product product = new Product(10, "samsung", "smartphone", 1000.0, subcat, shoppinglists);
		
		productRepository.saveAndFlush(product);

		doReturn(Optional.of(product)).when(productRepository).findById(10);

		this.mockMvc.perform(get("/categs/*/sub/*/products/{id}", 10))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("samsung"))
			.andExpect(jsonPath("$.description").value("smartphone"))
			.andExpect(jsonPath("$.price").value(1000.0))
			.andExpect(jsonPath("$.links[0].href").value("http://localhost:8080/categs/*/sub/2/products"))
			.andExpect(jsonPath("$.links[1].href").value("http://localhost:8080/user/lists/0/10"))
			.andExpect(jsonPath("$.links[2].href").value("http://localhost:8080/user/lists"));
	}
	
	@Test
	final void createProductTest() throws Exception {
	
		Set<ShoppingList> shoppinglists = new HashSet<>();
		sub.setId(1);
		Product product = new Product(10, "samsung", "smartphone", 1000.0, sub, shoppinglists);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(product);
        
		doReturn(Optional.of(sub)).when(subcategoryRepository).findById(1);
        doReturn(product).when(productRepository).save(any());
		
        this.mockMvc.perform(post("/categs/*/sub/{id}/products", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost:8080/categs/*/sub/1/products/10"))
				.andExpect(redirectedUrl("http://localhost:8080/categs/*/sub/1/products/10"));
	}
	
	@Test
	final void editProductTest() throws Exception {
		
		Set<ShoppingList> shoppinglists = new HashSet<>();
		sub.setId(1);
		Product product = new Product(10, "samsung", "smartphone", 1000.0, sub, shoppinglists);
		productRepository.saveAndFlush(product);
		Product product2 = new Product(10, "apple", "new smartphone", 1100.0, sub, shoppinglists);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(product2);
        
		doReturn(Optional.of(sub)).when(subcategoryRepository).findById(1);
		doReturn(Optional.of(product)).when(productRepository).findById(10);	
        doReturn(product2).when(productRepository).save(any());
        
        this.mockMvc.perform(put("/categs/*/sub/{id}/products/{id2}", 1, 10)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
        		.andExpect(content().string("Product edited"));
//				.andExpect(jsonPath("$.id").value(10))
//				.andExpect(jsonPath("$.name").value("apple"))
//				.andExpect(jsonPath("$.description").value("new smartphone"))
//				.andExpect(jsonPath("$.price").value(1100.0));
	}
	
	@Test
	final void deleteProductTest() throws Exception {
		Set<ShoppingList> shoppinglists = new HashSet<>();
		Product product = new Product(10, "samsung", "smartphone", 1000.0, sub, shoppinglists);
		productRepository.saveAndFlush(product);
		
		doReturn(Optional.of(product)).when(productRepository).findById(10);
//		doNothing().when(productRepository).deleteById(10);
		
        this.mockMvc.perform(delete("/categs/*/sub/*/products/{id}", 10)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(content().string("Product deleted"));
	}
}
