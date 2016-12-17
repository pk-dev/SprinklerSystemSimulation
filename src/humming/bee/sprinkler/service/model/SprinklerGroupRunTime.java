/**
 * 
 */
package humming.bee.sprinkler.service.model;

import java.util.Date;

/**
 * 
 *
 */
public class SprinklerGroupRunTime {
	
	private int id;
	private int groupId;
	private Date startTime;
	private Date endTime;
	
	
	//getters and setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	

}
