package com.url.shortener.controllers;

import com.url.shortener.controllers.params.LoginParams;
import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.dto.UserDto;
import com.url.shortener.repositories.RoleRepository;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.security.jwt.JwtUtils;
import com.url.shortener.security.services.UserDetailsImpl;
import com.url.shortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(path = "/sign_in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> authenticateUser(@Valid @RequestBody LoginParams loginParams) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginParams.email, loginParams.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(UserDto.builder()
                        .username(userDetails.getUsername())
                        .email(userDetails.getEmail())
                        .createdAt(userDetails.getCreatedAt())
                        .updatedAt(userDetails.getUpdatedAt())
                        .roles(roles)
                        .build());
    }

    @PostMapping(path = "/sign_up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterParams userRegisterParams) {
        if (userRepository.existsByEmail(userRegisterParams.email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already taken!"));
        }

        var newUser = userService.createDefaultByParams(userRegisterParams);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.builder()
                .username("123")
                .email("12312")
                .createdAt(Instant.now())
                .build());
    }

    @PostMapping(path = "/sign_out", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "You've been signed out!"));
    }
}