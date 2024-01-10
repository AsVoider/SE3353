package com.books.bkb.Config.Security.UserAuth;

import jakarta.servlet.FilterChain;
import io.jsonwebtoken.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.books.bkb.utils.JwtUtil;

import java.io.IOException;
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    AuthDetailService userDetailServiceImp;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parse(request);
            if ("OPTIONS".equals(request.getMethod())) {
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            if(jwt != null && JwtUtil.validateJwt(jwt))
            {
                Claims claims = JwtUtil.getUsernameFromJwt(jwt);
                String username = claims.get("username", String.class);
                String id = claims.get("id", String.class);
                String requestId = request.getParameter("uid");
                if(requestId == null || id.equals(requestId))
                {
                    UserDetails userDetails = userDetailServiceImp.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e)
        {
            logger.error("Set Auth Failed : {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    private String parse(HttpServletRequest request)
    {
        return JwtUtil.getJwtFromCookies(request);
    }
}
