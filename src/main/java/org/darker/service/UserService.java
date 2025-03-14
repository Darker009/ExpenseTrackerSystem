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
	        // Return a UserDTO that now includes the user ID.
	        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt());
	    }

	    throw new ResourceNotFoundException("Invalid email or password");
	}


	public UserDTO getUserDetails(Long id) {
	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt());
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
	    return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRegisteredAt());
	}

	public void deactivateUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		user.setActive(false);
		userRepository.save(user);
	}

	public boolean hasSavingsAccount(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Savings savings = user.getSavings();
		if (savings == null) {
			throw new ResourceNotFoundException("User does not have a savings account.");
		}

		return true;
	}
}
