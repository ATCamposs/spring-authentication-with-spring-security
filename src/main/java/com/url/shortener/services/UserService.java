package com.url.shortener.services;

import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.models.User;
import com.url.shortener.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @Transactional(rollbackOn = Exception.class)
    public User saveByParams(UserRegisterParams userRegisterParams) {
        userRegisterParams.password = passwordService.hash(userRegisterParams.password);
        var newUser = UserRegisterParams.MAPPER.toModel(userRegisterParams);
        return userRepository.save(newUser);
    }
}
