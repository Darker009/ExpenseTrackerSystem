package org.darker.controller;

import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	//user can register with name, email, and password.
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

	//user can login with email and password
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

	//user can fetch details 
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

	//user can update only password(changes accordingly)
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userUpdates) {
		try {
			UserDTO updatedUser = userService.updateUser(id, userUpdates);
			return ResponseEntity.ok(updatedUser);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An unexpected error occurred"));
		}
	}

	//user can deactivate his account(this particular end-point deactivate the user but not delete)
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
