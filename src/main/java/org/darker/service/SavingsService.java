package org.darker.service;

import java.math.BigDecimal;
import org.darker.entity.Savings;
import org.darker.entity.User;
import org.darker.exception.InsufficientBalanceException;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.SavingsRepository;
import org.darker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SavingsService {
	private final SavingsRepository savingsRepository;
	private final UserRepository userRepository;

	public SavingsService(SavingsRepository savingsRepository, UserRepository userRepository) {
		this.savingsRepository = savingsRepository;
		this.userRepository = userRepository;
	}

	// Get user by ID
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	// Save savings account
	public Savings saveSavings(Savings savings) {
		return savingsRepository.save(savings);
	}

	// Get user savings account
	public Savings getUserSavings(Long userId) {
		User user = getUserById(userId);
		return savingsRepository.findByUser(user).orElseThrow(
				() -> new ResourceNotFoundException("Savings account not found for user with id: " + userId));
	}

	// Deposit funds
	@Transactional
	public Savings depositFunds(Long userId, BigDecimal amount) {
		Savings savings = getUserSavings(userId);
		savings.setRemainingBalance(savings.getRemainingBalance().add(amount));
		return savingsRepository.save(savings);
	}

	// Withdraw funds
	@Transactional
	public Savings withdrawFunds(Long userId, BigDecimal amount) {
		Savings savings = getUserSavings(userId);
		if (savings.getRemainingBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient balance to withdraw the amount of " + amount + ".");
		}
		savings.setRemainingBalance(savings.getRemainingBalance().subtract(amount));
		return savingsRepository.save(savings);
	}
}
