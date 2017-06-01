package org.tenbitworks.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {
	private static final long serialVersionUID = -390489292624193795L;

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
}