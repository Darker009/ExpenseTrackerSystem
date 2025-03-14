package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.entity.Savings;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public void registerUser(User user) {
         userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
              throw new IllegalArgumentException("Email already registered");
         });

         user.setPassword(passwordEncoder.encode(user.getPassword()));
         user.setActive(true);
         user.setRegisteredAt(LocalDateTime.now());
         userRepository.save(user);
    }

    public UserDTO loginUser(String email, String password) {
         User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

         if (!user.isActive()) {
              throw new ResourceNotFoundException("Account is deactivated. Please contact support.");
         }

         if (passwordEncoder.matches(password, user.getPassword())) {
              return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt(), user.getProfileImageUrl());
         }

         throw new ResourceNotFoundException("Invalid email or password");
    }

    public UserDTO getUserDetails(Long id) {
         User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));
         return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt(), user.getProfileImageUrl());
    }

    public UserDTO updateUser(Long id, User userUpdates) {
         User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

         if (!user.isActive()) {
              throw new ResourceNotFoundException("User is deactivated. Cannot update.");
         }

         if (userUpdates.getName() != null && !userUpdates.getName().isEmpty()) {
              user.setName(userUpdates.getName());
         }
         if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
              if (userUpdates.getPassword().length() < 6) {
                   throw new IllegalArgumentException("Password must be at least 6 characters long.");
              }
              user.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
         }

         userRepository.save(user);
         return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt(), user.getProfileImageUrl());
    }

    // New method to update user profile including profile image.
    public UserDTO updateUserWithProfile(Long id, String name, String password, MultipartFile profileImage) {
         User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

         if (!user.isActive()) {
              throw new ResourceNotFoundException("User is deactivated. Cannot update.");
         }

         if (name != null && !name.isEmpty()) {
              user.setName(name);
         }
         if (password != null && !password.isEmpty()) {
              if (password.length() < 6) {
                   throw new IllegalArgumentException("Password must be at least 6 characters long.");
              }
              user.setPassword(passwordEncoder.encode(password));
         }

         if (profileImage != null && !profileImage.isEmpty()) {
              // Simulate file saving by generating a URL.
              // In a real application, save the file to disk or cloud storage and update the URL accordingly.
              String profileImageUrl = "http://localhost:8080/uploads/" + profileImage.getOriginalFilename();
              user.setProfileImageUrl(profileImageUrl);
         }

         userRepository.save(user);
         return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt(), user.getProfileImageUrl());
    }

    public void deactivateUser(Long id) {
         User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));
         user.setActive(false);
         userRepository.save(user);
    }

    public boolean hasSavingsAccount(Long userId) {
         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

         Savings savings = user.getSavings();
         if (savings == null) {
              throw new ResourceNotFoundException("User does not have a savings account.");
         }

         return true;
    }
}
