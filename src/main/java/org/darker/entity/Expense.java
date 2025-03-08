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

    // Default constructor
    public Expense() {}

    // Constructor
    public Expense(User user, BigDecimal amount, ExpenseCategory category) {
        this.user = user;
        this.amount = amount;
        this.category = category;
        this.date = LocalDate.now(); // Automatically set the current date
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    // Method to update the user's savings after adding an expense
    public void deductFromSavings() {
        BigDecimal currentBalance = user.getSavings().getRemainingBalance(); // Get the current remaining balance
        BigDecimal updatedBalance = currentBalance.subtract(amount); // Subtract the expense amount
        user.getSavings().setRemainingBalance(updatedBalance); // Set the updated balance back to user's savings
    }
}
