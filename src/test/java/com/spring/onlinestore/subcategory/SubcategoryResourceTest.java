package com.spring.onlinestore.subcategory;

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
import com.spring.onlinestore.category.Category;
import com.spring.onlinestore.category.CategoryRepository;
import com.spring.onlinestore.product.Product;

@AutoConfigureRestDocs // defaults to target/generated-snippets
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class SubcategoryResourceTest {
	
	@Mock
	Category cat;
	
	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	SubcategoryRepository subcategoryRepository;
	
	@InjectMocks
	SubcategoryResource subcategoryResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(subcategoryResource)
				.apply(documentationConfiguration(restDocumentation))
				 .alwaysDo(document("{method-name}", 
						    preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.build();
	}
	
	@Test
	final void retrieveAllSubcategoriesTest() throws Exception {
		
		cat.setId(1);
		List<Product> products = new ArrayList<>();
		
		Subcategory sub1 = new Subcategory(10, "Subcategory1", products);
		sub1.setCat(cat);
		
		Subcategory sub2 = new Subcategory(11, "Subcategory2", products);
		sub2.setCat(cat);
		
		subcategoryRepository.saveAndFlush(sub1);
		subcategoryRepository.saveAndFlush(sub2);
		
		List<Subcategory> list = List.of(sub1, sub2);
		
		doReturn(Optional.of(cat)).when(categoryRepository).findById(1);
		doReturn(list).when(cat).getSubcats();
		
		this.mockMvc.perform(get("/categs/{id}/sub", 1))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(10))
				.andExpect(jsonPath("$[0].name").value("Subcategory1"))
				.andExpect(jsonPath("$[1].id").value(11))
				.andExpect(jsonPath("$[1].name").value("Subcategory2"));
	}
	
	@Test
	final void createSubcategoryTest() throws Exception {
		
		cat.setId(1);
		List<Product> products = new ArrayList<>();
		
		Subcategory sub1 = new Subcategory(10, "Subcategory1", products);
		sub1.setCat(cat);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sub1);
        
		doReturn(Optional.of(cat)).when(categoryRepository).findById(1);
        doReturn(sub1).when(subcategoryRepository).save(any());
        
        this.mockMvc.perform(post("/categs/{id}/sub", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost:8080/categs/1/sub/10"))
				.andExpect(redirectedUrl("http://localhost:8080/categs/1/sub/10"));
	}
	
	@Test
	final void editSubcategoryTest() throws Exception {
		
		cat.setId(1);
		List<Product> products = new ArrayList<>();
		
		Subcategory sub1 = new Subcategory(10, "Subcategory1", products);
		sub1.setCat(cat);
		subcategoryRepository.saveAndFlush(sub1);
		
		Subcategory sub2 = new Subcategory(10, "Subcategory2_modified", products);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sub2);
        
        doReturn(Optional.of(cat)).when(categoryRepository).findById(1);
        doReturn(Optional.of(sub1)).when(subcategoryRepository).findById(10);
        doReturn(sub2).when(subcategoryRepository).save(any());
        
        this.mockMvc.perform(put("/categs/{id}/sub/{id2}", 1, 10)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.name").value("Subcategory2_modified"));
	}
	
	@Test
	final void deleteSubcategoryTest() throws Exception {
		
		List<Product> products = new ArrayList<>();
		Subcategory sub1 = new Subcategory(10, "Subcategory1", products);
		
		doReturn(Optional.of(sub1)).when(subcategoryRepository).findById(10);
		doNothing().when(subcategoryRepository).deleteById(10);
		
		this.mockMvc.perform(delete("/categs/*/sub/{id}", 10)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
