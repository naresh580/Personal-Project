package com.substring.auth.auth_app_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

/*    @Bean
    public UserDetailsService user(){
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails userDetails =  userBuilder
                .username("Naresh")
                .password("Naresh@123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }*/
}
