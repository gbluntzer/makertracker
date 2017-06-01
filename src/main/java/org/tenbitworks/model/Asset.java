package org.tenbitworks.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="asset")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	String tenbitId;

	@NotNull
	String title;

	@Column(name = "description", nullable = false, length = 1000)
	String description;

	Date dateAcquired;
	Date dateRemoved;

	String brand;
	String modelNumber;
	String serialNumber;

	BigDecimal retailValue;

	String webLink;
	String operator;  //Member
	String donor;     //Member

	@Enumerated(EnumType.STRING)
	AssetStatus status;

	public Asset() { }

	public Asset(long id) {
		this.id = id;
	}

	public Asset(String title) {
		this.title = title;
	}

	public Asset(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public Asset(long id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}
}
