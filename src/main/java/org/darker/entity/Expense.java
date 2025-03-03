package org.darker.entity;

import java.time.LocalDate;

import org.darker.enums.ExpenseCategory;

import jakarta.persistence.*;

@Entity
@Table(name ="expenses")
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseCategory category;
	
	@Column(nullable = false)
	private double amount;
	
	@Column(nullable = false)
	private LocalDate date;

	public Expense() {
		
	}

	public Expense(User user, ExpenseCategory category, double amount, LocalDate date) {
		super();
		this.user = user;
		this.category = category;
		this.amount = amount;
		this.date = date;
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

	public ExpenseCategory getCategory() {
		return category;
	}

	public void setCategory(ExpenseCategory category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	
}
