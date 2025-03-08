package org.darker.controller;

import org.darker.dto.ExpenseDTO;
import org.darker.entity.Expense;
import org.darker.enums.ExpenseCategory;
import org.darker.exception.ResourceNotFoundException;
import org.darker.service.ExpenseService;
import org.darker.service.SavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final SavingsService savingsService;

    public ExpenseController(ExpenseService expenseService, SavingsService savingsService) {
        this.expenseService = expenseService;
        this.savingsService = savingsService;
    }

    // ✅ Check if the user has a savings account
    private boolean userHasSavingsAccount(Long userId) {
        return savingsService.getUserSavings(userId) != null; // Assuming this method returns the user's savings account or null if not found
    }

    // ✅ Add Expense
    @PostMapping("/{userId}")
    public ResponseEntity<ExpenseDTO> addExpense(@PathVariable Long userId, @RequestBody Expense expense) {
        if (!userHasSavingsAccount(userId)) {
            // Return an empty ExpenseDTO with a message or an appropriate response indicating the issue
            return ResponseEntity.badRequest().body(new ExpenseDTO(null, userId, null, null, null, null)); // Placeholder, handle as needed
        }
        ExpenseDTO dto = expenseService.addExpense(userId, expense);
        return ResponseEntity.ok(dto);
    }

    // ✅ Get All Expenses for a User
    @GetMapping("/{userId}")
    public ResponseEntity<List<ExpenseDTO>> getUserExpenses(@PathVariable Long userId) {
        if (!userHasSavingsAccount(userId)) {
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list or some indication
        }
        List<ExpenseDTO> expenses = expenseService.getUserExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    // ✅ Get Expenses by Category
    @GetMapping("/{userId}/category/{category}")
    public ResponseEntity<List<ExpenseDTO>> getUserExpensesByCategory(
            @PathVariable Long userId, @PathVariable ExpenseCategory category) {
        if (!userHasSavingsAccount(userId)) {
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list or some indication
        }
        List<ExpenseDTO> expenses = expenseService.getUserExpensesByCategory(userId, category);
        return ResponseEntity.ok(expenses);
    }

    // ✅ Get Expenses by Date Range
    @GetMapping("/{userId}/date")
    public ResponseEntity<List<ExpenseDTO>> getUserExpensesByDateRange(
            @PathVariable Long userId, @RequestParam String startDate, @RequestParam String endDate) {
        if (!userHasSavingsAccount(userId)) {
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list or some indication
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ExpenseDTO> expenses = expenseService.getUserExpensesByDateRange(userId, start, end);
            return ResponseEntity.ok(expenses);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // ✅ Delete an Expense
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        try {
            expenseService.deleteExpense(expenseId);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("Expense not found");
        }
    }
}
