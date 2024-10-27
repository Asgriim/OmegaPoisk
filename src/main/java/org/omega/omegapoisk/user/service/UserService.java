package org.omega.omegapoisk.user.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.user.entity.RoleEntity;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.exception.UserAlreadyExistsException;
import org.omega.omegapoisk.user.repository.RoleRepository;
import org.omega.omegapoisk.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        RoleEntity roleEntity = roleRepository.findById(user.getRoleId()).orElseThrow(InvaliUserOrPasswordException::new);
        user.setRoleId(roleEntity.getId());
        user.setRole(roleEntity);
        return user;
    }

    public User getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) loadUserByUsername(userDetails.getUsername());
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
