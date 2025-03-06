package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User(userDTO.getName(), userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
        	System.out.println(passwordEncoder.matches(password, user.getPassword()));
        	return user;
        }
    	throw new ResourceNotFoundException("Invalid email or password");    
        
    }

    public User getUserDetails(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserDetails(id);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = getUserDetails(id);
        user.setActive(false);
        userRepository.save(user);
    }
}
