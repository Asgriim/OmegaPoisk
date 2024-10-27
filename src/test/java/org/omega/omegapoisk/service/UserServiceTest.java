package org.omega.omegapoisk.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.user.entity.RoleEntity;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.exception.UserAlreadyExistsException;
import org.omega.omegapoisk.user.repository.RoleRepository;
import org.omega.omegapoisk.user.repository.UserRepository;
import org.omega.omegapoisk.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@Testcontainers
class UserServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Value("${spring.application.page-size}")
    int pageSize;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldRegisterUser() {
        RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(InvaliUserOrPasswordException::new);
        userService.registerUser(new User(0, "aboba", "abobapass", "aboba@mail.ru", 1, roleEntity));
        assertThat(userService.loadUserByUsername("aboba")).isNotNull();
    }

    @Test
    void shouldNotRegisterUser() {
        RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(InvaliUserOrPasswordException::new);
        userService.registerUser(new User(0, "aboba", "abobapass", "aboba@mail.ru", 1, roleEntity));
        Throwable thrown = catchThrowable(() -> {
            userService.registerUser(new User(0, "aboba", "abobapass", "aboba@mail.ru", 1, roleEntity));
        });
        assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
    }
}