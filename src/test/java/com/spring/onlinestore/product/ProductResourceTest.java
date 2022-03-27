package com.spring.onlinestore.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.subcategory.Subcategory;
import com.spring.onlinestore.subcategory.SubcategoryRepository;

@ExtendWith(MockitoExtension.class)
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
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
	}
	
	@Test
	final void retrieveAllProductsTest() throws Exception {
		
		sub.setId(1);
		
		Product product = new Product(10, "samsung", "smartphone", 1000.0);
		product.setSubcat(sub);
		
		Product product2 = new Product(11, "apple", "smartphone 2", 900.0);
		product2.setSubcat(sub);
		
		productRepository.saveAndFlush(product);
		productRepository.saveAndFlush(product2);
		
		List<Product> list = List.of(product, product2);
		
		doReturn(Optional.of(sub)).when(subcategoryRepository).findById(1);
		doReturn(list).when(sub).getProds();
		
		this.mockMvc.perform(get("/categs/*/sub/{id}/products", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(10))
			.andExpect(jsonPath("$[0].name").value("samsung"))
			.andExpect(jsonPath("$[0].price").value(1000.0))
			.andExpect(jsonPath("$[1].id").value(11))
			.andExpect(jsonPath("$[1].name").value("apple"))
			.andExpect(jsonPath("$[1].price").value(900.0));
	}
	
	@Test
	final void retrieveProductTest() throws Exception {
		
		Product product = new Product(10, "samsung", "smartphone", 1000.0);
		
		// For HATEOAS link
		Subcategory subcat = new Subcategory();
		subcat.setId(2);
		product.setSubcat(subcat);
		/////////////////////////
		
		productRepository.saveAndFlush(product);

		doReturn(Optional.of(product)).when(productRepository).findById(10);

		this.mockMvc.perform(get("/categs/*/sub/*/products/{id}", 10))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("samsung"))
			.andExpect(jsonPath("$.description").value("smartphone"))
			.andExpect(jsonPath("$.price").value(1000.0))
			.andExpect(jsonPath("$.links[0].href").value("http://localhost/categs/*/sub/2/products"));
	}
	
	@Test
	final void createProductTest() throws Exception {
		
		sub.setId(1);
		Product product = new Product(10, "samsung", "smartphone", 1000.0);
		product.setSubcat(sub);
		
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
				.andExpect(header().string("Location", "http://localhost/categs/*/sub/1/products/10"))
				.andExpect(redirectedUrl("http://localhost/categs/*/sub/1/products/10"));
	}
	
	@Test
	final void editProductTest() throws Exception {
		
		sub.setId(1);
		Product product = new Product(10, "samsung", "smartphone", 1000.0);
		product.setSubcat(sub);
		productRepository.saveAndFlush(product);
		
		Product product2 = new Product(10, "apple", "new smartphone", 1100.0);
		
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
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.name").value("apple"))
				.andExpect(jsonPath("$.description").value("new smartphone"))
				.andExpect(jsonPath("$.price").value(1100.0));
	}
	
	@Test
	final void deleteProductTest() throws Exception {
		
		Product product = new Product(10, "samsung", "smartphone", 1000.0);
		productRepository.saveAndFlush(product);
		
		doReturn(Optional.of(product)).when(productRepository).findById(10);
		doNothing().when(productRepository).deleteById(10);
		
        this.mockMvc.perform(delete("/categs/*/sub/*/products/{id}", 10)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
        		.andExpect(status().isOk());
	}
}
