package org.darker.controller;

import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// User registration with name, email, and password.
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		if (user.getEmail() == null || user.getPassword() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required."));
		}

		try {
			userService.registerUser(user);
			return ResponseEntity.ok(Map.of("message", "User registered successfully"));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}

	// User login with email and password.
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		if (user.getEmail() == null || user.getPassword() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required."));
		}

		try {
			UserDTO userDTO = userService.loginUser(user.getEmail(), user.getPassword());
			return ResponseEntity.ok(userDTO);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}

	// Fetch user details.
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable Long id) {
		try {
			UserDTO user = userService.getUserDetails(id);
			return ResponseEntity.ok(user);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}

	// Update user profile (name, password, and optional profile image).
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestParam("name") String name,
			@RequestParam("password") String password,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
		try {
			UserDTO updatedUser = userService.updateUserWithProfile(id, name, password, profileImage);
			return ResponseEntity.ok(updatedUser);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}

	// Deactivate the user account (deactivates but does not delete).
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
		try {
			userService.deactivateUser(id);
			return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}
}
