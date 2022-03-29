package com.spring.onlinestore.security;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.spring.onlinestore.user.StoreUserDetails;
import com.spring.onlinestore.user.StoreUserDetailsService;
import com.spring.onlinestore.user.User;
import com.spring.onlinestore.user.UserRepository;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    StoreUserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		final UsernamePasswordAuthenticationToken upAuth = (UsernamePasswordAuthenticationToken) authentication;
        
		final String name = (String) authentication.getPrincipal();
		final String password = (String) upAuth.getCredentials();
        final String storedPassword = userRepository.findByUsername(name).map(User::getPassword)
            .orElseThrow(() -> new BadCredentialsException("illegal username or password"));

        if (Objects.equals(password, "") || !Objects.equals(password, storedPassword)) {
            throw new BadCredentialsException("illegal username or password");
        }
        
        StoreUserDetails userDetails = userDetailsService.loadUserByUsername(name);

        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
              userDetails.getUsername(), authentication.getCredentials(), userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return true;
	}
}
