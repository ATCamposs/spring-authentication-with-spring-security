package com.url.shortener.services;

import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.models.Role;
import com.url.shortener.models.User;
import com.url.shortener.repositories.RoleRepository;
import com.url.shortener.repositories.UserRepository;
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

    @Transactional(rollbackOn = Exception.class)
    public User createDefaultByParams(UserRegisterParams userRegisterParams) {
        userRegisterParams.password = passwordService.hash(userRegisterParams.password);
        var newUser = UserRegisterParams.MAPPER.toModel(userRegisterParams);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.Type.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }
}
