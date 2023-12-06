package com.books.bkb.Config.Security;

import com.books.bkb.Config.Security.UserAuth.AuthDetail;
import com.books.bkb.Config.Security.UserAuth.AuthDetailService;
import com.books.bkb.Config.Security.UserAuth.AuthEntryPointJwt;
import com.books.bkb.Config.Security.UserAuth.AuthTokenFilter;
import com.books.bkb.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SecurityConfig {

    AuthDetailService authDetailService;
    AuthEntryPointJwt authEntryPointJwt;
    ObjectMapper objectMapper;

    @Autowired
    public SecurityConfig(AuthDetailService authDetailService, AuthEntryPointJwt authEntryPointJwt, ObjectMapper objectMapper) {
        this.authDetailService = authDetailService;
        this.authEntryPointJwt = authEntryPointJwt;
        this.objectMapper = objectMapper;
    }

    @Bean
    public AuthTokenFilter authtokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exp -> exp.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/neo4j").permitAll()
                                .requestMatchers("/admin/**").hasRole("ROLE_ADMIN")
                                .anyRequest().authenticated()
                );
        http.
                formLogin(form -> form.loginPage("/login")
                        .permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            setHeader(request, response);
                            AuthDetail user = (AuthDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            ResponseCookie responseCookie;
                            try {
                                responseCookie = JwtUtil.generateJwtCookie(user.getUsername(), user.id().toString(), user.getAuthorities().toString());
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                                throw new RuntimeException(e);
                            }
                            setHeader(request, response);
                            response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
                            response.setStatus(HttpServletResponse.SC_OK);
                            Map<String, Object> responseData = new HashMap<>();
                            responseData.put("id", user.id());
                            responseData.put("role", user.getAuthorities().stream().anyMatch(au -> au.getAuthority().contains("ROLE_ADMIN")) ? 1 : 0);
                            responseData.put(responseCookie.getName(), responseCookie.getValue());
                            responseData.put("Path", responseCookie.getPath());
                            try{
                                response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
                                response.getWriter().flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }))
                        .failureHandler(((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            setHeader(request, response);
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
                            Map<String, String> map = new HashMap<>();
                            map.put("message", "logoutSuccess");
                            response.getWriter().println(objectMapper.writeValueAsString(map));
                        }).permitAll());
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authtokenFilter(), UsernamePasswordAuthenticationFilter.class);

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

}

