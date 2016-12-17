package humming.bee.sprinkler.service;

import java.util.List;

import humming.bee.sprinkler.db.SprinklerDb;
import humming.bee.sprinkler.service.model.GroupRunDuration;
import humming.bee.sprinkler.service.model.OverrideTemperatureConfiguration;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerGroupConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroupRunTime;
import humming.bee.sprinkler.service.model.SprinklerRunTime;


public class SprinklerService {

	private SprinklerDb dbService = new SprinklerDb();

	/*
	 * public List<Sprinkler> getStatus() {
	 * 
	 * return dbService.getAllStatus(); }
	 */

	public List<Sprinkler> getSprinkler() {
		return dbService.getSprinkler();
	}

	public List<SprinklerGroup> getGroup() {
		return dbService.getGroup();
	}

	public List<OverrideTemperatureConfiguration> temperatureConfiguration() {
		return dbService.getTemperatureConfiguration();
	}

	public void setTemperatureConfiguration(OverrideTemperatureConfiguration tempConfiguration) {
		dbService.setTemperatureConfiguration(tempConfiguration);
	}

	public List<GroupRunDuration> getGroupRunDuration() {
		return dbService.getGroupRunDuration();
	}

	public void updateSprinklerGroupStatusById(int groupId, String status) {
		dbService.updateSprinklerGroupStatusById(groupId, status);
	}
	
	/**
	 * Calls the method to Get all sprinklers by Group name
	 * 
	 * @param groupName
	 * @return List
	 */
	public List<Sprinkler> getSprinklersByGroup(String groupName) {
		return dbService.getSprinklersByGroup(groupName);

	}

	/**
	 * Calls the method to Get details of a sprinkler
	 * 
	 * @param sprinklerName
	 * @return Sprinkler
	 */
	public Sprinkler getSprinklerByName(String sprinklerName) {
		return dbService.getSprinklerByName(sprinklerName);
	}

	/**
	 * Calls the method to update sprinkler status to database
	 * 
	 * @param sprinklerName
	 * @param status
	 */
	public void updateSprinklerStatus(String sprinklerName, String status) {
		dbService.updateSprinklerStatus(sprinklerName, status);
	}

	/**
	 * calls method to Update sprinkler to functional/not-functional
	 * 
	 * @param sprinklerName
	 * @param functional
	 */
	public void updateSprinklerFunctional(String sprinklerName, int functional) {
		dbService.updateSprinklerFunctional(sprinklerName, functional);
	}

	/**
	 * update sprinkler status on user click
	 * 
	 * @param sprinklerName
	 * @param status
	 * @param byUserOn
	 */
	public void updateSprinklerStatusByUser(String sprinklerName, String status, boolean byUserOn) {
		dbService.updateSprinklerStatusByUser(sprinklerName, status, byUserOn);
	}
	
	public void updateSprinklerStatusById(int id, String status) {
		dbService.updateSprinklerStatusById(id, status);
	}
	
	/**
	 * Calls the method to Get sprinkler-group by group name
	 * @param groupName
	 * @param status
	 */
	public SprinklerGroup getSprinklerGroupByName(String groupName)
	{
		return dbService.getSprinklerGroupByName(groupName);
	}
	
	
	/**
	 * Calls method to Update status of a sprinkler-group (on/off) to database
	 * @param groupName
	 * @param status
	 */
	public void updateSprinklerGroupStatus(String groupName,String status)
	{
		dbService.updateSprinklerGroupStatus(groupName, status);
	}
	
	public void updateSprinklerGroupStatusOnly(int groupId, String status)
	{
		dbService.updateSprinklerGroupStatusOnly(groupId, status);
	}
	
	
	public void updateSprinklerGroupEvent(int month, int dayOfMonth, int groupId, long duration)
	{
		dbService.updateSprinklerGroupEvent(month, dayOfMonth, groupId, duration);
	}
	
	
	public List<OverrideTemperatureConfiguration> getTemperatureConfiguration()
	{
		return dbService.getTemperatureConfiguration();
	}
	

	// ***********************sprinkler configuration*******************

	public List<SprinklerConfiguration> getSprinklerConfigurationByDay(int sprinklerId, String day) {
		return dbService.getSprinklerConfigurationByDay(sprinklerId, day);
	}

	public int deleteSprinklerConfigById(int sConfigId) {
		return dbService.deleteSprinklerConfigById(sConfigId);
	}

	public boolean isSprinklerConfigExist(SprinklerConfiguration sConfig) {
		return dbService.isSprinklerConfigExist(sConfig);
	}
	

	public List<SprinklerConfiguration> getConfiguration(String sprinklerId) {
		// TODO Auto-generated method stub
		
		SprinklerDb dbService = new SprinklerDb();
		return dbService.getConfiguration(sprinklerId);
		
	}
	
	public int addSprinklerConfiguration(List<SprinklerConfiguration> newSprinklerConfigList)
	{
		int i=0;
		SprinklerDb dbService = new SprinklerDb();
		//add all values in list to db
		for(SprinklerConfiguration sConfig:newSprinklerConfigList)
		{
			i=dbService.addSprinklerConfiguration(sConfig);
		}
		return i;
	}
	
	

	// ******************************************************************

	// ***********************group configuration*******************

	public List<SprinklerGroupConfiguration> getGroupConfigurationByDay(int groupId, String day) {
		return dbService.getGroupConfigurationByDay(groupId, day);
	}

	public int deleteGroupConfigById(int sConfigId) {
		return dbService.deleteGroupConfigById(sConfigId);
	}

	public boolean isGroupConfigExist(SprinklerGroupConfiguration gConfig) {
		return dbService.isGroupConfigExist(gConfig);
	}
	

	
	public int addGroupConfiguration(List<SprinklerGroupConfiguration> newGroupConfigList)
	{
		int i=0;
		SprinklerDb dbService = new SprinklerDb();
		//add all values in list to db
		for(SprinklerGroupConfiguration gConfig:newGroupConfigList)
		{
			i=dbService.addGroupConfiguration(gConfig);
		}
		return i;
	}
	
	public List<SprinklerGroupConfiguration> getSprinklerGroupConfigurationByDay(String day) 
	{
		return dbService.getSprinklerGroupConfigurationByDay(day);
	}
	
	

	

	// **********************************************************************

	// *************************sprinkler run time************************

	public int addSprinklerRunTime(int sprinklerId) {
		return dbService.addSprinklerRunTime(sprinklerId);
	}

	public void updateSprinklerRunTime(int runTimeId) {
		dbService.updateSprinklerRunTime(runTimeId);
	}

	public SprinklerRunTime getSprinklerRunTimeToUpdate(int sprinklerId) {
		return dbService.getSprinklerRunTimeToUpdate(sprinklerId);
	}

	// ******************************************************************

	// ***********************sprinkler group run time*******************

	public int addGroupRunTime(int groupId) {
		return dbService.addGroupRunTime(groupId);
	}

	public void updateGroupRunTime(int gRunTimeId) {
		dbService.updateGroupRunTime(gRunTimeId);
	}

	public SprinklerGroupRunTime getGroupRunTimeToUpdate(int groupId) {
		return dbService.getGroupRunTimeToUpdate(groupId);
	}

}
