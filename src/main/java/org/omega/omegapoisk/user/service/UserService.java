package org.omega.omegapoisk.user.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.user.entity.RoleEntity;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.exception.UserAlreadyExistsException;
import org.omega.omegapoisk.user.repository.RoleRepository;
import org.omega.omegapoisk.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User loadUserByUsername(String username)  {
        User user = userRepository.findByLogin(username).orElse(null);
        RoleEntity roleEntity = roleRepository.findById(user.getRoleId()).orElseThrow(InvaliUserOrPasswordException::new);
        user.setRoleId(roleEntity.getId());
        user.setRole(roleEntity);
        return user;
    }

    @Transactional
    public void registerUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException(user.getLogin() + ": already exist");
        }

        RoleEntity roleEntity = roleRepository.findByName(user.getRole().getName()).orElseThrow(InvaliUserOrPasswordException::new);
        user.setRole(roleEntity);
        user.setRoleId(roleEntity.getId());
        userRepository.save(user);
    }

}
