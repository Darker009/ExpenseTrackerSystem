package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.entity.Savings;
import org.darker.exception.ResourceNotFoundException; // Existing exception
import org.darker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register User
    public void registerUser(User user) {
        // Check if email already exists
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new ResourceNotFoundException("Email already registered");
                });

        // Encoding password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRegisteredAt(LocalDateTime.now()); // Ensure registeredAt is set at the time of registration
        userRepository.save(user);
    }

    // ✅ User Login
    public UserDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.isActive()) {
            throw new ResourceNotFoundException("Account is deactivated. Please contact support.");
        }

        // Verify password with encoded value
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UserDTO(user.getName(), user.getEmail(), user.getRegisteredAt());
        }

        throw new ResourceNotFoundException("Invalid email or password");
    }

    // ✅ Get User Details
    public UserDTO getUserDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(user.getName(), user.getEmail(), user.getRegisteredAt());
    }

    // ✅ Update User Details
    public UserDTO updateUser(Long id, User userUpdates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new ResourceNotFoundException("User is deactivated. Cannot update.");
        }

        // Update user properties
        if (userUpdates.getName() != null && !userUpdates.getName().isEmpty()) {
            user.setName(userUpdates.getName());
        }
        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdates.getPassword())); // Re-encode the password
        }

        userRepository.save(user);
        return new UserDTO(user.getName(), user.getEmail(), user.getRegisteredAt());
    }

    // ✅ Deactivate User
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    // ✅ Ensure that the user has a savings account before deducting from savings
    public boolean hasSavingsAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if the user has a savings account linked
        Savings savings = user.getSavings();
        if (savings == null) {
            throw new ResourceNotFoundException("User does not have a savings account.");
        }

        return true; // User has a savings account
    }
}
