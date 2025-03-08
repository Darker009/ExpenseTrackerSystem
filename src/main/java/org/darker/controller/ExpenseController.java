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

	private boolean userHasSavingsAccount(Long userId) {
		return savingsService.getUserSavings(userId) != null;
	}

	@PostMapping("/{userId}")
	public ResponseEntity<ExpenseDTO> addExpense(@PathVariable Long userId, @RequestBody Expense expense) {
		if (!userHasSavingsAccount(userId)) {
			return ResponseEntity.badRequest().body(new ExpenseDTO(null, userId, null, null, null, null));
		}
		ExpenseDTO dto = expenseService.addExpense(userId, expense);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<ExpenseDTO>> getUserExpenses(@PathVariable Long userId) {
		if (!userHasSavingsAccount(userId)) {
			return ResponseEntity.badRequest().body(List.of());
		}
		List<ExpenseDTO> expenses = expenseService.getUserExpenses(userId);
		return ResponseEntity.ok(expenses);
	}

	@GetMapping("/{userId}/category/{category}")
	public ResponseEntity<List<ExpenseDTO>> getUserExpensesByCategory(@PathVariable Long userId,
			@PathVariable ExpenseCategory category) {
		if (!userHasSavingsAccount(userId)) {
			return ResponseEntity.badRequest().body(List.of());
		}
		List<ExpenseDTO> expenses = expenseService.getUserExpensesByCategory(userId, category);
		return ResponseEntity.ok(expenses);
	}

	@GetMapping("/{userId}/date")
	public ResponseEntity<List<ExpenseDTO>> getUserExpensesByDateRange(@PathVariable Long userId,
			@RequestParam String startDate, @RequestParam String endDate) {
		if (!userHasSavingsAccount(userId)) {
			return ResponseEntity.badRequest().body(List.of());
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
