package org.tenbitworks.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="asset")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(unique = true)
	String tenbitId;

	@NotNull
	String title;

	@Column(name = "description", length = 1000)
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

	@Enumerated(EnumType.STRING)
	AssetStatus status;
	
	boolean trainingRequired;
	
	@ManyToMany
	List<Member> members;
	
	long accessControlTimeMS;
	
	//TODO Create list of maintainers or other people with extra access
	//TODO Create open access times or calendar based functions

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

	public long getAccessControlTimeMS() {
		return accessControlTimeMS;
	}

	public void setAccessControlTimeMS(long accessControlTimeMS) {
		this.accessControlTimeMS = accessControlTimeMS;
	}

	@Override
	public String toString() {
		return "Asset [id=" + id + ", tenbitId=" + tenbitId + ", title=" + title + ", description=" + description
				+ ", dateAcquired=" + dateAcquired + ", dateRemoved=" + dateRemoved + ", brand=" + brand
				+ ", modelNumber=" + modelNumber + ", serialNumber=" + serialNumber + ", retailValue=" + retailValue
				+ ", webLink=" + webLink + ", operator=" + operator + ", donor=" + donor + ", status=" + status
				+ ", trainingRequired=" + trainingRequired + 
				", accessControlTimeMS=" + accessControlTimeMS + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accessControlTimeMS ^ (accessControlTimeMS >>> 32));
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((dateAcquired == null) ? 0 : dateAcquired.hashCode());
		result = prime * result + ((dateRemoved == null) ? 0 : dateRemoved.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((donor == null) ? 0 : donor.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		result = prime * result + ((modelNumber == null) ? 0 : modelNumber.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((retailValue == null) ? 0 : retailValue.hashCode());
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tenbitId == null) ? 0 : tenbitId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + (trainingRequired ? 1231 : 1237);
		result = prime * result + ((webLink == null) ? 0 : webLink.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asset other = (Asset) obj;
		if (accessControlTimeMS != other.accessControlTimeMS)
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (dateAcquired == null) {
			if (other.dateAcquired != null)
				return false;
		} else if (!dateAcquired.equals(other.dateAcquired))
			return false;
		if (dateRemoved == null) {
			if (other.dateRemoved != null)
				return false;
		} else if (!dateRemoved.equals(other.dateRemoved))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (donor == null) {
			if (other.donor != null)
				return false;
		} else if (!donor.equals(other.donor))
			return false;
		if (id != other.id)
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		if (modelNumber == null) {
			if (other.modelNumber != null)
				return false;
		} else if (!modelNumber.equals(other.modelNumber))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (retailValue == null) {
			if (other.retailValue != null)
				return false;
		} else if (!retailValue.equals(other.retailValue))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (status != other.status)
			return false;
		if (tenbitId == null) {
			if (other.tenbitId != null)
				return false;
		} else if (!tenbitId.equals(other.tenbitId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (trainingRequired != other.trainingRequired)
			return false;
		if (webLink == null) {
			if (other.webLink != null)
				return false;
		} else if (!webLink.equals(other.webLink))
			return false;
		return true;
	}
	
	
}
