package com.url.shortener.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    public String hash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public boolean verify(String password, String actual) {
        return new BCryptPasswordEncoder().matches(password, actual);
    }
}