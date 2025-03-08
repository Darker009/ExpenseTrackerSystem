package org.darker.service;

import java.math.BigDecimal;

import org.darker.entity.Savings;
import org.darker.entity.User;
import org.darker.exception.InsufficientBalanceException;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.SavingsRepository;
import org.darker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SavingsService {
    private final SavingsRepository savingsRepository;
    private final UserRepository userRepository;

    public SavingsService(SavingsRepository savingsRepository, UserRepository userRepository) {
        this.savingsRepository = savingsRepository;
        this.userRepository = userRepository;
    }

    // ✅ Get User by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // ✅ Save Savings
    public Savings saveSavings(Savings savings) {
        return savingsRepository.save(savings);  // Saves the savings account details
    }

    // ✅ Get User Savings Details
    public Savings getUserSavings(Long userId) {
        // Fetch user savings account by userId
        return savingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Savings account not found for user with id: " + userId));
    }

    // ✅ Deposit Funds
    public Savings depositFunds(Long userId, BigDecimal amount) {
        // Get the user's savings
        Savings savings = getUserSavings(userId);
        
        // Add funds to savings
        savings.addFunds(amount);  // Assuming addFunds is a method in Savings entity to handle deposit

        // Save and return the updated savings
        return savingsRepository.save(savings);
    }

    // ✅ Withdraw Funds
    public Savings withdrawFunds(Long userId, BigDecimal amount) {
        // Get the user's savings
        Savings savings = getUserSavings(userId);

        // Ensure sufficient funds before withdrawing
        if (!savings.withdrawFunds(amount)) {
            throw new InsufficientBalanceException("Insufficient balance to withdraw the amount of " + amount + ".");
        }

        // Save and return the updated savings
        return savingsRepository.save(savings);
    }
}
