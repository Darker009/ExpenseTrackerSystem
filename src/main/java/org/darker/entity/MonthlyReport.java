package org.darker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "monthly_reports")
public class MonthlyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private LocalDate month;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal totalIncome;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal totalExpense;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal remainingBalance;

	public MonthlyReport() {
	}

	
	public MonthlyReport(User user, LocalDate month, BigDecimal totalIncome, BigDecimal totalExpense,
			BigDecimal remainingBalance) {
		this.user = user;
		this.month = month.withDayOfMonth(1); 
		this.totalIncome = totalIncome;
		this.totalExpense = totalExpense;
		this.remainingBalance = remainingBalance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getMonth() {
		return month;
	}

	public void setMonth(LocalDate month) {
		this.month = month.withDayOfMonth(1); 
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
}
