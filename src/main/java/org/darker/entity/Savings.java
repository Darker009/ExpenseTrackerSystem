package org.darker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings")
public class Savings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal totalBalance;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal remainingBalance;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
	private User user;

	@Column(length = 15, unique = true, nullable = false)
	private String phoneNumber;

	@Column(length = 20, unique = true, nullable = false)
	private String accountNumber;

	@Column(length = 10, unique = true, nullable = false)
	private String panNumber;

	@Column(length = 12, unique = true, nullable = false)
	private String aadhaarNumber;

	@Column(length = 255, nullable = false)
	private String address;

	@Column(nullable = false)
	private LocalDate date;

	public Savings() {
	}

	public Savings(BigDecimal totalBalance, BigDecimal remainingBalance, User user, String phoneNumber,
			String accountNumber, String panNumber, String aadhaarNumber, String address, LocalDate date) {
		this.totalBalance = totalBalance;
		this.remainingBalance = remainingBalance;
		this.user = user;
		this.phoneNumber = phoneNumber;
		this.accountNumber = accountNumber;
		this.panNumber = panNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.date = date;
	}

	public boolean withdrawFunds(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
		}
		if (remainingBalance.compareTo(amount) >= 0) {
			remainingBalance = remainingBalance.subtract(amount);
			return true;
		} else {
			return false;
		}
	}

	public void addFunds(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Deposit amount must be greater than zero.");
		}
		totalBalance = totalBalance.add(amount);
		remainingBalance = remainingBalance.add(amount);
	}

	public boolean deductExpenseAmount(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Expense amount must be greater than zero.");
		}
		if (remainingBalance.compareTo(amount) >= 0) {
			remainingBalance = remainingBalance.subtract(amount);
			return true;
		} else {
			return false;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public BigDecimal getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(BigDecimal remainingBalance) {
		this.remainingBalance = remainingBalance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
