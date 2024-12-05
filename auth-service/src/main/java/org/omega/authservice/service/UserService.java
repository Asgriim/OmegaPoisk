package org.omega.authservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.authservice.entity.RoleEntity;
import org.omega.authservice.entity.User;
import org.omega.authservice.repository.RoleRepository;
import org.omega.authservice.repository.UserRepository;
import org.omega.authservice.util.UserAlreadyExistException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(roleService.getByRoleId(user.getRoleId()));
        return user;
    }

    @Transactional
    public void createUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistException("User " + user.getLogin() + " already exists");
        }
        userRepository.save(user);
    }
}
