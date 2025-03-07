package org.darker.service;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new ResourceNotFoundException("Invalid email or password");
    }

    public User getUserDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user;
    }


    public User updateUser(Long id, User userUpdates) {
        User user = getUserDetails(id);

        user.setName(userUpdates.getName());
        user.setEmail(userUpdates.getEmail());

        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = getUserDetails(id);
        user.setActive(false);
        userRepository.save(user);
    }
}
