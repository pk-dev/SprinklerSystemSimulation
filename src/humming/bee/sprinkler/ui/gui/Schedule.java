/**
 * 
 */
package humming.bee.sprinkler.ui.gui;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.DayOfWeek;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerGroupConfiguration;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import java.awt.Font;

/**
 * 
 *
 */
public class Schedule extends JPanel {

	
	JPanel outerPane;
	
	JPanel paneMon;
	JPanel paneTue;
	JPanel paneWed;
	JPanel paneThu;
	JPanel paneFri;
	JPanel paneSat;
	JPanel paneSun;
	
	SprinklerService service=new SprinklerService();
	
	private ActionListener menuItemListener;
	
	private String selectedId;
	private boolean isSprinkler;
	
	/**
	 * Create the panel.
	 */
	public Schedule(String selectedId, boolean isSprinkler) {
		
		this.selectedId=selectedId;
		this.isSprinkler=isSprinkler;
		
		menuItemListener=new MenuItemListener();
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int height = screenSize.height;
    	int width = screenSize.width;
		
		//outer panel
		outerPane=new JPanel();
		outerPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		outerPane.setLayout(new GridLayout(7, 1));
		//outerPane.setPreferredSize(new Dimension(750, 250));
		outerPane.setPreferredSize(new Dimension(width*2/3, 220));
		
		
		//inner panels
		
		JLabel lblMon=new JLabel("Monday");
		lblMon.setOpaque(true);
		lblMon.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblMon.setBackground(new Color(100, 149, 237));
		lblMon.setPreferredSize(new Dimension(85, 20));
		JLabel lblTue=new JLabel("Tuesday");
		lblTue.setOpaque(true);
		lblTue.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblTue.setBackground(new Color(100, 149, 237));
		lblTue.setPreferredSize(new Dimension(85, 20));
		JLabel lblWed=new JLabel("Wednesday");
		lblWed.setBackground(new Color(100, 149, 237));
		lblWed.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblWed.setOpaque(true);
		lblWed.setPreferredSize(new Dimension(85, 20));
		JLabel lblThu=new JLabel("Thursday");
		lblThu.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblThu.setOpaque(true);
		lblThu.setBackground(new Color(100, 149, 237));
		lblThu.setPreferredSize(new Dimension(85, 20));
		JLabel lblFri=new JLabel("Friday");
		lblFri.setOpaque(true);
		lblFri.setBackground(new Color(100, 149, 237));
		lblFri.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblFri.setPreferredSize(new Dimension(85, 20));
		JLabel lblSat=new JLabel("Saturday");
		lblSat.setBackground(new Color(100, 149, 237));
		lblSat.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSat.setOpaque(true);
		lblSat.setPreferredSize(new Dimension(85, 20));
		JLabel lblSun=new JLabel("Sunday");
		lblSun.setBackground(new Color(100, 149, 237));
		lblSun.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSun.setOpaque(true);
		lblSun.setPreferredSize(new Dimension(85, 20));
		
		
		paneMon=new JPanel();
		paneMon.setBackground(new Color(230, 230, 250));
		//paneMon.setLayout(new BoxLayout(paneMon, BoxLayout.X_AXIS));
		paneMon.setLayout(new FlowLayout(FlowLayout.LEFT));
		//paneMon.setBackground(bg);
		paneMon.add(lblMon,Component.LEFT_ALIGNMENT);
		
		
		
		paneTue=new JPanel();
		//paneTue.setLayout(new BoxLayout(paneTue, BoxLayout.X_AXIS));
		paneTue.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneTue.add(lblTue, Component.LEFT_ALIGNMENT);
		
		
		paneWed=new JPanel();
		paneWed.setBackground(new Color(230, 230, 250));
		//paneWed.setLayout(new BoxLayout(paneWed, BoxLayout.X_AXIS));
		paneWed.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneWed.add(lblWed, Component.LEFT_ALIGNMENT);
	
		
		paneThu=new JPanel();
		//paneThu.setLayout(new BoxLayout(paneThu, BoxLayout.X_AXIS));
		paneThu.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneThu.add(lblThu, Component.LEFT_ALIGNMENT);
		
		
		
		paneFri=new JPanel();
		paneFri.setBackground(new Color(230, 230, 250));
		paneFri.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneFri.add(lblFri, Component.LEFT_ALIGNMENT);
		
		
		paneSat=new JPanel();
		paneSat.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneSat.add(lblSat, Component.LEFT_ALIGNMENT);
		
		
		paneSun=new JPanel();
		paneSun.setBackground(new Color(230, 230, 250));
		paneSun.setLayout(new FlowLayout(FlowLayout.LEFT));
		paneSun.add(lblSun, Component.LEFT_ALIGNMENT);
		
		populateInnerPanes();//populate inner panes with schedule data
		
		//add inner panels to outer panels
		outerPane.add(paneMon, Component.LEFT_ALIGNMENT);
		outerPane.add(paneTue, Component.LEFT_ALIGNMENT);
		outerPane.add(paneWed, Component.LEFT_ALIGNMENT);
		outerPane.add(paneThu, Component.LEFT_ALIGNMENT);
		outerPane.add(paneFri, Component.LEFT_ALIGNMENT);
		outerPane.add(paneSat, Component.LEFT_ALIGNMENT);
		outerPane.add(paneSun, Component.LEFT_ALIGNMENT);
		
		
		//add to layout
		add(outerPane);
		

	}
	
	private void populateInnerPanes()
	{
		
		if(isSprinkler)//if sprinkler selected,
		{
			int sprinklerId=service.getSprinklerByName(selectedId).getSprinklerId();
			//populate week data for selected sprinkler
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Monday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Tuesday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Wednesday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Thursday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Friday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Saturday);
			populateSprinklerConfigForDay(sprinklerId,DayOfWeek.Sunday);
		}
		else//else if group selected,
		{
			//System.out.println("schedule: group="+selectedId);
			
			//populate week data for selected group
			int groupId=service.getSprinklerGroupByName(selectedId).getGroupId();
			//System.out.println("groupid="+groupId);
			populateGroupConfigForDay(groupId,DayOfWeek.Monday);
			populateGroupConfigForDay(groupId,DayOfWeek.Tuesday);
			populateGroupConfigForDay(groupId,DayOfWeek.Wednesday);
			populateGroupConfigForDay(groupId,DayOfWeek.Thursday);
			populateGroupConfigForDay(groupId,DayOfWeek.Friday);
			populateGroupConfigForDay(groupId,DayOfWeek.Saturday);
			populateGroupConfigForDay(groupId,DayOfWeek.Sunday);
		
		}
		
	}
	
	private void populateSprinklerMon(int sprinklerId)
	{
		//get Monday data for selected sprinkler
		List<SprinklerConfiguration> sConfigList=new ArrayList<SprinklerConfiguration>();
		sConfigList=service.getSprinklerConfigurationByDay(sprinklerId, DayOfWeek.Monday.toString());
		
		
		for(SprinklerConfiguration sConfig:sConfigList)
		{
			//generate jpanel
			JPanel pn=new JPanel();
			JLabel lblTime=new JLabel(formatTime(sConfig.getStartTime(), sConfig.getEndTime()));//format time range
			JMenuItem m1 = new JMenuItem(new ImageIcon(getClass().getResource("/res/deleteIcon.png")));
			m1.setActionCommand(String.valueOf(sConfig.getId()));
			m1.addActionListener(menuItemListener);
			
			pn.add(lblTime);
			pn.add(m1);
			
			//add to paneMon
			paneMon.add(pn);
		
		}
		
	}
	private void populateSprinklerConfigForDay(int sprinklerId,DayOfWeek day)
	{
		//get day's data for selected sprinkler
		List<SprinklerConfiguration> sConfigList=new ArrayList<SprinklerConfiguration>();
		sConfigList=service.getSprinklerConfigurationByDay(sprinklerId, day.toString());
		
		
		for(SprinklerConfiguration sConfig:sConfigList)
		{
			//generate jpanel
			JPanel pn=new JPanel();
			JLabel lblTime=new JLabel(formatTime(sConfig.getStartTime(), sConfig.getEndTime()));//format time range
			JMenuItem m1 = new JMenuItem(new ImageIcon(getClass().getResource("/res/deleteIcon.png")));
			m1.setActionCommand(String.valueOf(sConfig.getId()));
			m1.addActionListener(menuItemListener);
			
			pn.add(lblTime);
			pn.add(m1);
			
			//add to corresponding pane
			switch(day)
			{
			case Monday:
				paneMon.add(pn);
				break;
			case Tuesday:
				paneTue.add(pn);
				break;
			case Wednesday:
				paneWed.add(pn);
				break;
			case Thursday:
				paneThu.add(pn);
				break;
			case Friday:
				paneFri.add(pn);
				break;
			case Saturday:
				paneSat.add(pn);
				break;
			case Sunday:
				paneSun.add(pn);
				break;
			default:
				break;
			}
			
		
		}
		
	}
	
	private void populateGroupConfigForDay(int groupId,DayOfWeek day)
	{
		//get day's data for selected sprinkler
		List<SprinklerGroupConfiguration> gConfigList=new ArrayList<SprinklerGroupConfiguration>();
		gConfigList=service.getGroupConfigurationByDay(groupId, day.toString());
		
		
		for(SprinklerGroupConfiguration gConfig:gConfigList)
		{
			//System.out.println(gConfig.getId());
			//generate jpanel
			JPanel pn=new JPanel();
			JLabel lblTime=new JLabel(formatTime(gConfig.getStartTime(), gConfig.getEndTime()));//format time range
			JMenuItem m1 = new JMenuItem(new ImageIcon(getClass().getResource("/res/deleteIcon.png")));
			m1.setActionCommand(String.valueOf(gConfig.getId()));
			m1.addActionListener(menuItemListener);
			
			pn.add(lblTime);
			pn.add(m1);
			
			//add to corresponding pane
			switch(day)
			{
			case Monday:
				paneMon.add(pn);
				break;
			case Tuesday:
				paneTue.add(pn);
				break;
			case Wednesday:
				paneWed.add(pn);
				break;
			case Thursday:
				paneThu.add(pn);
				break;
			case Friday:
				paneFri.add(pn);
				break;
			case Saturday:
				paneSat.add(pn);
				break;
			case Sunday:
				paneSun.add(pn);
				break;
			default:
				break;
			}
			
		
		}
		
	}
	
	
	
	/**
	 * Format time to desired format: start time - end time
	 * @param startTime
	 * @param endTime
	 * @return String
	 */
	private String formatTime(Date startTime,Date endTime)
	{
		
		SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
		
		return (formatter.format(startTime) +"-" + formatter.format(endTime));
		
	}
	
	//EventHandlers
	class MenuItemListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{  
			JMenuItem j=(JMenuItem)e.getSource();
			JPanel p=(JPanel)j.getParent();//gets the parent JPane for this entry
			JPanel iPane=(JPanel)p.getParent();//gets the inner panel in which this component lies
			
			if(isSprinkler)// delete selected sprinkler config
			{
				int sConfigId=Integer.parseInt(e.getActionCommand());
				//System.out.println("deleting"+sConfigId);
				int response = JOptionPane.showConfirmDialog(null, "Do you really want to delete this schedule?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response==JOptionPane.YES_OPTION)
				{
					//delete from db
					int st=service.deleteSprinklerConfigById(sConfigId);
					//System.out.println("del-"+st);
					//remove from UI
					
					iPane.remove(p);//paneMon.remove(p);
					iPane.repaint();//paneMon.repaint(); 
				}
			}
			else//delete selected group config
			{
				int gConfigId=Integer.parseInt(e.getActionCommand());
				//System.out.println("deleting"+gConfigId);
				int response = JOptionPane.showConfirmDialog(null, "Do you really want to delete this schedule?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response==JOptionPane.YES_OPTION)
				{
					//delete from db
					int st=service.deleteGroupConfigById(gConfigId);
					//System.out.println("del-"+st);
					//remove from UI
					
					iPane.remove(p);//paneMon.remove(p);
					iPane.repaint();//paneMon.repaint(); 
				}
			}
			
			
		}
		
	}

}
