package org.darker.entity;

import jakarta.persistence.*;
import org.darker.enums.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseCategory category;

	@Column(nullable = false, updatable = false)
	private LocalDate date;

	@Column(nullable = true)
	private String description;

	public Expense() {
	}

	public Expense(User user, BigDecimal amount, ExpenseCategory category, String description) {
		this.user = user;
		this.amount = amount;
		this.category = category;
		this.description = description;
		this.date = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
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

	public String getDescription() {
		return description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCategory(ExpenseCategory category) {
		this.category = category;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
