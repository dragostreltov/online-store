package com.spring.onlinestore.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.subcategory.Subcategory;

@AutoConfigureRestDocs // defaults to target/generated-snippets
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
//@SpringBootTest
public class CategoryResourceTest {
	
	@Mock
	CategoryRepository categoryRepository;
	
	@InjectMocks
	CategoryResource categoryResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(categoryResource)
				.apply(documentationConfiguration(restDocumentation))
				 .alwaysDo(document("{method-name}", 
						    preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.build();
	}
	
	@Test
	final void retrieveAllCategoriesTest() throws Exception {
		
		List<Subcategory> sub = new ArrayList<>();
		Category cat1 = new Category(1, "Category 1", sub);
		Category cat2 = new Category(2, "Category 2", sub);
		List<Category> cats = List.of(cat1, cat2);
		
		doReturn(cats).when(categoryRepository).findAll();
		
		this.mockMvc.perform(get("/categs"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Category 1"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("Category 2"));
	}
	
	@Test
	final void createCategoryTest() throws Exception {
		List<Subcategory> subcats = new ArrayList<>();
        Category category = new Category(1, "Masini", subcats);
        
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(category);
       
		doReturn(category).when(categoryRepository).save(any());
        
		this.mockMvc.perform(post("/categs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost:8080/categs/1"))
				.andExpect(redirectedUrl("http://localhost:8080/categs/1"));
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
        		.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id").value(1))
    			.andExpect(jsonPath("$.name").value("Masini"));
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
        		.andExpect(status().isOk())
        		.andExpect(content().string("Category deleted"));
	}
}
