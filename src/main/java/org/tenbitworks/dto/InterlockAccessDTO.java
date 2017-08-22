package org.tenbitworks.dto;

public class InterlockAccessDTO {
	private String assetId;
	private boolean accessGranted;
	private long accessTimeMS;
	
	public boolean isAccessGranted() {
		return accessGranted;
	}
	public void setAccessGranted(boolean accessGranted) {
		this.accessGranted = accessGranted;
	}
	public long getAccessTimeMS() {
		return accessTimeMS;
	}
	public void setAccessTimeMS(long accessTime) {
		this.accessTimeMS = accessTime;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
}
