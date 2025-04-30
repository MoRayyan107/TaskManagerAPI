package com.manager.TaskManagerAPI.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;

import java.util.List;

@Service
public class CustomerDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            return new User("admin","{noop}password", List.of(new SimpleGrantedAuthority(ROLE_USER)));
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
