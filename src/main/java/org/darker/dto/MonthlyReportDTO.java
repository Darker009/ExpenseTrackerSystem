package org.darker.dto;

import java.time.YearMonth;

public class MonthlyReportDTO {
    private Long id;
    private Long userId;
    private YearMonth month;
    private double totalIncome;
    private double totalExpense;
    private double remainingBalance;

    public MonthlyReportDTO(Long id, Long userId, YearMonth month, double totalIncome, double totalExpense, double remainingBalance) {
        this.id = id;
        this.userId = userId;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.remainingBalance = remainingBalance;
    }
    public MonthlyReportDTO() {}
    
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

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}

	public double getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(double remainingBalance) {
		this.remainingBalance = remainingBalance;
	}

   
}
