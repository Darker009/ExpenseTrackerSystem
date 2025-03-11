package org.darker.dto;

import org.darker.enums.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDTO {
	private Long id;
	private Long userId;
	private BigDecimal amount;
	private ExpenseCategory category;
	private LocalDate date;
	private BigDecimal remainingBalance;
	private String description;

	public ExpenseDTO(Long id, Long userId, BigDecimal amount, ExpenseCategory category, LocalDate date,
			BigDecimal remainingBalance, String description) {
		this.id = id;
		this.userId = userId;
		this.amount = amount;
		this.category = category;
		this.date = date;
		this.remainingBalance = remainingBalance;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public ExpenseCategory getCategory() {
		return category;
	}

	public LocalDate getDate() {
		return date;
	}

	public BigDecimal getRemainingBalance() {
		return remainingBalance;
	}

	public String getDescription() {
		return description;
	}
}
