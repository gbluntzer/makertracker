package org.tenbitworks.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.tenbitworks.model.Asset;
import org.tenbitworks.model.AssetStatus;
import org.tenbitworks.model.Member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AssetDTO {

	private long id;

	@NotNull
	String tenbitId;

	@NotNull
	String title;

	String description;

	Date dateAcquired;
	Date dateRemoved;

	String brand;
	String modelNumber;
	String serialNumber;

	BigDecimal retailValue;

	String webLink;
	String operator;
	String donor;

	AssetStatus status;
	
	boolean trainingRequired;
	
	List<Member> members;
	List<String> memberNames;
	
	long accessControlTimeMS;
	
	public AssetDTO() { }

	public AssetDTO(Asset asset) {
		this.id = asset.getId();
		this.tenbitId = asset.getTenbitId();
		this.title = asset.getTitle();
		this.description = asset.getDescription();
		this.dateAcquired = asset.getDateAcquired();
		this.dateRemoved = asset.getDateRemoved();

		this.brand = asset.getBrand();
		this.modelNumber = asset.getModelNumber();
		this.serialNumber = asset.getSerialNumber();

		this.retailValue = asset.getRetailValue();

		this.webLink = asset.getWebLink();
		this.operator = asset.getOperator();
		this.donor = asset.getDonor();

		this.status = asset.getStatus();
		
		this.trainingRequired = asset.isTrainingRequired();
		
		this.members = asset.getMembers();
		
		this.memberNames = new ArrayList<>();
		this.members.forEach((member) -> {
			memberNames.add(member.getMemberName());
		});
		
		this.accessControlTimeMS = asset.getAccessControlTimeMS();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTenbitId() {
		return tenbitId;
	}

	public void setTenbitId(String tenbitId) {
		this.tenbitId = tenbitId;
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

	public Date getDateAcquired() {
		return dateAcquired;
	}

	public void setDateAcquired(Date dateAcquired) {
		this.dateAcquired = dateAcquired;
	}

	public Date getDateRemoved() {
		return dateRemoved;
	}

	public void setDateRemoved(Date dateRemoved) {
		this.dateRemoved = dateRemoved;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public BigDecimal getRetailValue() {
		return retailValue;
	}

	public void setRetailValue(BigDecimal retailValue) {
		this.retailValue = retailValue;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDonor() {
		return donor;
	}

	public void setDonor(String donor) {
		this.donor = donor;
	}

	public AssetStatus getStatus() {
		return status;
	}

	public void setStatus(AssetStatus status) {
		this.status = status;
	}

	public boolean isTrainingRequired() {
		return trainingRequired;
	}

	public void setTrainingRequired(boolean trainingRequired) {
		this.trainingRequired = trainingRequired;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
	public void addMember(Member member) {
		if (this.members == null) {
			members = new ArrayList<>();
		}
		this.members.add(member);
	}

	public List<String> getMemberNames() {
		return memberNames;
	}

	public void setMemberNames(List<String> memberNames) {
		this.memberNames = memberNames;
	}

	public long getAccessControlTimeMS() {
		return accessControlTimeMS;
	}

	public void setAccessControlTimeMS(long accessControlTimeMS) {
		this.accessControlTimeMS = accessControlTimeMS;
	}
}
