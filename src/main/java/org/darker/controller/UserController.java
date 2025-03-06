package org.darker.controller;

import org.darker.dto.UserDTO;
import org.darker.entity.User;
import org.darker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody(required = true) UserDTO userDTO) {
        
    	System.out.println(userDTO.getEmail()+"\n "+userDTO.getPassword());
    	if(userDTO.getEmail()==null || userDTO.getPassword()==null) {
        	return ResponseEntity.badRequest().body(null);
        }
    	return ResponseEntity.ok(userService.loginUser(userDTO.getEmail(), userDTO.getPassword()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }
}
