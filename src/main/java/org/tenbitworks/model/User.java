package org.tenbitworks.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@NotNull
	@Length(max=50)
	private String username;

	@NotNull
	@Length(max=500)
	@JsonIgnore
	private String password;

	@NotNull
	private boolean enabled;

}