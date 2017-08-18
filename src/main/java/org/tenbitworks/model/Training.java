package org.tenbitworks.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="training")
public class Training {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	String title;

	@Column(name = "description", nullable = false, length = 1000)
	String description;

	@NotNull
	@ManyToOne
	Asset asset;

	@Transient
	String assetTitle;

	public Training() { }

	public Training(long id) {
		this.id = id;
	}

	public Training(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public Training(String title, String description, Asset asset) {
		this.title = title;
		this.description = description;
		this.asset = asset;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}
}
