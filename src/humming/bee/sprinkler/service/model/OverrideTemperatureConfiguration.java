package humming.bee.sprinkler.service.model;

public class OverrideTemperatureConfiguration {

	int id;
	Integer tempUpperLimit;
	Integer tempLowerLimit;

	int groupId;

	public Integer getTempUpperLimit() {
		return tempUpperLimit;
	}

	public void setTempUpperLimit(Integer tempUpperLimit) {
		this.tempUpperLimit = tempUpperLimit;
	}

	public Integer getTempLowerLimit() {
		return tempLowerLimit;
	}

	public void setTempLowerLimit(Integer tempLowerLimit) {
		this.tempLowerLimit = tempLowerLimit;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
