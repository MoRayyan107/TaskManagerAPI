package com.manager.TaskManagerAPI.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This class loads user details for authentication during login.
 * Currently, supports a hardcoded user for demonstration purposes.
 * <p>
 * Used by Spring Security to validate JWT tokens and establish security context.
 * <p>
 * hardcoded login details:
 * <br>
 * Username: admin <br>
 * Password: password (noop-encoded)
 */
@Service
public class CustomerDetailService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomerDetailService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            return new User("admin","{noop}password", List.of(new SimpleGrantedAuthority(ROLE_USER)));
        } else {
            logger.log(Level.INFO, "Incorrect username or password");
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
