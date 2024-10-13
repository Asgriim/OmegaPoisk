package org.omega.omegapoisk.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.UserDTO;
import org.omega.omegapoisk.entity.user.RoleEntity;
import org.omega.omegapoisk.entity.user.User;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.exception.UserAlreadyExistsException;
import org.omega.omegapoisk.repository.user.RoleRepository;
import org.omega.omegapoisk.repository.user.UserRepository;
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
        return userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public User getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) loadUserByUsername(userDetails.getUsername());
        return user;
    }

    @Transactional
    public void registerUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        RoleEntity roleEntity = roleRepository.findByName(user.getRole().getName()).orElseThrow(InvaliUserOrPasswordException::new);
        user.setRole(roleEntity);
        userRepository.save(user);
    }

}
