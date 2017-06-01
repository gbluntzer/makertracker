package org.tenbitworks.dto;


import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.tenbitworks.model.User;

import lombok.Data;

@Data
public class UserDTO {

	@NotNull
	@Length(max=50)
	private String username;
	
	@NotNull
	private boolean enabled;
	
	private List<String> roles;
	
	private String memberName;
	private UUID memberId;

	public UserDTO(User user, List<String> roles) {
		this.username = user.getUsername();
		this.enabled = user.isEnabled();
		this.roles = roles;
	}
}