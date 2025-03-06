package org.darker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "savings")
public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2) // Better precision for money
    private BigDecimal totalBalance = BigDecimal.ZERO; // Initial balance

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingBalance = BigDecimal.ZERO; // Available after spending

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Default Constructor
    public Savings() {}

    // Constructor with Parameters
    public Savings(BigDecimal totalBalance, BigDecimal remainingBalance, User user) {
        this.totalBalance = totalBalance;
        this.remainingBalance = remainingBalance;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getTotalBalance() { return totalBalance; }
    public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }

    public BigDecimal getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(BigDecimal remainingBalance) { this.remainingBalance = remainingBalance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Utility Methods for Depositing and Spending
    public void addFunds(BigDecimal amount) {
        this.totalBalance = this.totalBalance.add(amount);
        this.remainingBalance = this.remainingBalance.add(amount);
    }

    public boolean withdrawFunds(BigDecimal amount) {
        if (this.remainingBalance.compareTo(amount) >= 0) {
            this.remainingBalance = this.remainingBalance.subtract(amount);
            return true; // Withdrawal successful
        }
        return false; // Insufficient balance
    }
}
