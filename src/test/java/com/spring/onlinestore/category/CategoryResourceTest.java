package com.spring.onlinestore.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.subcategory.Subcategory;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CategoryResource.class)
public class CategoryResourceTest {
	
	@Mock
	CategoryRepository categoryRepository;
	
	@InjectMocks
	CategoryResource categoryResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(categoryResource).build();
	}
	
	@Test
	final void retrieveAllCategoriesTest() throws Exception {
		
		this.mockMvc.perform(get("/categs"))
		.andExpect(status().isOk());
	}
	
	@Test
	final void createCategoryTest() throws Exception {
		List<Subcategory> subcats = new ArrayList<>();
        Category category = new Category(null, "Masini", subcats);
        
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(category);
       
		doReturn(category).when(categoryRepository).save(any());
        
		this.mockMvc.perform(post("/categs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	final void editCategoryTest() throws Exception {
		List<Subcategory> subcats = new ArrayList<>();
		Category category  = new Category(1, "Cars", subcats);
        categoryRepository.saveAndFlush(category);
        
        Category category2 = new Category(1, "Masini", subcats);
        
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(category2);
  
		doReturn(Optional.of(category)).when(categoryRepository).findById(1);
		doReturn(category2).when(categoryRepository).save(any());
        
        this.mockMvc.perform(put("/categs/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
        		.andExpect(status().isOk());
	}
	
	@Test
	final void deleteCategoryTest() throws Exception {
		List<Subcategory> subcats = new ArrayList<>();
		Category category  = new Category(1, "Cars", subcats);
        categoryRepository.saveAndFlush(category);
		
		doReturn(Optional.of(category)).when(categoryRepository).findById(1);
		doNothing().when(categoryRepository).deleteById(1);

		
        this.mockMvc.perform(delete("/categs/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
        		.andExpect(status().isOk());
	}
}
