package org.tenbitworks.dto;


import java.util.List;
import java.util.UUID;

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
	
	private String memberName;
	private UUID memberId;

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

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public UUID getMemberId() {
		return memberId;
	}

	public void setMemberId(UUID memberId) {
		this.memberId = memberId;
	}
}