/**
 * 
 */
package humming.bee.sprinkler.ui.gui;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerGroup;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 *
 */
public class SprinklerSelection extends JPanel {

	private SprinklerService sprinklerService=new SprinklerService();
	
	private JComboBox<String> cmbSprinklerGroup;
	private JComboBox<String> cmbSprinkler;
	private JButton btnShow;
	
	private SprinklerSelectionListener selectionListener;
	
	//setter
	public void setSelectionListener(SprinklerSelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	/**
	 * Create the panel.
	 */
	public SprinklerSelection() {

		
		setLayout(new FlowLayout(FlowLayout.CENTER,30,5));
		
		cmbSprinklerGroup=new JComboBox<String>();
		cmbSprinklerGroup.addItem("North");
		cmbSprinklerGroup.addItem("East");
		cmbSprinklerGroup.addItem("West");
		cmbSprinklerGroup.addItem("South");
		
		cmbSprinklerGroup.setSelectedIndex(0);
		
		
		cmbSprinkler=new JComboBox();
		cmbSprinkler.addItem("Select");
		populateSprinklers();
		
		
		btnShow=new JButton("Show");
		btnShow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// display weekly schedule for the selected sprinkler/group
				if(cmbSprinkler.getSelectedIndex()==0)//yes, only group is selected.
				{
					String selectedGroup=cmbSprinklerGroup.getSelectedItem().toString();
					selectionListener.selectionChanged(selectedGroup,false);
				}
				else//sprinkler is also selected
				{
					String selectedSprinkler=cmbSprinkler.getSelectedItem().toString();
					selectionListener.selectionChanged(selectedSprinkler,true);
				}
			}
		});
		
		
		//Populate sprinklers by group name 
		//when a new group is selected
		cmbSprinklerGroup.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				//repopulate Sprinkler combobox when a new group is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
			          // get sprinklers in newly selected group
					  populateSprinklers();
			          
			       }
				
			}
		});
		
		JPanel groupPane=new JPanel();
		groupPane.add(new JLabel("Group:"));
		groupPane.add(cmbSprinklerGroup);
		
		JPanel sprinklerPane=new JPanel();
		sprinklerPane.add(new JLabel("Sprinkler:"));
		sprinklerPane.add(cmbSprinkler);
		
		add(groupPane);
		add(sprinklerPane);
		add(btnShow);
		
		
		
	}
	
	private void populateSprinklers()
	{
		List<Sprinkler> sprinklers=sprinklerService.getSprinklersByGroup(cmbSprinklerGroup.getSelectedItem().toString());
        cmbSprinkler.removeAllItems();//remove all sprinklers currently in combobox
        cmbSprinkler.addItem("Select");
        //add new list of sprinklers to combobox
        for(Sprinkler sprinkler:sprinklers)
        {
      	  cmbSprinkler.addItem(sprinkler.getSprinklerName());
        }
	}
	
}
