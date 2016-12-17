/**
 * 
 */
package humming.bee.sprinkler.service.model;

import java.util.Date;

/**
 * 
 *
 */
public class SprinklerRunTime {
	
	private int id;
	private int sprinklerId;
	private Date startTime;
	private Date endTime;
	
	
	//getters and setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSprinklerId() {
		return sprinklerId;
	}
	public void setSprinklerId(int sprinklerId) {
		this.sprinklerId = sprinklerId;
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
