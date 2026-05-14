package com.substring.auth.auth_app_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeHttpRequest->
                authorizeHttpRequest.requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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
