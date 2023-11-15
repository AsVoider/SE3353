package com.books.bkb.Config.Security;

import com.books.bkb.Config.Security.UserAuth.AuthDetail;
import com.books.bkb.Config.Security.UserAuth.AuthDetailService;
import com.books.bkb.DTO.AuthServiceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SecurityConfig {

    AuthDetailService authDetailService;
    ObjectMapper objectMapper;

    @Autowired
    public SecurityConfig(AuthDetailService authDetailService, ObjectMapper objectMapper) {
        this.authDetailService = authDetailService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.
                cors().and().csrf().disable().httpBasic();
        http.
                formLogin(form -> form.loginPage("/login").permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            setHeader(request, response);
                            AuthDetail user = (AuthDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            AuthServiceDTO authServiceDTO = new AuthServiceDTO();
                            authServiceDTO.setId(user.id());
                            authServiceDTO.setName(user.name());
                            authServiceDTO.setUsername(user.getUsername());
                            authServiceDTO.setAdmin(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
                            response.getWriter().println(objectMapper.writeValueAsString(authServiceDTO));
                            //clockServe.OnLogin();
                            System.out.println("success");
                        }))
                        .failureHandler(((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            setHeader(request, response);
                            System.out.println();
                            if(exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException)
                                response.getWriter().println("Wrong Username or Password");
                            else if(exception instanceof DisabledException)
                                response.getWriter().println("Your Account is disabled");
                            System.out.println("FAILED");
                        }))
                );
        http.
                logout(lg -> lg.logoutUrl("/logout").permitAll()
                        .logoutSuccessHandler((request, response, authentication) -> {
                            setHeader(request, response);
                            response.setStatus(HttpServletResponse.SC_OK);
                            //String time = clockServe.OnLogout();
                            Map<String, String> map = new HashMap<>();
                            //map.put("message", time);
                            response.getWriter().println(objectMapper.writeValueAsString(map));
                        }).permitAll());
        http.
                authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/public/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());


        return http.build();
    }

    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json,charset=utf-8");
        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Credentials", "true");
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setAllowedMethods(Collections.singletonList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration(("/**"), configuration);
//        return source;
//    }
}

