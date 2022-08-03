package com.url.shortener.runners;

import com.url.shortener.models.Role;
import com.url.shortener.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void run(ApplicationArguments args) {
        roleRepository.save(new Role(Role.Type.USER));
        roleRepository.save(new Role(Role.Type.MODERATOR));
        roleRepository.save(new Role(Role.Type.ADMIN));
    }
}