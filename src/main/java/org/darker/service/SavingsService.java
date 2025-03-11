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

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	public Savings saveSavings(Savings savings) {
		return savingsRepository.save(savings);
	}

	public Savings getUserSavings(Long userId) {
		User user = getUserById(userId);
		return savingsRepository.findByUser(user).orElseThrow(
				() -> new ResourceNotFoundException("Savings account not found for user with id: " + userId));
	}

	public Savings depositFunds(Long userId, BigDecimal amount) {
		Savings savings = getUserSavings(userId);
		savings.addFunds(amount);
		return savingsRepository.save(savings);
	}

	public Savings withdrawFunds(Long userId, BigDecimal amount) {
		Savings savings = getUserSavings(userId);
		if (!savings.withdrawFunds(amount)) {
			throw new InsufficientBalanceException("Insufficient balance to withdraw the amount of " + amount + ".");
		}
		return savingsRepository.save(savings);
	}
}
