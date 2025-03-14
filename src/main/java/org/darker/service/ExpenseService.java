package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.ExpenseDTO;
import org.darker.entity.Expense;
import org.darker.entity.Savings;
import org.darker.entity.User;
import org.darker.enums.ExpenseCategory;
import org.darker.exception.ResourceNotFoundException;
import org.darker.exception.InsufficientBalanceException;
import org.darker.repository.ExpenseRepository;
import org.darker.repository.SavingsRepository;
import org.darker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final SavingsRepository savingsRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,
                          SavingsRepository savingsRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.savingsRepository = savingsRepository;
    }

    @Transactional
    public ExpenseDTO addExpense(Long userId, Expense expense) {
        // Retrieve the user; if not found, throw an exception.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Retrieve the user's savings account; if not present, throw an exception.
        Savings savings = savingsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Savings account not found. Please create one first."));

        // Attempt to withdraw the expense amount from the savings account.
        boolean isDeducted = savings.withdrawFunds(expense.getAmount());
        if (!isDeducted) {
            throw new InsufficientBalanceException("Insufficient funds to cover the expense.");
        }

        // Save the expense record.
        Expense newExpense = new Expense(user, expense.getAmount(), expense.getCategory(), expense.getDescription());
        newExpense = expenseRepository.save(newExpense);

        // Optionally persist the updated savings if not auto-updated.
        savingsRepository.save(savings);

        // Return a DTO containing expense details and updated remaining balance.
        return new ExpenseDTO(
                newExpense.getId(),
                user.getId(),
                newExpense.getAmount(),
                newExpense.getCategory(),
                newExpense.getDate(),
                savings.getRemainingBalance(),
                newExpense.getDescription()
        );
    }

    public List<ExpenseDTO> getUserExpenses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Savings savings = savingsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Savings account not found. Please create one first."));

        return expenseRepository.findByUser(user).stream()
                .map(expense -> new ExpenseDTO(
                        expense.getId(),
                        user.getId(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDate(),
                        savings.getRemainingBalance(),
                        expense.getDescription()))
                .collect(Collectors.toList());
    }

    public List<ExpenseDTO> getUserExpensesByCategory(Long userId, ExpenseCategory category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Savings savings = savingsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Savings account not found. Please create one first."));

        return expenseRepository.findByUserAndCategory(user, category).stream()
                .map(expense -> new ExpenseDTO(
                        expense.getId(),
                        user.getId(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDate(),
                        savings.getRemainingBalance(),
                        expense.getDescription()))
                .collect(Collectors.toList());
    }

    public List<ExpenseDTO> getUserExpensesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Savings savings = savingsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Savings account not found. Please create one first."));

        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate).stream()
                .map(expense -> new ExpenseDTO(
                        expense.getId(),
                        user.getId(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDate(),
                        savings.getRemainingBalance(),
                        expense.getDescription()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        Savings savings = savingsRepository.findByUser(expense.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Savings not found"));

        // Add back the expense amount to the savings as a balance update.
        savings.addFunds(expense.getAmount());
        savingsRepository.save(savings);

        expenseRepository.deleteById(id);
    }
}
