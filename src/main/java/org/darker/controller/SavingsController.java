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

	//user can create saving account with(aadharNo, panNo, address, balance, accountNo, phoneNo etc.)
	@PostMapping("/register/{userId}")
	public ResponseEntity<SavingsDTO> registerSavings(@PathVariable Long userId,
			@RequestBody SavingsDTO savingsRequest) {
		try {
			User user = savingsService.getUserById(userId);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	//user can check his balance.
	@GetMapping("/{userId}")
	public ResponseEntity<SavingsLimitedDTO> getUserSavings(@PathVariable Long userId) {
		try {
			Savings savings = savingsService.getUserSavings(userId);
			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	//user can deposit cash 
	@PostMapping("/{userId}/deposit")
	public ResponseEntity<SavingsLimitedDTO> depositFunds(@PathVariable Long userId,
			@RequestBody Map<String, BigDecimal> request) {
		BigDecimal amount = request.get("amount");
		validateAmount(amount, "Deposit amount must be greater than zero.");
		try {
			Savings savings = savingsService.depositFunds(userId, amount);

			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	//user can withdraw cash 
	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<SavingsLimitedDTO> withdrawFunds(@PathVariable Long userId,
			@RequestBody Map<String, BigDecimal> request) {
		BigDecimal amount = request.get("amount");
		validateAmount(amount, "Withdrawal amount must be greater than zero.");
		try {
			Savings savings = savingsService.withdrawFunds(userId, amount);
			SavingsLimitedDTO response = new SavingsLimitedDTO(savings.getId(), savings.getUser().getId(),
					savings.getRemainingBalance());

			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (InsufficientBalanceException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	//method which is responsible for checking amount related conditions.
	private void validateAmount(BigDecimal amount, String errorMessage) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	@ExceptionHandler({ ResourceNotFoundException.class, InsufficientBalanceException.class,
			IllegalArgumentException.class })
	public ResponseEntity<String> handleExceptions(Exception e) {
		if (e instanceof ResourceNotFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		if (e instanceof InsufficientBalanceException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
