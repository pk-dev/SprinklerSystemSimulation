/**
 * 
 */
package humming.bee.sprinkler.ui.gui;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.DayOfWeek;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerGroupConfiguration;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;
import javax.swing.text.DateFormatter;
import javax.swing.DropMode;

/**
 * 
 *
 */
public class AddSetting extends JPanel {

	//data members and controls
	
	private JTextField txtStartTime;
	private JTextField txtEndTime;
	private JList lstDays;
	private JButton btnAdd;
	
	private JLabel lblStatus;
	
	private JSpinner spnStartTime;
	private JSpinner spnEndTime;
	
	JCheckBox chkMon;
	JCheckBox chkTue;
	JCheckBox chkWed;
	JCheckBox chkThu;
	JCheckBox chkFri;
	JCheckBox chkSat;
	JCheckBox chkSun;
	
	SpinnerDateModel startModel = new SpinnerDateModel();
	SpinnerDateModel endModel = new SpinnerDateModel();
	
	private ScheduleListener scheduleListener;

	private String selectedId;
	
	private SprinklerService service=new SprinklerService();
	
	
	//getters and setters
	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	
	public void setScheduleListener(ScheduleListener scheduleListener) {
		this.scheduleListener = scheduleListener;
	}
	

	/**
	 * Constructor
	 * Create the panel.
	 */
	public AddSetting(String selectedId,boolean isSprinkler)
	{
		setSelectedId(selectedId);
		
		setLayout(new FlowLayout(FlowLayout.CENTER,30,5));
		Border border=BorderFactory.createTitledBorder("Add Configuration");
		setBorder(border);
		
		createTimeSelection();
		
		
		JPanel daysPane=new JPanel();
		daysPane.setLayout(new BoxLayout(daysPane, BoxLayout.Y_AXIS));
		daysPane.setAlignmentX(LEFT_ALIGNMENT);
		 chkMon=new JCheckBox("Monday");
		 chkTue=new JCheckBox("Tuesday");
		 chkWed=new JCheckBox("Wednesday");
		 chkThu=new JCheckBox("Thursday");
		 chkFri=new JCheckBox("Friday");
		 chkSat=new JCheckBox("Saturday");
		 chkSun=new JCheckBox("Sunday");
		 daysPane.add(chkMon);
		 daysPane.add(chkTue);
		 daysPane.add(chkWed);
		 daysPane.add(chkThu);
		 daysPane.add(chkFri);
		 daysPane.add(chkSat);
		 daysPane.add(chkSun);
		 
		
		
		btnAdd=new JButton("Add");
		//add button click
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//validate day selection - required
				List<DayOfWeek> daysSelected=getDaysSelected();
				if(daysSelected.size()==0)
				{
					//System.out.println("Please select the days to schedule");
					JOptionPane.showMessageDialog(null, "Please select the days to schedule.","Humming Bee - Error",JOptionPane.ERROR_MESSAGE);
				}
				else{
				//if ok:
				// validate start time and end time
				
				
				//SimpleDateFormat parser = new SimpleDateFormat("HH");
				SimpleDateFormat parser = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
				
				try {
					Date stDate=parser.parse(String.valueOf(spnStartTime.getValue()));
					Date enDate=parser.parse(spnEndTime.getValue().toString());
					Time st = new Time(stDate.getTime());
					Time en=new Time(enDate.getTime());
					
					
					if(en.after(st))
					{
						//ok, add schedule and refresh Week panel
						//System.out.println(daysSelected.size() + "days selected");
						if(isSprinkler){
							List<SprinklerConfiguration> sprinklerConfigList=new ArrayList<SprinklerConfiguration>();
							
							SprinklerService service=new SprinklerService();
							Sprinkler sp=service.getSprinklerByName(selectedId);
							
							//System.out.println(sp.getSprinklerId());
							
							SprinklerConfiguration sConfig=null;
							for(DayOfWeek day:daysSelected)
							{
								sConfig=new SprinklerConfiguration();
								sConfig.setSprinklerId(sp.getSprinklerId());
								sConfig.setDayOfWeek(day.toString());
								sConfig.setStartTime(new Time(st.getTime()));
								sConfig.setEndTime(new Time(en.getTime()));
								sprinklerConfigList.add(sConfig);
								
								
							}
							
							//add to db
							boolean addToDb=true;
							//first check whether any of this config already exist
							//if yes, show error
							for(SprinklerConfiguration config:sprinklerConfigList)
							{
								if(service.isSprinklerConfigExist(config)){
									JOptionPane.showMessageDialog(null, "One or more of the schedule you selected overlaps with existing schedule. "
											+ "Please re-enter new schedule and try again.","Humming Bee - Alert",JOptionPane.ERROR_MESSAGE);
									addToDb=false;
									break;
								}
							}
							
							if(addToDb){
								int status=service.addSprinklerConfiguration(sprinklerConfigList);
								if(status==1)//record successfully added
								{
									clearAllSelections();
									JOptionPane.showMessageDialog(null, "New schedule added successfully to sprinkler.","Humming Bee - Success",JOptionPane.INFORMATION_MESSAGE);
								
									//refresh Schedule panel
									scheduleListener.scheduleAdded();
								}
							}
							
							
							
							
						}
						else
						{
							
							List<SprinklerGroupConfiguration> groupConfigList=new ArrayList<SprinklerGroupConfiguration>();
							
							SprinklerGroup newGroup=service.getSprinklerGroupByName(selectedId);
							
							
							
							SprinklerGroupConfiguration gConfig=null;
							for(DayOfWeek day:daysSelected)
							{
								gConfig=new SprinklerGroupConfiguration();
								gConfig.setGroupId(newGroup.getGroupId());
								gConfig.setDayOfWeek(day.toString());
								gConfig.setStartTime(new Time(st.getTime()));
								gConfig.setEndTime(new Time(en.getTime()));
								groupConfigList.add(gConfig);
								
								
							}
							
							//add to db
							boolean addToDb=true;
							//first check whether any of this config already exist
							//if yes, show error
							for(SprinklerGroupConfiguration config:groupConfigList)
							{
								if(service.isGroupConfigExist(config)){
									JOptionPane.showMessageDialog(null, "One or more of the schedule you selected already exists. "
											+ "Please reenter new schedule and try again.","Humming Bee - Alert",JOptionPane.ERROR_MESSAGE);
									addToDb=false;
									break;
								}
							}
							
							
							//if no, add to db (ie, addToDb = true)
							if(addToDb)
							{
								int status=service.addGroupConfiguration(groupConfigList);
								if(status==1)//record successfully added
								{
									clearAllSelections();
									JOptionPane.showMessageDialog(null, "New schedule added successfully to group.","Humming Bee - Success",JOptionPane.INFORMATION_MESSAGE);
								
									//refresh Schedule panel
									scheduleListener.scheduleAdded();
								}
							}
							
						}
						
						
					}
					else//date validation error
					{
						//end should be > start - display validation error
						//System.out.println("End time should be later than start time.");
						JOptionPane.showMessageDialog(null, "End time must be later than start time.","Humming Bee - Error",JOptionPane.ERROR_MESSAGE);
						
					}
					
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				}
				
				
			}
		});
		
		
		JPanel startPane=new JPanel();
		startPane.add(new JLabel("Start time:"));
		startPane.add(spnStartTime);
		JPanel endPane=new JPanel();
		endPane.add(new JLabel("End time:"));
		endPane.add(spnEndTime);
		JPanel listPane=new JPanel();
		listPane.add(new JLabel("Days:"));
		listPane.add(daysPane);
		
		//add to layout
		add(startPane);
		add(endPane);
		add(listPane);
		add(btnAdd);
		
		
		
		
		
	}
	
	/**
	 * Creates time selection controls
	 */
	private void createTimeSelection()
	{
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24); 
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        //start time spinner
        //System.out.println(calendar.getTime());
        
        startModel.setValue(calendar.getTime());

        spnStartTime = new JSpinner(startModel);

        JSpinner.DateEditor statTimeEditor = new JSpinner.DateEditor(spnStartTime, "HH:mm");
        DateFormatter formatter1 = (DateFormatter)statTimeEditor.getTextField().getFormatter();
        formatter1.setAllowsInvalid(false); 
        formatter1.setOverwriteMode(true);

        spnStartTime.setEditor(statTimeEditor);
        
        
        //end time spinner
        
        endModel.setValue(calendar.getTime());
        
        spnEndTime = new JSpinner(endModel);

        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(spnEndTime, "HH:mm");
        DateFormatter formatter2 = (DateFormatter)endTimeEditor.getTextField().getFormatter();
        formatter2.setAllowsInvalid(false); 
        formatter2.setOverwriteMode(true);

        spnEndTime.setEditor(endTimeEditor);
	}
	
	
	
	private List<DayOfWeek> getDaysSelected()
	{
		List<DayOfWeek> daysSelected=new ArrayList<DayOfWeek>();
		
		if (chkMon.isSelected())
	         daysSelected.add(DayOfWeek.Monday);
		if(chkTue.isSelected())
		   daysSelected.add(DayOfWeek.Tuesday);
		if(chkWed.isSelected())
		   daysSelected.add(DayOfWeek.Wednesday);
		if(chkThu.isSelected())
		   daysSelected.add(DayOfWeek.Thursday);
		if(chkFri.isSelected())
		   daysSelected.add(DayOfWeek.Friday);
		if(chkSat.isSelected())
		   daysSelected.add(DayOfWeek.Saturday);
		if(chkSun.isSelected())
		   daysSelected.add(DayOfWeek.Sunday);
		
		return daysSelected;
		
	}
	
	
	/**
	 * Clear all add settings selection
	 */
	private void clearAllSelections()
	{
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24); 
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        startModel.setValue(calendar.getTime());
        endModel.setValue(calendar.getTime());
        
		spnStartTime.setModel(startModel);
		spnEndTime.setModel(endModel);
		
		chkMon.setSelected(false);
		chkTue.setSelected(false);
		chkWed.setSelected(false);
		chkThu.setSelected(false);
		chkFri.setSelected(false);
		chkSat.setSelected(false);
		chkSun.setSelected(false);
	}
	
	
	

}
