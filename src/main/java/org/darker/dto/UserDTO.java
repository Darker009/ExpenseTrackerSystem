package org.darker.dto;

import java.time.LocalDateTime;

public class UserDTO {
	private String name;
	private String email;
	private LocalDateTime registeredAt;

	public UserDTO() {
	}

	public UserDTO(String name, String email, LocalDateTime registeredAt) {
		this.name = name;
		this.email = email;
		this.registeredAt = registeredAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "name='" + name + '\'' + ", email='" + email + '\'' + ", registeredAt=" + registeredAt
				+ '}';
	}

}
