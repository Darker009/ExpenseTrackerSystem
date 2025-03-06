package org.darker.entity;

import jakarta.persistence.*;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_reports")
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private YearMonth month; // Stores year and month

    @Column(nullable = false)
    private double totalIncome;

    @Column(nullable = false)
    private double totalExpense;

    @Column(nullable = false)
    private double remainingBalance;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Constructor
    public MonthlyReport() {}

    public MonthlyReport(YearMonth month, double totalIncome, double totalExpense, double remainingBalance, User user) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.remainingBalance = remainingBalance;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public YearMonth getMonth() { return month; }
    public void setMonth(YearMonth month) { this.month = month; }

    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(double totalExpense) { this.totalExpense = totalExpense; }

    public double getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(double remainingBalance) { this.remainingBalance = remainingBalance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
