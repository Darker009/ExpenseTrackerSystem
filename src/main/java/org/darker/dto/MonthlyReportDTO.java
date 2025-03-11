package org.darker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class MonthlyReportDTO {
	private Long id;
	private Long userId;
	private LocalDate month;
	private BigDecimal totalIncome;
	private BigDecimal totalExpense;
	private BigDecimal remainingBalance;
	private Map<String, BigDecimal> expenseBreakdown;

	public MonthlyReportDTO() {
	}

	public MonthlyReportDTO(Long id, Long userId, LocalDate month, BigDecimal totalIncome, BigDecimal totalExpense,
			BigDecimal remainingBalance, Map<String, BigDecimal> expenseBreakdown) {
		this.id = id;
		this.userId = userId;
		this.month = month;
		this.totalIncome = totalIncome;
		this.totalExpense = totalExpense;
		this.remainingBalance = remainingBalance;
		this.expenseBreakdown = expenseBreakdown;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getMonth() {
		return month;
	}

	public void setMonth(LocalDate month) {
		this.month = month;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(BigDecimal totalExpense) {
		this.totalExpense = totalExpense;
	}

	public BigDecimal getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(BigDecimal remainingBalance) {
		this.remainingBalance = remainingBalance;
	}

	public Map<String, BigDecimal> getExpenseBreakdown() {
		return expenseBreakdown;
	}

	public void setExpenseBreakdown(Map<String, BigDecimal> expenseBreakdown) {
		this.expenseBreakdown = expenseBreakdown;
	}
}
