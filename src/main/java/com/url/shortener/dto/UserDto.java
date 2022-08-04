package com.url.shortener.dto;

import com.url.shortener.models.Role;
import com.url.shortener.models.User;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mapstruct.factory.Mappers.getMapper;

@Builder
public class UserDto {
    public final static Mappers MAPPER = getMapper(Mappers.class);
    public String username;
    public String email;
    public Instant createdAt;
    public Instant updatedAt;
    public List<String> roles;
    public String token;
    public String refreshToken;

    @Mapper
    public interface Mappers {
        @Mapping(target = "roles", expression = "java(roles.stream().map(role -> role.getName().toString()).collect(java.util.stream.Collectors.toList()))")
        UserDto toDto(User user, Set<Role> roles, String token, String refreshToken);
    }
}
