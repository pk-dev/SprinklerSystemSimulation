/**
 * 
 */
package humming.bee.sprinkler.service.model;

import java.sql.Time;
import java.util.Date;

/**
 * 
 *
 */
public abstract class AbstractConfiguration {
	
	//Constructor
	public AbstractConfiguration()
	{
		
	}
	
	//data members
	private int id;
	private String dayOfWeek;
	private Time startTime;
	private Time endTime;
	private double volumeOfWater;
	
	
	//getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public double getVolumeOfWater() {
		return volumeOfWater;
	}
	public void setVolumeOfWater(double volumeOfWater) {
		this.volumeOfWater = volumeOfWater;
	}
	
	

}
