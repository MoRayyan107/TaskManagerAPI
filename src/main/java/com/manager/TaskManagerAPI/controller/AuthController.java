package com.manager.TaskManagerAPI.controller;

import com.manager.TaskManagerAPI.model.AppUser;
import com.manager.TaskManagerAPI.model.AuthenticationRequest;
import com.manager.TaskManagerAPI.model.AuthenticationResponse;
import com.manager.TaskManagerAPI.Util.JwtUtil;
import com.manager.TaskManagerAPI.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;

/**
 * REST controller for handling user authentication requests.
 * <p>
 * Exposes a POST endpoint at /authenticate which accepts username and password,
 * validates credentials, and returns a JWT token if authentication succeeds.
 * <p>
 * Works with Spring Security's AuthenticationManager and UserDetailsService
 * to process login attempts and issue secure JWTs via JwtUtil.
 */
@RestController
@Tag(name = "Auth Controller" )
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    @Operation(summary = "Login method for user", description = "Logs in with user credentials giving access to create, delete and edit tasks")
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @Operation(summary = "Registering new users",
            description = """
                    this gives them access to log in and access to create, delete and edit tasks \n
                    Usage: \n
                    \t{ 
                      \t "username": "johndoe", 
                      \t "password": "secret123", 
                      \t "firstName": "John", 
                      \t "lastName": "Doe", 
                      \t "email": "john@example.com" 
                    \t}
                    """)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authRequest) throws Exception {
        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(authRequest.getUsername());
        String encodedPassword = passwordEncoder.encode(authRequest.getPassword());
        appUser.setPassword(encodedPassword);
        appUser.setRole(ROLE_USER);

        userRepository.save(appUser);
        return ResponseEntity.ok("User registered successfully");
    }

}
