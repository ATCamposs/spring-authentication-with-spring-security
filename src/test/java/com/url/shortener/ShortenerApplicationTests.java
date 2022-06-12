package com.url.shortener;

import com.url.shortener.controllers.params.LoginParams;
import com.url.shortener.controllers.params.UserRegisterParams;
import com.url.shortener.models.Role;
import com.url.shortener.models.User;
import com.url.shortener.repositories.RoleRepository;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.services.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordService passwordService;

    @Test
    void authenticate() {
        var loginParams = new LoginParams();
        loginParams.email = "john@doe.com";
        loginParams.password = "password";

        var response = restTemplate.postForEntity(
                "/api/auth/sign_in",
                loginParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        var user = new User();
        user.setEmail(loginParams.email);
        user.setUsername("john");
        user.setPassword(passwordService.hash(loginParams.password));
        userRepository.save(user);

        response = restTemplate.postForEntity(
                "/api/auth/sign_in",
                loginParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0)).contains("jwt");
    }

    @Test
    void create_account() {
        //REGISTER ROLES
        Role userRole = new Role();
        userRole.setName(Role.Type.USER);
        roleRepository.save(userRole);

        Role modRole = new Role();
        modRole.setName(Role.Type.MODERATOR);
        roleRepository.save(modRole);

        Role admRole = new Role();
        admRole.setName(Role.Type.ADMIN);
        roleRepository.save(admRole);

        var registerParams = new UserRegisterParams();
        registerParams.username = "john";
        registerParams.email = "john2@doe.com";
        registerParams.password = "password";

        var response = restTemplate.postForEntity(
                "/api/auth/sign_up",
                registerParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var loginParams = new LoginParams();
        loginParams.email = registerParams.email;
        loginParams.password = registerParams.password;

        response = restTemplate.postForEntity(
                "/api/auth/sign_in",
                loginParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}