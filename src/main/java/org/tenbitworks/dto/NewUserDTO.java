package org.tenbitworks.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class NewUserDTO {

	@NotBlank
	private String username;
	
	@NotBlank
	private String plainPassword;
	
	private UUID memberId;
	
	@NotNull
	private List<String> roles;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPlainPassword() {
		return plainPassword;
	}
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public UUID getMemberId() {
		return memberId;
	}
	public void setMemberId(UUID memberId) {
		this.memberId = memberId;
	}
	public void setMemberId(String memberId) {
		if (memberId == null || memberId.trim().isEmpty()) {
			this.memberId = null;
		} else {
			this.memberId = UUID.fromString(memberId);
		}
	}
}
