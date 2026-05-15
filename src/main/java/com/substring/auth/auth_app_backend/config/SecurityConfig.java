package com.substring.auth.auth_app_backend.config;

import com.substring.auth.auth_app_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest->
                         authorizeHttpRequest.requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
                                             .requestMatchers("/api/v1/auth/login").permitAll()
                                             .anyRequest()
                                             .authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint((request, response, authException) -> {
                    authException.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String message = "Unautherized Access "+authException.getMessage();
                    Map<String, String> errorMap = Map.of("message",message,"statusCode", Integer.toString(401));
                    var mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(errorMap));
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //.httpBasic(Customizer.withDefaults());

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
