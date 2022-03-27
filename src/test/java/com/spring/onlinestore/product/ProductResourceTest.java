package com.spring.onlinestore.product;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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

import com.spring.onlinestore.subcategory.Subcategory;
import com.spring.onlinestore.subcategory.SubcategoryRepository;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ProductResource.class)
public class ProductResourceTest {
	
//	private static final Logger log = LoggerFactory.getLogger(ProductResourceTest.class);
	
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
		
		productRepository.saveAndFlush(product);

		doReturn(Optional.of(product)).when(productRepository).findById(10);

		this.mockMvc.perform(get("/categs/*/sub/*/products/{id}", 10))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("samsung"))
			.andExpect(jsonPath("$.description").value("smartphone"))
			.andExpect(jsonPath("$.price").value(1000.0));
	}
}
