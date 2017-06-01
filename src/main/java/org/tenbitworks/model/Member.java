package org.tenbitworks.model;


import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue
	private UUID id;

	@NotNull
	private String email;

	@NotNull
	private String memberName;

	@OneToOne
	@JoinColumn(name="username")
	private User user;

	@Enumerated(EnumType.STRING)
	private MemberStatus status;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	private String phoneNumber;
	private String description;
	private String zipCode;

	public Member() { }

	public Member(UUID id) { 
		this.id = id;
	}

	public Member(String uuidString) {
		this.id = UUID.fromString(uuidString);
	}

	public Member(String memberName, String email ) {
		this.email = email;
		this.memberName = memberName;
	}

}