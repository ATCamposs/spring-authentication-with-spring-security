package com.url.shortener.controllers;

import com.url.shortener.controllers.params.LoginParams;
import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.dto.UserDto;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping(path = "/sign_in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> authenticateUser(@Valid @RequestBody LoginParams loginParams) {
        var userDto = userService.loginByEmailAndPassword(loginParams.email, loginParams.password);
        if (userDto == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping(path = "/sign_up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterParams userRegisterParams) {
        if (userRepository.existsByEmail(userRegisterParams.email))
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already taken!"));

        var newUser = userService.createDefaultByParams(userRegisterParams);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping(path = "/sign_out", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> logoutUser() {
        return ResponseEntity.ok().body(Map.of("message", "You've been signed out!"));
    }
}