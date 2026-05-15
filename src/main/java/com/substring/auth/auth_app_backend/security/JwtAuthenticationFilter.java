package com.substring.auth.auth_app_backend.security;

import com.substring.auth.auth_app_backend.entities.User;
import com.substring.auth.auth_app_backend.helpers.UserHelper;
import com.substring.auth.auth_app_backend.repositories.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        logger.info("Authorization header: {}",header);
        if(header !=null && header.startsWith("Bearer ")){

            //token extract and validate then authentication create and then set in security context
            String token = header.substring(7);
            try{
                Jws<Claims> parse = jwtService.parse(token);
                Claims claims = parse.getPayload();
                //check for access token
                if(!jwtService.isAccessToken(token)){
                    filterChain.doFilter(request, response);
                    return;
                }
                String userId = claims.getSubject();
                UUID uId = UserHelper.parseUUID(userId);

                userRepository.findById(uId)
                        .ifPresent(user  -> {
                            if(user.isEnable()) {

                                List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() :
                                        user.getRoles().stream()
                                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                                        .collect(Collectors.toList());
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                                authentication.setDetails(new WebAuthenticationDetails(request));
                                //final line to set the authentication to security context
                                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                            }
                        });

            }catch (ExpiredJwtException ex){
                ex.printStackTrace();
            }catch (MalformedJwtException ex){
                ex.printStackTrace();
            }catch (JwtException ex){
                ex.printStackTrace();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
