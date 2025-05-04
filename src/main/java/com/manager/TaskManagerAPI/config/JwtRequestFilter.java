package com.manager.TaskManagerAPI.config;

import com.manager.TaskManagerAPI.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.manager.TaskManagerAPI.constants.AppConstants.AUTH_HEADER;
import static com.manager.TaskManagerAPI.constants.AppConstants.TOKEN_PREFIX;

/**
 * Filter that intercepts every HTTP request to extract and validate a JWT token.
 * <p>
 * If a valid token is found in the Authorization header (starting with \"Bearer \"),
 * the filter sets the authenticated user into the Spring Security context.
 * <p>
 * This allows access to protected endpoints for authenticated users only.
 * Works with JwtUtil and UserDetailsService to perform validation and user lookup.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Intercepts each HTTP request to:
     * 1. Extract JWT token from the Authorization header.
     * 2. Validate the token.
     * 3. Authenticate the user by setting SecurityContext.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @param chain    the filter chain to continue request processing
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs during request handling
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException
    {
        final String authHeader = request.getHeader(AUTH_HEADER);
        logger.debug("Auth head: {}", authHeader);
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            jwt = authHeader.substring(7); // remove teh 'Bearer ' token
            username = jwtUtil.extractUsername(jwt);

            logger.debug("Token: " + jwt);
            logger.debug("Username: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            logger.info("Trying to log in...");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication successful");
            }
        }

        chain.doFilter(request, response);
    }

}
