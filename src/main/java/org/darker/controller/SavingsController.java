package org.darker.controller;

import org.darker.dto.SavingsDTO;
import org.darker.dto.SavingsLimitedDTO;
import org.darker.exception.InsufficientBalanceException;
import org.darker.exception.ResourceNotFoundException;
import org.darker.service.SavingsService;
import org.darker.entity.Savings;
import org.darker.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/savings")
public class SavingsController {
	private final SavingsService savingsService;

	public SavingsController(SavingsService savingsService) {
		this.savingsService = savingsService;
	}

	// Endpoint to register a savings account for a user.
	@PostMapping("/register/{userId}")
	public ResponseEntity<?> registerSavings(@PathVariable Long userId, @RequestBody SavingsDTO savingsRequest) {
		try {
			// Retrieve the user; if not found, throw exception.
			User user = savingsService.getUserById(userId);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}

			// Create a new savings account using the provided details.
			Savings savings = new Savings();
			savings.setUser(user);
			BigDecimal initialBalance = savingsRequest.getTotalBalance() != null ? savingsRequest.getTotalBalance()
					: BigDecimal.ZERO;
			savings.setTotalBalance(initialBalance);
			savings.setRemainingBalance(initialBalance);
			savings.setAadhaarNumber(savingsRequest.getAadhaarNumber());
			savings.setAccountNumber(savingsRequest.getAccountNumber());
			savings.setPanNumber(savingsRequest.getPanNumber());
			savings.setPhoneNumber(savingsRequest.getPhoneNumber());
			savings.setAddress(savingsRequest.getAddress());
			savings.setDate(LocalDate.now());

			Savings savedSavings = savingsService.saveSavings(savings);
			return ResponseEntity.status(HttpStatus.CREATED).body(new SavingsDTO(savedSavings));

		} catch (Exception e) {
			e.printStackTrace(); // Log full error details for debugging.
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register savings. " + e.getMessage());
		}
	}

	// Endpoint to get savings details for a user.
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserSavings(@PathVariable Long userId) {
		try {
			Savings savings = savingsService.getUserSavings(userId);
			// Return only the remaining balance in the limited DTO.
			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Savings not found for user.");
		}
	}

	// Endpoint to deposit funds into the savings account.
	@PostMapping("/{userId}/deposit")
	public ResponseEntity<?> depositFunds(@PathVariable Long userId, @RequestBody Map<String, BigDecimal> request) {
		try {
			BigDecimal amount = request.get("amount");
			validateAmount(amount, "Deposit amount must be greater than zero.");

			Savings savings = savingsService.depositFunds(userId, amount);
			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User savings not found.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Endpoint to withdraw funds from the savings account.
	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<?> withdrawFunds(@PathVariable Long userId, @RequestBody Map<String, BigDecimal> request) {
		try {
			BigDecimal amount = request.get("amount");
			validateAmount(amount, "Withdrawal amount must be greater than zero.");

			Savings savings = savingsService.withdrawFunds(userId, amount);
			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User savings not found.");
		} catch (InsufficientBalanceException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Helper method to validate deposit/withdrawal amounts.
	private void validateAmount(BigDecimal amount, String errorMessage) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	// Global exception handler for the controller.
	@ExceptionHandler({ ResourceNotFoundException.class, InsufficientBalanceException.class,
			IllegalArgumentException.class })
	public ResponseEntity<String> handleExceptions(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
