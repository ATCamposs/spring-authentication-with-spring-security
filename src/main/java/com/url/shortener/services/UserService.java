package com.url.shortener.services;

import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.dto.UserDto;
import com.url.shortener.models.Role;
import com.url.shortener.repositories.RoleRepository;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    @Transactional(rollbackOn = Exception.class)
    public UserDto createDefaultByParams(UserRegisterParams userRegisterParams) {
        userRegisterParams.password = passwordService.hash(userRegisterParams.password);
        var newUser = UserRegisterParams.MAPPER.toModel(userRegisterParams);

        Set<Role> roles = getUserRoles();

        newUser.setRoles(roles);
        userRepository.save(newUser);
        var user = userRepository.findByEmail(newUser.getEmail()).get();

        var token = jwtUtils.generateTokenFromEmail(newUser.getEmail());
        return UserDto.MAPPER.toDto(user, roles, token, "");
    }

    public UserDto loginByEmailAndPassword(String email, String password) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) return null;
        var actualUser = user.get();
        if (passwordService.verify(password, actualUser.getPassword())) {
            var roles = roleRepository.findByUserEmail(email);
            var token = jwtUtils.generateTokenFromEmail(email);
            return UserDto.MAPPER.toDto(actualUser, roles, token, "");
        }
        return null;
    }

    public void removeRefreshToken(String email) {
        //userRepository.removeUserRefreshToken();
    }

    private Set<Role> getUserRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.Type.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        return roles;
    }
}
