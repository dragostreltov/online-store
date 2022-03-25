package com.spring.onlinestore.product;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ProductResource.class)
public class ProductResourceTest {
	
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
	final void testRetrieveProduct() throws Exception {
		
		Product product = new Product();
		product.setId(10);
		product.setName("samsung");
		product.setDescription("smartphone");
		product.setPrice(1000.0);
		
		productRepository.saveAndFlush(product);

		doReturn(Optional.of(product)).when(productRepository).findById(10);

		this.mockMvc.perform(get("/categs/*/sub/*/products/{id}", 10))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("samsung"))
			.andExpect(jsonPath("$.description").value("smartphone"))
			.andExpect(jsonPath("$.price").value(1000.0));
	}
}
