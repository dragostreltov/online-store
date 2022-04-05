package com.spring.onlinestore.coupon;

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

@AutoConfigureRestDocs // defaults to target/generated-snippets
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class CouponResourceTest {

	@Mock
	CouponRepository couponRepository;
	
	@InjectMocks
	CouponResource couponResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(couponResource)
				.apply(documentationConfiguration(restDocumentation))
				 .alwaysDo(document("{method-name}", 
						    preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.build();
	}
	
	@Test
	void retrieveAllCouponsTest() throws Exception {
		Coupon coupon1 = new Coupon(1, "TOTAL15", 0.85);
		Coupon coupon2 = new Coupon(2, "TOTAL20", 0.80);
		List<Coupon> list = List.of(coupon1, coupon2);
		
		doReturn(list).when(couponRepository).findAll();
		
		this.mockMvc.perform(get("/coupons"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].code").value("TOTAL15"))
			.andExpect(jsonPath("$[0].percentage").value(0.85))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].code").value("TOTAL20"))
			.andExpect(jsonPath("$[1].percentage").value(0.80));
	}
	
	@Test
	void createCouponTest() throws Exception {
		Coupon coupon = new Coupon(1, "TOTAL15", 0.85);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(coupon);
        
        doReturn(coupon).when(couponRepository).save(any());
        
		this.mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost:8080/coupons/1"))
				.andExpect(redirectedUrl("http://localhost:8080/coupons/1"));
	}
	
	@Test
	void editCouponTest() throws Exception {
		Coupon oldCoupon = new Coupon(1, "TOTAL15", 0.85);
		couponRepository.saveAndFlush(oldCoupon);
		Coupon newCoupon = new Coupon(1, "TOTAL20", 0.80);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(newCoupon);
        
		doReturn(Optional.of(oldCoupon)).when(couponRepository).findById(1);
		doReturn(newCoupon).when(couponRepository).save(any());
		
		this.mockMvc.perform(put("/coupons/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id").value(1))
    			.andExpect(jsonPath("$.code").value("TOTAL20"))
    			.andExpect(jsonPath("$.percentage").value(0.80));
	}
	
	@Test
	void deleteCouponTest() throws Exception {
		Coupon oldCoupon = new Coupon(1, "TOTAL15", 0.85);
		couponRepository.saveAndFlush(oldCoupon);
		
		doReturn(Optional.of(oldCoupon)).when(couponRepository).findById(1);
		doNothing().when(couponRepository).deleteById(1);
		
        this.mockMvc.perform(delete("/coupons/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(content().string("Coupon deleted"));
	}
	

}
