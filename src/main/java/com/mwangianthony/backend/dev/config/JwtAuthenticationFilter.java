package com.mwangianthony.backend.dev.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mwangianthony.backend.dev.services.JwtServices;
import com.mwangianthony.backend.dev.services.UserServices;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtServices jwtservices;
	private UserServices userDetailsServices;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
        final String authHeader;
        final String jwt;
        final String userEmail;  
        
        authHeader = request.getHeader("Authorization");
        
		if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwt = authHeader.substring(7);
		
		userEmail = jwtservices.extractUsername(jwt);
		
		if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
			
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			
			UserDetails userDetails = userDetailsServices.userDetailsServices().loadUserByUsername(userEmail);
			
			if(jwtservices.isTokenValid(userEmail, userDetails)) {
				
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				securityContext.setAuthentication(authToken);
				SecurityContextHolder.setContext(securityContext);
			}
		}
	}

}