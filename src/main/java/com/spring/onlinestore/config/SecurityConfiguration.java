package com.spring.onlinestore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.spring.onlinestore.security.UserAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserAuthenticationProvider authProvider;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
    	// Authorized H2 access public - WILL BE DISABLED BEFORE RELEASE
        http.httpBasic().and().authorizeRequests()
        		.antMatchers("/h2-console/**", "/error").permitAll()
        		.antMatchers(HttpMethod.POST, "/user").permitAll()
          		.antMatchers(HttpMethod.GET, "/categs/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
          		.antMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
          		.antMatchers("/categs/**", "/user/**").hasAnyAuthority("ROLE_ADMIN")
          		.anyRequest().authenticated()
          		.and().formLogin().permitAll()
          		.and().logout().permitAll();
        
        http.csrf().disable();
        // Disables header security - This allows the use of the h2 console
        http.headers().frameOptions().disable();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}
