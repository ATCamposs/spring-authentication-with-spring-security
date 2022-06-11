package com.url.shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String hash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public boolean verify(String password, String actual) {
        return bCryptPasswordEncoder.matches(password, actual);
    }
}