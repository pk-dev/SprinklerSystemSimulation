package humming.bee.sprinkler.service.model;

import java.util.Date;

public class Sprinkler {

	private int sprinklerId;
	private String sprinklerName;
	private int groupId;
	private String sprinklerStatus;
	private double volumeOfWater;
	private boolean functional;
	private long duration;
	private Date lastUpdateTime;
	private boolean byUser;

	public int getSprinklerId() {
		return sprinklerId;
	}

	public void setSprinklerId(int sprinklerId) {
		this.sprinklerId = sprinklerId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getSprinklerStatus() {
		return sprinklerStatus;
	}

	public void setSprinklerStatus(String sprinklerStatus) {
		this.sprinklerStatus = sprinklerStatus;
	}

	public double getVolumeOfWater() {
		return volumeOfWater;
	}

	public void setVolumeOfWater(double volumeOfWater) {
		this.volumeOfWater = volumeOfWater;
	}

	public String getSprinklerName() {
		return sprinklerName;
	}

	public void setSprinklerName(String sprinklerName) {
		this.sprinklerName = sprinklerName;
	}
	
	public boolean isFunctional()
	{
		return functional;
	}
	
	public void setFunctional(boolean functional) {
		this.functional = functional;
	}
	
	public boolean isByUser() {
		return byUser;
	}

	public void setByUser(boolean byUser) {
		this.byUser = byUser;
	}
	

	@Override
	public String toString() {
		return "Sprinkler [sprinklerId=" + sprinklerId + ", sprinklerName=" + sprinklerName + ", groupId=" + groupId
				+ ", sprinklerStatus=" + sprinklerStatus + ", volumeOfWater=" + volumeOfWater +", functional=" + functional+ "\n"+ "]";
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
