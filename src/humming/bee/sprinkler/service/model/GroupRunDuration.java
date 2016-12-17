package humming.bee.sprinkler.service.model;

public class GroupRunDuration {

	private int groupId;
	private long durationInSeconds;
	private int day;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public long getDurationInSeconds() {
		return durationInSeconds;
	}
	public void setDurationInSeconds(long durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	
}
