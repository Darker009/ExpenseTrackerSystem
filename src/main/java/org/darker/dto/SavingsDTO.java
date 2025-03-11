package org.darker.dto;

import org.darker.entity.Savings;
import java.math.BigDecimal;

public class SavingsDTO {
	private Long id;
	private BigDecimal totalBalance;
	private BigDecimal remainingBalance;
	private Long userId;
	private String aadhaarNumber;
	private String panNumber;
	private String accountNumber;
	private String phoneNumber;
	private String address;

	public SavingsDTO() {
	}

	public SavingsDTO(Savings savings) {
		this.id = savings.getId();
		this.totalBalance = savings.getTotalBalance();
		this.remainingBalance = savings.getRemainingBalance();
		this.userId = savings.getUser().getId();
		this.aadhaarNumber = savings.getAadhaarNumber();
		this.panNumber = savings.getPanNumber();
		this.accountNumber = savings.getAccountNumber();
		this.phoneNumber = savings.getPhoneNumber();
		this.address = savings.getAddress();
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
