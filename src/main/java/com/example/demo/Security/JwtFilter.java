package com.example.demo.Security;

import com.example.demo.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter set before UsernamePasswordAuthenticationFilter in SecurityFilterChain.
 * This filter handles JWT token.*/
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtFilter(CustomUserDetailsService userDetailsService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    /**
     * Validate token if valid pass generate and pass
     * UsernamePasswordAuthentication token to UsernamePasswordAuthenticationFilter.*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if(jwt!=null && jwtUtils.validateToken(jwt)){
                String username = jwtUtils.getUserNameFromToken(jwt);
                UserDetails userDetails =userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception e){
            System.out.println("Cannot set authentication");
            System.out.println(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
//Helper method
    private String parseJwt(HttpServletRequest request) {
        String jwt =jwtUtils.getJwtFromHeader(request);
        return jwt;
    }
}
