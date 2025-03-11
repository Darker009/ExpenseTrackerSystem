package org.darker.dto;

import java.math.BigDecimal;

public class SavingsLimitedDTO {
	private Long id;
	private Long userId;
	private BigDecimal remainingBalance;

	public SavingsLimitedDTO() {
	}

	public SavingsLimitedDTO(Long id, Long userId, BigDecimal remainingBalance) {
		this.id = id;
		this.userId = userId;
		this.remainingBalance = remainingBalance;
	}

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

	public BigDecimal getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(BigDecimal remainingBalance) {
		this.remainingBalance = remainingBalance;
	}
}
