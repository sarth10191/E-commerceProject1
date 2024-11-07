package com.example.demo.Config;

import com.example.demo.Security.AuthEntryPoint;
import com.example.demo.Security.CustomUserDetailsService;
import com.example.demo.Security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final CustomUserDetailsService userDetailsService;

    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService, AuthEntryPoint authEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)//no csrf hence every request is a new stateless request.
                .httpBasic(Customizer.withDefaults())//handle basic authentication using default security features.
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//disable session maintenance on client machine. Makes api stateless.
                .cors(Customizer.withDefaults())//handle cross-origin request service.
                .authenticationProvider(daoAuthenticationProvider())//set authentication provider.
                .authorizeHttpRequests(
                        request->request.requestMatchers("/api/login", "/api/register").permitAll()
                                .anyRequest().authenticated()
                )//allow all clients to log in and register.
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authEntryPoint))//unauthorised request handling passed to auth entry point.
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();//set jwt filter before UsernamePasswordAuthenticationFilter
    }

    /**
     * Allow cross-origin requests from stated origins. Pass a list of IP-addresses in allow origin method. */
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5432", "http://localhost:9000"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE"));
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
