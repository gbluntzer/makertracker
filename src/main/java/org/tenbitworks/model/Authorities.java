package org.tenbitworks.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {
	private static final long serialVersionUID = 6008418255662269697L;

	@Id
	@NotNull
	@Length(max=50)
	@ManyToOne
	@JoinColumn(name="username")
	private User user;

	@Id
	@NotNull
	@Length(max=50)
	private String authority;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}