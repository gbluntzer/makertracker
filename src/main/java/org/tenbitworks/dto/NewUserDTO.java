package org.tenbitworks.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class NewUserDTO {

	@NotBlank
	private String username;
	
	@NotBlank
	private String plainPassword;
	
	private UUID memberId;
	
	@NotNull
	private List<String> roles;
	
	public void setMemberId(String memberId) {
		if (memberId == null || memberId.trim().isEmpty()) {
			this.memberId = null;
		} else {
			this.memberId = UUID.fromString(memberId);
		}
	}
}
