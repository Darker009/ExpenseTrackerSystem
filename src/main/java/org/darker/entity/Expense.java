package org.darker.entity;

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
}
