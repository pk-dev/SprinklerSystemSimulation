/**
 * 
 */
package humming.bee.sprinkler.ui.activation;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

/**
 *
 *
 */
public class TurnOffTimer extends TimerTask {

	
	private boolean isSprinkler=false;
	
	private String selectedId;
	
	
	/**
	 * 
	 */
	public TurnOffTimer(boolean isSprinkler,String selectedId) {
		// TODO Auto-generated constructor stub
		this.isSprinkler=isSprinkler;
		
		this.selectedId=selectedId;
	}
	
	/**
	 * @see java.util.TimerTask#run()
	 * 
	 * 
	 * 
	 */
	@Override
	 public void run()
    {
  	  
  	  //if sprinkler,
  	  if(isSprinkler)
  	  { 
  		  SprinklerService sprinklerService=new SprinklerService();
      	  //turn off sprinkler after a fixed time
    			System.out.println("sprinkler "+selectedId+" turning off.");
    			//set status=off if sprinkler is ON
      	  if(sprinklerService.getSprinklerByName(selectedId).getSprinklerStatus().equals(SprinklerStatus.ON.toString()))
      	    sprinklerService.updateSprinklerStatus(selectedId, SprinklerStatus.OFF.toString());
    			
  	  }
  	  else//group
  	  {
  		  
  		 //turn off group after a fixed time
  		  System.out.println("group "+selectedId+" turning off.");
  		  //set status=off if group is ON
  		SprinklerService sprinklerService=new SprinklerService();
  		  if(sprinklerService.getSprinklerGroupByName(selectedId).getStatus().equals(SprinklerStatus.ON.toString()))
  		  {
  			
  			sprinklerService.updateSprinklerGroupStatus(selectedId, SprinklerStatus.OFF.toString());
  			  //update status to off to sprinklers under this group  without a sprinklerconfig for today
				List<Sprinkler> sprinklerList=new ArrayList<Sprinkler>();
				sprinklerList=sprinklerService.getSprinklersByGroup(selectedId);
				for(Sprinkler newSprinkler:sprinklerList)
				{
					if(newSprinkler.isFunctional()){
					List<SprinklerConfiguration> sConfigList=sprinklerService.getSprinklerConfigurationByDay(newSprinkler.getSprinklerId(), getCurrentDay());
					if(sConfigList.isEmpty())//if yes, no sprinkler config found for today
					{
						//update status to Off
						sprinklerService.updateSprinklerStatus(newSprinkler.getSprinklerName(), SprinklerStatus.OFF.toString());
					}
					}
				}
  			  
  		  }
  		  
  	  }
  	  
    }
	
	private String getCurrentDay()
	{
		String[] DAYS = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday" };
		Calendar c1 = Calendar.getInstance();
		return DAYS[c1.get(Calendar.DAY_OF_WEEK) - 1];
		
	}
	
	

}
