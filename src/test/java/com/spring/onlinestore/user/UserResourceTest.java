package com.spring.onlinestore.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.onlinestore.role.Role;
import com.spring.onlinestore.role.RoleRepository;

//@ContextConfiguration
//@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig
class UserResourceTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@InjectMocks
	UserResource userResource;
	
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
	}

	@Test // No authentication required
	void registerTest() throws Exception {
		
		User testUser = new User();
		Role role = new Role();
		
		testUser.setId(1);
		testUser.setEnabled(true);
		testUser.setUsername("user");
		testUser.setPassword("password");
		role.setId(10);
		role.setName("ROLE_USER");
		
		List<User> users = List.of(testUser);
		role.setUsers(users);
		testUser.setRole(role);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(testUser);
        
        doReturn(Optional.of(role)).when(roleRepository).findByName("ROLE_USER");
        doReturn(testUser).when(userRepository).save(any());
        
        this.mockMvc.perform(post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/user/user"))
				.andExpect(redirectedUrl("http://localhost/user/user"));
	}
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	@Disabled("Disabled until I find out why authentication is null")
	void editUserTest() throws Exception {
		User currentUser = new User();
		User newUser = new User();
		Role role = new Role();
		
		currentUser.setId(1);
		currentUser.setEnabled(true);
		currentUser.setUsername("user");
		currentUser.setPassword("password");
		newUser.setId(1);
		newUser.setEnabled(true);
		newUser.setUsername("user2");
		newUser.setPassword("password2");
		
		role.setId(10);
		role.setName("ROLE_USER");
		
		List<User> users = List.of(currentUser, newUser);
		role.setUsers(users);
		currentUser.setRole(role);
		newUser.setRole(role);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(newUser);
		
        
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
     	SecurityContextHolder.setContext(securityContext);
     	
     	doReturn(authentication).when(securityContext).getAuthentication();
     	doReturn(currentUser.getUsername()).when(authentication).getName();
        doReturn(Optional.of(role)).when(roleRepository).findByName("ROLE_USER");
        doReturn(Optional.of(currentUser)).when(userRepository).findByUsername("user");
        doReturn(newUser).when(userRepository).save(any());		
        
        this.mockMvc.perform(put("/user/{name}", "user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(authenticated().withAuthenticationName("user"))
				.andExpect(authenticated().withRoles("USER"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.username").value("user2"))
				.andExpect(jsonPath("$.password").value("new password2"));
	}
}
