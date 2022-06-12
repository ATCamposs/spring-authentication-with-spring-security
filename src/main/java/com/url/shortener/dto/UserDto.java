package com.url.shortener.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public class UserDto {
    public String username;
    public String email;
    public Instant createdAt;
    public Instant updatedAt;
    public List<String> roles;
}
