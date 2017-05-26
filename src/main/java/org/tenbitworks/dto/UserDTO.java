package org.tenbitworks.dto;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.tenbitworks.model.User;

public class UserDTO {

	@NotNull
	@Length(max=50)
	private String username;
	
	@NotNull
	private boolean enabled;
	
	private List<String> roles;

	public UserDTO(User user, List<String> roles) {
		this.username = user.getUsername();
		this.enabled = user.isEnabled();
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}