package org.darker.controller;

import org.darker.dto.SavingsDTO;
import org.darker.exception.InsufficientBalanceException;
import org.darker.exception.ResourceNotFoundException;
import org.darker.service.SavingsService;
import org.darker.entity.Savings;
import org.darker.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/savings")
public class SavingsController {
    private final SavingsService savingsService;

    public SavingsController(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    // ✅ Register Savings Account for a User
    @PostMapping("/register/{userId}")
    public ResponseEntity<SavingsDTO> registerSavings(@PathVariable Long userId, @RequestBody Savings savingsRequest) {
        try {
            // Fetch the user by userId
            User user = savingsService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
            }

            // Create and set the new savings account
            Savings savings = new Savings();
            savings.setUser(user);
            savings.setPhoneNumber(savingsRequest.getPhoneNumber());
            savings.setAccountNumber(savingsRequest.getAccountNumber());
            savings.setPanNumber(savingsRequest.getPanNumber());
            savings.setAadhaarNumber(savingsRequest.getAadhaarNumber());
            savings.setAddress(savingsRequest.getAddress());

            // Set the initial balance or default to zero if not provided
            BigDecimal initialBalance = savingsRequest.getTotalBalance() != null ? savingsRequest.getTotalBalance() : BigDecimal.ZERO;
            savings.setTotalBalance(initialBalance); // Set the total balance to the provided or default value
            savings.setRemainingBalance(initialBalance); // Set the remaining balance to the initial balance

            // Save the savings details
            Savings savedSavings = savingsService.saveSavings(savings);

            // Return the saved savings in response as SavingsDTO
            return ResponseEntity.status(HttpStatus.CREATED).body(new SavingsDTO(savedSavings));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle any other error
        }
    }

    // ✅ Get User Savings Details
    @GetMapping("/{userId}")
    public ResponseEntity<SavingsDTO> getUserSavings(@PathVariable Long userId) {
        try {
            Savings savings = savingsService.getUserSavings(userId);
            return ResponseEntity.ok(new SavingsDTO(savings)); // Convert to DTO before sending response
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User or savings not found
        }
    }

    // ✅ Deposit Funds into Savings Account
    @PostMapping("/{userId}/deposit")
    public ResponseEntity<SavingsDTO> depositFunds(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        validateAmount(amount, "Deposit amount must be greater than zero.");
        try {
            Savings savings = savingsService.depositFunds(userId, amount);
            return ResponseEntity.ok(new SavingsDTO(savings)); // Convert to DTO before sending response
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }
    }

    // ✅ Withdraw Funds from Savings Account
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<SavingsDTO> withdrawFunds(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        validateAmount(amount, "Withdrawal amount must be greater than zero.");
        try {
            Savings savings = savingsService.withdrawFunds(userId, amount);
            return ResponseEntity.ok(new SavingsDTO(savings)); // Convert to DTO before sending response
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Insufficient balance error
        }
    }

    // ✅ Helper Method: Validate Amount
    private void validateAmount(BigDecimal amount, String errorMessage) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ✅ Global Exception Handling
    @ExceptionHandler({ResourceNotFoundException.class, InsufficientBalanceException.class, IllegalArgumentException.class})
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
