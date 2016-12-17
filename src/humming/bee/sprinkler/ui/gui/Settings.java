/**
 * 
 */
package humming.bee.sprinkler.ui.gui;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerStatus;
import humming.bee.sprinkler.ui.activation.TurnOffTimer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.print.attribute.standard.Severity;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.util.Timer;

/**
 * 
 *
 */
public class Settings extends JFrame {

	JFrame frame;
	Container contentPane;
	
	Schedule schedule;
	
	AddSetting addSetting;
	
	
	private JLabel lblStatus;
	
	private JButton btnOn;
	private JButton btnOff;
	private JCheckBox chkFunctional;
	private JButton btnSave;
	private JButton btnCancel;
	
	private JPanel schedulePane;
	
	JPanel settingPane;
	
	private SprinklerSelection selectSprinkler;
	private String selectedId;//to identify which sprinkler/group is selected in SprinklerSelection component.
	private boolean isSprinklerSelected=false;//to identify whether group or sprinkler is selected in SprinklerSelection component.
	
	SprinklerService sprinklerService=new SprinklerService();
	SprinklerGroup sprinklerGroup=new SprinklerGroup();
	
	
	private final int turnOffDelayMinutes=1;
	
	public Settings(String selectedId,boolean isSprinkler)
	{
		super("Humming Bee - Configuration");
		
		this.selectedId=selectedId;
		this.isSprinklerSelected=isSprinkler;
		
		setFrame();
		
	}

	/**
	 * Create the frame.
	 */
	public Settings() {
		
		super("Humming Bee - Configuration");
		
		setFrame();
				
	}
	
	/**
	 * set layout, size and create controls
	 */
	private void setFrame()
	{
		frame=this;//current frame
		setLayout(new BorderLayout());
		contentPane = getContentPane();
		
		//set default size and position for this frame		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int height = screenSize.height;
    	int width = screenSize.width;
    	setSize(width*3/4, height*3/4);
    	setMaximumSize(new Dimension(width*3/4, height*3/4));
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	
		setVisible(true);
		
		createControls();
	}
	
	private void createControls()
	{
		//add group and sprinkler dropdowns and show button
		selectSprinkler=new SprinklerSelection();
		//show button click
		selectSprinkler.setSelectionListener(new SprinklerSelectionListener() {
			
			@Override
			public void selectionChanged(String id,boolean isSprinkler) {
				// set selectedId and group/sprinkler flag to populate controls
				selectedId=id;
				isSprinklerSelected=isSprinkler;
				//get sprinkler/group details and populate controls
				
				if(isSprinklerSelected)//populate selected sprinkler details
				{
					
					//reload Schedule panel and update panel
					
					//repopulate week panel(JTables)
					//newWeek.refreshWeek();
					schedulePane.removeAll();
					schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
					schedulePane.repaint();
					
					//repopulate update panel
					populateSprinklerStatus();
					
				}
				else//populate selected group details
				{

					//reload schedule panel and update panel
					
					//schedule refresh
					schedulePane.removeAll();
					schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
					schedulePane.repaint();
					
					//repopulate update panel
					populateGroupStatus();
				}
				
			}
		});
		contentPane.add(selectSprinkler,BorderLayout.NORTH);
			
		//add the update setting panel
		createUpdatePanel();
		createSchedulePanel();
		
		
		//*************************************
		//set default selected group as North
		this.selectedId="North";
		this.isSprinklerSelected=false;
		//populate schedule panel and update panel accordingly
		schedulePane.removeAll();
		schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
		schedulePane.repaint();
		//repopulate update panel
		populateGroupStatus();
		//*************************************
		
		
	}
	
	/**
	 * Creates and adds schedule panel
	 * Schedule panel displays the schedule settings for selected sprinkler/group
	 */
	private void createSchedulePanel()
	{
		schedule=new Schedule(selectedId, isSprinklerSelected);
		schedulePane=new JPanel();
		schedulePane.setLayout(new FlowLayout(FlowLayout.CENTER));
		schedulePane.add(schedule);
		contentPane.add(new JScrollPane(schedulePane),BorderLayout.CENTER);//add to content pane
	}
	
	/**
	 * Creates and adds updatePane and addSettingPane
	 * updatePane shows: on/off status, functional/not-functional status and allows to change these settings
	 * addSettingPane allows to add new schedule setting for selected sprinkler/group
	 */
	private void createUpdatePanel()
	{
		settingPane=new JPanel();
		settingPane.setLayout(new BoxLayout(settingPane, BoxLayout.Y_AXIS));
		
		//************************update pane*****************************//
		JPanel updatePane=new JPanel();
		updatePane.setLayout(new FlowLayout(FlowLayout.CENTER,30,5));
		Border setBorder=BorderFactory.createTitledBorder("Settings");
		updatePane.setBorder(setBorder);
		
		lblStatus=new JLabel();
		
		btnOn=new JButton("ON");
		btnOff=new JButton("OFF");
		
		//On button click
		btnOn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				String message;
				if(isSprinklerSelected)//Sprinkler
				{
					message="Are you sure you want to turn on this sprinkler now?";
					//turn on the sprinkler for a fixed time
					int response = JOptionPane.showConfirmDialog(null, message, "Confirm",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(response==JOptionPane.YES_OPTION)
					{
						
						//update status=on to db
						sprinklerService.updateSprinklerStatusByUser(selectedId, SprinklerStatus.ON.toString(),true);
						
						int sprinklerId=sprinklerService.getSprinklerByName(selectedId).getSprinklerId();
						
						
						//set timer to turn off this sprinkler after a fixed time
						turnOffDelayTimer();
						
						//refresh update pane for sprinkler
						populateSprinklerStatus();
						
					}
				}
				else//group
				{
					message="Are you sure you want to turn on this sprinkler group now?";
					//turn on the sprinkler for fixed time
					int response = JOptionPane.showConfirmDialog(null, message, "Confirm",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(response==JOptionPane.YES_OPTION)
					{
						//update status=on to db
					    sprinklerService.updateSprinklerGroupStatus(selectedId, SprinklerStatus.ON.toString());
						//update status=on for all sprinklers in this group that does not have a sprinkler config for today
						List<Sprinkler> sprinklerList=new ArrayList<Sprinkler>();
						sprinklerList=sprinklerService.getSprinklersByGroup(selectedId);
						for(Sprinkler newSprinkler:sprinklerList)
						{//only if sprinkler is functional
							if(newSprinkler.isFunctional()){
							List<SprinklerConfiguration> sConfigList=sprinklerService.getSprinklerConfigurationByDay(newSprinkler.getSprinklerId(), getCurrentDay());
							if(sConfigList.isEmpty())//if yes, no sprinkler config found for today
							{
								//update sprinkler status to On
								sprinklerService.updateSprinklerStatusByUser(newSprinkler.getSprinklerName(), SprinklerStatus.ON.toString(), true);
							}
							}
						}
						//set timer to turn off this group after a fixed time
						
						
						turnOffDelayTimer();
						
						
						//refresh update pane for group
						populateGroupStatus();
						
					}
				}
				
				
				
			}
		});
		
		//Off button click
		btnOff.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// turn off the sprinkler/group
				
				String message;
				if(isSprinklerSelected)//Sprinkler
				{
					message="Are you sure you want to turn off this sprinkler now?";
					//turn on the sprinkler for 1 hour
					int response = JOptionPane.showConfirmDialog(null, message, "Confirm",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(response==JOptionPane.YES_OPTION)
					{
						//update status=off to db
						sprinklerService.updateSprinklerStatus(selectedId, SprinklerStatus.OFF.toString());
						//update sprinkler run time - end_time
						int sprinklerId=sprinklerService.getSprinklerByName(selectedId).getSprinklerId();
						System.out.println(sprinklerId);
						
						populateSprinklerStatus();
						
					}
				}
				else//group
				{
					message="Are you sure you want to turn off this sprinkler group now?";
					
					int response = JOptionPane.showConfirmDialog(null, message, "Confirm",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(response==JOptionPane.YES_OPTION)
					{
						//update status=off to db
						sprinklerService.updateSprinklerGroupStatus(selectedId, SprinklerStatus.OFF.toString());
          			   //update status to off to sprinklers under this group  without a sprinklerconfig for today
						List<Sprinkler> sprinklerList=new ArrayList<Sprinkler>();
						sprinklerList=sprinklerService.getSprinklersByGroup(selectedId);
						for(Sprinkler newSprinkler:sprinklerList)
						{//only if the sprinkler is functional
							if(newSprinkler.isFunctional()){
							List<SprinklerConfiguration> sConfigList=sprinklerService.getSprinklerConfigurationByDay(newSprinkler.getSprinklerId(), getCurrentDay());
							if(sConfigList.isEmpty())//if yes, no sprinkler config found for today
							{
								//update status to Off
								sprinklerService.updateSprinklerStatus(newSprinkler.getSprinklerName(), SprinklerStatus.OFF.toString());
							}
							}
						}
						
						
						
						//refresh update pane for group
						populateGroupStatus();
						
					}
				}
			}
		});
		
		chkFunctional=new JCheckBox("Functional");
		btnSave=new JButton("Save");
		//save button click - only for sprinklers
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isSprinklerSelected)//sprinkler functional data save
				{
					int response;
					int fn=(chkFunctional.isSelected())?1:0;//functional=1, not-functional=0
					//handle condition for chkFunctional 
					if(chkFunctional.isSelected())//made it functional
					{
						response = JOptionPane.showConfirmDialog(null, "Are you sure you want to set this sprinkler as Functional?", "Confirm",
						        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
						if(response==JOptionPane.YES_OPTION)
						{
							//save functional to db
							sprinklerService.updateSprinklerFunctional(selectedId, fn);
							//show addSetting and schedule panes
							addSetting.setVisible(true);
							//newWeek.setVisible(true);
							schedule.setVisible(true);
						}
						
					}
					else//made it not-functional
					{
						response = JOptionPane.showConfirmDialog(null, "Are you sure you want to set this sprinkler as Not Functional?", "Confirm",
						        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
						if(response==JOptionPane.YES_OPTION)
						{
							//save not functional and status=off to db
							sprinklerService.updateSprinklerFunctional(selectedId, fn);
							//disable addSetting and schedule panes
							addSetting.setVisible(false);
							//newWeek.setVisible(false);
							schedule.setVisible(false);
						}
						
					}
				}
				
				
			}
				
				
		});
	
		
		//group controls in panels
		JPanel textPane=new JPanel();
		textPane.add(lblStatus);
		JPanel statusPane=new JPanel();
		statusPane.add(btnOff);//statusPane.add(chkOff);
		statusPane.add(btnOn);//statusPane.add(chkOn);
		JPanel functionalPane=new JPanel();
		functionalPane.add(chkFunctional);
		
		
		//add controls to updatepanel
		updatePane.add(textPane);
		updatePane.add(statusPane);
		updatePane.add(functionalPane);
		//updatePane.add(btnCancel);
		updatePane.add(btnSave);
		//**************************************************************//
		
		//*********************addSetting pane**************************//
		addSetting=new AddSetting(selectedId,isSprinklerSelected);
		
		addSetting.setScheduleListener(new ScheduleListener() {
			
			@Override
			public void scheduleAdded() {
				// refresh Schedule panel
				System.out.println("3."+selectedId);
				schedulePane.removeAll();
				schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
				schedulePane.validate();
				schedulePane.repaint();
				
			}
		});
		
		//**************************************************************//
		
		//add components to setting pane
		settingPane.add(updatePane);	
		settingPane.add(addSetting);
		
		
		
		//add setting pane to content pane
		contentPane.add(settingPane,BorderLayout.SOUTH);
		
		
	}
	
	
	/**
	 * Populate update pane with sprinkler details
	 * 
	 */
	private void populateSprinklerStatus()
	{
		//get data
		Sprinkler newSprinkler=sprinklerService.getSprinklerByName(selectedId);
		
		chkFunctional.setVisible(true);
		btnSave.setVisible(true);
		//btnCancel.setVisible(true);
		
		if(newSprinkler.isFunctional())
		{
			chkFunctional.setSelected(true);//checks functional checkbox.
			addSetting=new AddSetting(selectedId, isSprinklerSelected);
			addSetting.setScheduleListener(new ScheduleListener() {
				
				@Override
				public void scheduleAdded() {
					// refresh Schedule panel
					System.out.println("1."+selectedId);
					schedulePane.removeAll();
					schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
					schedulePane.validate();
					schedulePane.repaint();
					
				}
			});
			//settingPane.removeAll();
			settingPane.remove(1);
			settingPane.add(addSetting, 1);
			addSetting.setVisible(true);
			//newWeek.setVisible(true);
			schedule.setVisible(true);
			
			if(newSprinkler.getSprinklerStatus().equals("OFF"))
			{
				//if sprinkler is off, show ON option 
				////chkOff.setSelected(true);
				btnOff.setVisible(false);//chkOff.setVisible(false);
				btnOn.setVisible(true);//chkOn.setSelected(false);
				//chkOn.setVisible(true);
				
				//show status as text
				lblStatus.setText("Status:OFF");
				//show 'off' icon 
				
				
			}
			else if(newSprinkler.getSprinklerStatus().equals("ON"))
			{
				//if sprinkler is on, show OFF option 
				btnOff.setVisible(true);
				btnOn.setVisible(false);
				
				//show status as text
				lblStatus.setText("Status:ON");
				//show 'on' icon 
			}
			
		}
		else//sprinkler is not functional
		{
			//uncheck functional checkbox
			chkFunctional.setSelected(false);
			
			lblStatus.setText("Status:OFF, Not Functional");
			//icon=not functional image
			
			//disable add settings/weekly schedule panel
			addSetting.setVisible(false);
			
			schedule.setVisible(false);
			//only the functional checkbox and save button will be accessible
			btnOff.setVisible(false);//chkOn.setVisible(false);
			btnOn.setVisible(false);//chkOff.setVisible(false);

		}
	}
	
	/**
	 * Populates updatePane with sprinkler group details
	 * 
	 */
	private void populateGroupStatus()
	{
		//System.out.println("inside populateGroupStatus");
		//get data
		SprinklerGroup newGroup=sprinklerService.getSprinklerGroupByName(selectedId);
		
		chkFunctional.setVisible(false);//No functional checkbox in this case.
		btnSave.setVisible(false);
		
		//initialize addSetting
		addSetting=new AddSetting(selectedId, isSprinklerSelected);
		addSetting.setScheduleListener(new ScheduleListener() {
			
			@Override
			public void scheduleAdded() {
				// refresh Schedule panel
				System.out.println("2."+selectedId);
				schedulePane.removeAll();
				schedulePane.add(new Schedule(selectedId, isSprinklerSelected));
				schedulePane.validate();
				schedulePane.repaint();
				
			}
		});
		settingPane.remove(1);
		settingPane.add(addSetting, 1);
		addSetting.setVisible(true);
		
		schedule.setVisible(true);
		//on/off button and status text management
		if(newGroup.getStatus().equals(SprinklerStatus.OFF.toString()))
		{
			//if group is off, show ON option 
			
			btnOff.setVisible(false);
			btnOn.setVisible(true);
			
			
			//show status as text
			lblStatus.setText("Status:OFF");
			//show 'off' icon 
			
			
		}
		else if(newGroup.getStatus().equals(SprinklerStatus.ON.toString()))
		{
			//if group is on, show OFF option 
			btnOff.setVisible(true);
			btnOn.setVisible(false);
			
			//show status as text
			lblStatus.setText("Status:ON");
			//show 'on' icon 
		}
	}
	
	/**
	 * Turn off sprinkler/group after a delay
	 * 
	 */
	private void turnOffDelayTimer()
	{
		long delay=60*1000;//1 minute
		Timer myTimer=new Timer();
		TurnOffTimer turnOff=new TurnOffTimer(isSprinklerSelected, selectedId);
		myTimer.schedule(turnOff,delay);
		
		
	}
	
	/**
	 * Gets current day
	 * @return
	 */
	private String getCurrentDay()
	{
		String[] DAYS = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday" };
		Calendar c1 = Calendar.getInstance();
		return DAYS[c1.get(Calendar.DAY_OF_WEEK) - 1];
		
	}

}
