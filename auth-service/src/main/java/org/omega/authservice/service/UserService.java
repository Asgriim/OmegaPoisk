package org.omega.authservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.authservice.entity.User;
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

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void createUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistException("User " + user.getLogin() + " already exists");
        }
        userRepository.save(user);
    }
}
