package org.tenbitworks.dto;

import java.util.List;

public class InterlockAccessDTO {
	private long assetId;
	private List<String> rfidList;
	
	private boolean trainingRequired;
	
	private long accessTimeMS;
	
	public long getAccessTimeMS() {
		return accessTimeMS;
	}
	public void setAccessTimeMS(long accessTime) {
		this.accessTimeMS = accessTime;
	}
	public long getAssetId() {
		return assetId;
	}
	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	public boolean isTrainingRequired() {
		return trainingRequired;
	}
	public void setTrainingRequired(boolean trainingRequired) {
		this.trainingRequired = trainingRequired;
	}
	public List<String> getRfidList() {
		return rfidList;
	}
	public void setRfidList(List<String> rfid) {
		this.rfidList = rfid;
	}
}
