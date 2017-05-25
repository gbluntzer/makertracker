package org.tenbitworks.dto;

import java.util.List;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.tenbitworks.model.Member;

public class NewUser {

	@NotBlank
	private String username;
	
	@NotBlank
	private String plainPassword;
	
	@NotNull
	@OneToOne
	private Member member;
	
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
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "NewUser [username=" + username + ", member=" + member + ", roles=" + roles + "]";
	}
}
