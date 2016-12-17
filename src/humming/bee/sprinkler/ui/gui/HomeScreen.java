package humming.bee.sprinkler.ui.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.GroupRunDuration;
import humming.bee.sprinkler.service.model.OverrideTemperatureConfiguration;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerSystem;
import humming.bee.sprinkler.service.model.TemperatureSensor;

public class HomeScreen {

	private JFrame frame;
	private JPanel titlePane;
	private JPanel statusPane;
	private JPanel functionalityPane;
	private JButton btnOverrideButton;
	private JButton btnNextScreen;
	private JButton btnSetTemp;
	private JLabel lblTime;
	private JPanel northPane;
	private JPanel eastPane;
	private JPanel westPane;
	private JPanel southPane;
	private JLabel lblWeather;
	private JCheckBox chkSystem;
	
	private JTabbedPane graphPane;

	List<Sprinkler> sprinklerList;
	List<Sprinkler> northList = new ArrayList<Sprinkler>();
	List<Sprinkler> eastList = new ArrayList<Sprinkler>();
	List<Sprinkler> southList = new ArrayList<Sprinkler>();
	List<Sprinkler> westList = new ArrayList<Sprinkler>();
	List<SprinklerGroup> groupList;
	List<GroupRunDuration> groupRunDurationList;

	SprinklerService service = new SprinklerService();
	DateFormat dateFormat = new SimpleDateFormat("h:mm a  MMM d, ''yy");

	ImageIcon iconOn = new ImageIcon(getClass().getResource("/res/ON.png"));
	ImageIcon iconOff = new ImageIcon(getClass().getResource("/res/off.png"));
	ImageIcon iconNotWorking = new ImageIcon(getClass().getResource("/res/not-working.png"));
	Color[] colors = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.ORANGE, Color.BLACK,
			Color.GRAY};

	/**
	 * Launch the application.
	 */
	public static void startHomeScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// UIManager.put("nimbusBase", Color.nimbusOrange);
					UIManager.put("nimbusBlueGrey", new Color(128, 255, 102));
					// UIManager.put("control", new Color(204,255,229));
					UIManager.put("control", new Color(255, 255, 255));

					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.
				}
				try {
					HomeScreen window = new HomeScreen();
					// window.welcomeScreen.setVisible(true);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeScreen() {
		getData();
		initialize();
		Timer timer = new Timer(5000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshData();
			}
		});
		timer.start();
	}

	public void refreshData() {
		int selectedGraphTab = graphPane.getSelectedIndex();
		getData();
		statusPane.removeAll();
		createGroupPanes();
		graphPane.removeAll();
		addBarToGraph();
		graphPane.setSelectedIndex(selectedGraphTab);
		setCurrentTime();
		frame.revalidate();
	}

	private void getData() {
		Map<Integer, String> groupIdNameMap = new HashMap<Integer, String>();
		sprinklerList = service.getSprinkler();
		groupList = service.getGroup();
		for (SprinklerGroup group : groupList) {
			groupIdNameMap.put(group.getGroupId(), group.getGroupName());
		}

		groupRunDurationList = service.getGroupRunDuration();
		northList.clear();
		eastList.clear();
		westList.clear();
		southList.clear();
		for (int i = 0; i < sprinklerList.size(); i++) {
			int groupId = sprinklerList.get(i).getGroupId();
			if (groupIdNameMap.get(groupId).equals("North")) {
				northList.add(sprinklerList.get(i));
			} else if (groupIdNameMap.get(groupId).equals("East")) {
				eastList.add(sprinklerList.get(i));
			} else if (groupIdNameMap.get(groupId).equals("West")) {
				westList.add(sprinklerList.get(i));
			} else if (groupIdNameMap.get(groupId).equals("South")) {
				southList.add(sprinklerList.get(i));
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();

		JFrame.setDefaultLookAndFeelDecorated(false);

		/*******************************************************************************************************************************************************/
		// Frame with Box Layout and 4 panels
		frame = new JFrame();
		/*******************************************************************************************************************************************************/
		// Welcome Scren with Border Layout and 4 panels

		// Gets the current Screen size and set it to the Frame
		frame.setSize(dim.width, dim.height);
		frame.setTitle("Garden Sprinkler System simulation  by Aarthi and Priya");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		titlePane();
		buttonPane();
		statusPane();
		waterConsumptionGraph();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, statusPane, functionalityPane);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		splitPane.setResizeWeight(0.9f);
		splitPane.setDividerLocation(0.7);
	}

	/*******************************************************************************************************************************************************/
	// Title panel to display the Title,Time and temperature
	private void titlePane() {

		titlePane = new JPanel();

		// This part is for getting System Date

		EmptyBorder border = new EmptyBorder(3, 0, 6, 50);
		EmptyBorder border1 = new EmptyBorder(3, 0, 6, 250);

		titlePane.setPreferredSize(new Dimension(150, 50));
		titlePane.setBorder(new LineBorder(new Color(102, 0, 0), 2));

		// Get the URL of image to set icon
		ImageIcon icon = new ImageIcon(getClass().getResource("/res/wateringcan.png"));
		JLabel title = new JLabel();
		title.setIcon(icon);
		title.setText("HUMMING BEE HOME GARDEN SPRINKLER SYSTEM");
		title.setFont(new Font("Calibri",Font.BOLD,14));
		title.setIconTextGap(5);
		title.setBorder(border1);
		titlePane.add(title, titlePane.CENTER_ALIGNMENT);

		ImageIcon time = new ImageIcon(getClass().getResource("/res/time.png"));
		lblTime = new JLabel();
		setCurrentTime();
		lblTime.setIcon(time);
		lblTime.setIconTextGap(5);
		lblTime.setBorder(border);
		titlePane.add(lblTime);
		String temp = Integer.toString(TemperatureSensor.getCurrentTemperature());
		ImageIcon weather = new ImageIcon(getClass().getResource("/res/Weather-icon.png"));
		lblWeather = new JLabel();
		lblWeather.setText(temp + "˚F");
		lblWeather.setIcon(weather);
		lblWeather.setIconTextGap(5);
		lblWeather.setBorder(border);

		titlePane.add(lblWeather, titlePane.RIGHT_ALIGNMENT);
		
		chkSystem = new JCheckBox("Enabled");
		chkSystem.setSelected(true);
		titlePane.add(chkSystem, titlePane.RIGHT_ALIGNMENT);
		
		chkSystem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chkSystem.isSelected()) {
					//enable now if it is disabled
					if (!SprinklerSystem.isEnabled()) {
						SprinklerSystem.setEnabled(true);
						btnOverrideButton.setEnabled(true);
						btnSetTemp.setEnabled(true);
						btnNextScreen.setEnabled(true);
						refreshData();
					}
				}
				else {
					//disable now if it is enabled
					if (SprinklerSystem.isEnabled()) {
						SprinklerSystem.setEnabled(false);
						try {
							Thread.sleep(1000);
							disableAllSprinklers();
							btnOverrideButton.setEnabled(false);
							btnSetTemp.setEnabled(false);
							btnNextScreen.setEnabled(false);
							refreshData();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		/*
		 * title.setSize(new Dimension(150, 20)); titlePane.add(title);
		 */
		frame.getContentPane().add(titlePane, BorderLayout.NORTH);

	}
	
	private void disableAllSprinklers() {
		for (SprinklerGroup group : groupList) {
			service.updateSprinklerGroupStatusById(group.getGroupId(), "OFF");
		}
	}

	private void setCurrentTime() {
		Date date = new Date();
		lblTime.setText(dateFormat.format(date));
	}
	

	/*******************************************************************************************************************************************************/
	// Status panel to display the status of Sprinklers
	private void statusPane() {
		statusPane = new JPanel();
		statusPane.setPreferredSize(new Dimension(300, 300));
		statusPane.setBorder(new TitledBorder(new LineBorder(new Color(102, 0, 0), 2), "Sprinkler Status",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));

		// Set Grid Layout for the Status panel with 2 Rows and 2 Columns
		statusPane.setLayout(new GridLayout(2, 2));
		createGroupPanes();
	}

	private void createGroupPanes() {
		northPane();
		eastPane();
		westPane();
		southPane();
	}

	/*------------------------------------------------------------------------------------------------------------------- ----------------*/
	// North pane of the Status panel

	private void northPane() {

		northPane = new JPanel();
		northPane.setPreferredSize(new Dimension(100, 100));

		northPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 102, 0), 2), "North Group",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// Set Grid Layout for the Status panel with 2 Rows and 3 Columns
		northPane.setLayout(new GridLayout(3, 4, 2, 3));

		for (int i = 0; i < northList.size(); i++) {
			JPanel sprinklerpane = new JPanel();
			sprinklerpane.setLayout(new GridLayout(1, 2));

			JButton btnSprinkler = new JButton();
			btnSprinkler.setText(northList.get(i).getSprinklerName());

			// iconON
			JLabel statusSpklr = new JLabel();
			if (northList.get(i).isFunctional()) {
				if (northList.get(i).getSprinklerStatus().equals("ON")) {
					statusSpklr.setIcon(iconOn);
				} else if (northList.get(i).getSprinklerStatus().equals("OFF")) {
					statusSpklr.setIcon(iconOff);
				}

			} else {
				statusSpklr.setIcon(iconNotWorking);
			}

			statusSpklr.setHorizontalAlignment(SwingConstants.LEFT);
			statusSpklr.setBorder(new EmptyBorder(0, 10, 0, 0));
			sprinklerpane.add(btnSprinkler);
			sprinklerpane.add(statusSpklr);
			northPane.add(sprinklerpane);
		}

		// Adding the North Pane to the Status Panel Grid
		statusPane.add(northPane);
	}

	/*------------------------------------------------------------------------------------------------------------------- ----------------*/
	// East pane of the status pane
	private void eastPane() {
		eastPane = new JPanel();
		eastPane.setPreferredSize(new Dimension(100, 100));

		eastPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 102, 0), 2), "East Group", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		// Set GridLayout for the East Pane with 2 Rows and 3 Columns
		eastPane.setLayout(new GridLayout(3, 4, 2, 3));

		for (int i = 0; i < eastList.size(); i++) {
			JPanel sprinklerpane = new JPanel();
			sprinklerpane.setLayout(new GridLayout(1, 2));
			// sprinklerpane.setBackground(new Color(255, 255, 255));

			JButton btnSprinkler = new JButton(eastList.get(i).getSprinklerName());
			btnSprinkler.setOpaque(false);

			JLabel statusSpklr = new JLabel();
			if (eastList.get(i).isFunctional()) {
				if (eastList.get(i).getSprinklerStatus().equals("ON")) {
					statusSpklr.setIcon(iconOn);
				} else if (eastList.get(i).getSprinklerStatus().equals("OFF")) {
					statusSpklr.setIcon(iconOff);
				}
			} else {
				statusSpklr.setIcon(iconNotWorking);
			}

			statusSpklr.setHorizontalAlignment(SwingConstants.LEFT);
			statusSpklr.setBorder(new EmptyBorder(0, 10, 0, 0));
			sprinklerpane.add(btnSprinkler);
			sprinklerpane.add(statusSpklr);
			eastPane.add(sprinklerpane);
		}

		// Adding the East Pane to the Status Panel Grid
		statusPane.add(eastPane);
	}

	/*------------------------------------------------------------------------------------------------------------------- ----------------*/
	// West pane of the status pane
	private void westPane() {
		westPane = new JPanel();
		westPane.setPreferredSize(new Dimension(100, 100));

		westPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 102, 0), 2), "West Group", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		// Set GridLayout for the West Pane with 2 Rows and 3 Columns
		westPane.setLayout(new GridLayout(3, 4, 2, 3));

		for (int i = 0; i < westList.size(); i++) {
			JPanel sprinklerpane = new JPanel();
			sprinklerpane.setLayout(new GridLayout(1, 2));
			// 128,255,128

			JButton btnSprinkler = new JButton(westList.get(i).getSprinklerName());
			btnSprinkler.setOpaque(true);

			JLabel statusSpklr = new JLabel();
			if (westList.get(i).isFunctional()) {
				if (westList.get(i).getSprinklerStatus().equals("ON")) {
					statusSpklr.setIcon(iconOn);
				} else if (westList.get(i).getSprinklerStatus().equals("OFF")) {
					statusSpklr.setIcon(iconOff);
				}
			} else {
				statusSpklr.setIcon(iconNotWorking);
			}
			statusSpklr.setHorizontalAlignment(SwingConstants.LEFT);
			statusSpklr.setBorder(new EmptyBorder(0, 10, 0, 0));
			sprinklerpane.add(btnSprinkler);
			sprinklerpane.add(statusSpklr);
			westPane.add(sprinklerpane);
		}

		// Adding the West Pane to the Status Panel Grid
		statusPane.add(westPane);
	}

	/*------------------------------------------------------------------------------------------------------------------- ----------------*/
	// South pane of the status pane
	private void southPane() {
		southPane = new JPanel();
		southPane.setPreferredSize(new Dimension(100, 100));

		southPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 102, 0), 2), "South Group",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// Set GridLayout for the southPane with 2 Rows and 3 Columns
		southPane.setLayout(new GridLayout(3, 4, 3, 2));

		for (int i = 0; i < southList.size(); i++) {
			JPanel sprinklerpane = new JPanel();
			sprinklerpane.setLayout(new GridLayout(1, 2));

			JButton btnSprinkler = new JButton(southList.get(i).getSprinklerName());
			btnSprinkler.setOpaque(true);

			JLabel statusSpklr = new JLabel();
			if (southList.get(i).isFunctional()) {
				if (southList.get(i).getSprinklerStatus().equals("ON")) {
					statusSpklr.setIcon(iconOn);
				} else if (southList.get(i).getSprinklerStatus().equals("OFF")) {
					statusSpklr.setIcon(iconOff);
				}
			} else {
				statusSpklr.setIcon(iconNotWorking);
			}
			statusSpklr.setHorizontalAlignment(SwingConstants.LEFT);
			statusSpklr.setBorder(new EmptyBorder(0, 10, 0, 0));
			sprinklerpane.add(btnSprinkler);
			sprinklerpane.add(statusSpklr);
			southPane.add(sprinklerpane);
		}

		// Adding the West Pane to the Status Panel Grid
		statusPane.add(southPane);
	}

	/*******************************************************************************************************************************************************/

	/*
	 * Panel for water consumption graph with a combo box to choose between
	 * Sprinkler, Sprinkler Group, Sprinkler System and it has a BoxLayout
	 */
	private void waterConsumptionGraph() {

		graphPane = new JTabbedPane();
		graphPane.setPreferredSize(new Dimension(150, 350));
		graphPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// SprinklerSystemGraphPane.setPreferredSize(new Dimension(150, 300));
		graphPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 102, 0), 2), "Water Consumption Graph",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		addBarToGraph();
		
		frame.add(graphPane, BorderLayout.SOUTH);
	}

	private void addBarToGraph() {
		Map<Integer, List<GroupRunDuration>> groupMap = new HashMap<Integer, List<GroupRunDuration>>();
		for (GroupRunDuration groupRunDuration : groupRunDurationList) {
			if (!groupMap.containsKey(groupRunDuration.getGroupId())) {
				groupMap.put(groupRunDuration.getGroupId(), new ArrayList<GroupRunDuration>());
			}
			groupMap.get(groupRunDuration.getGroupId()).add(groupRunDuration);
		}
		for (SprinklerGroup group : groupList) {
			String groupName = group.getGroupName();
			JPanel sprinklerGroupPane = new JPanel();
			graphPane.add(new JScrollPane(sprinklerGroupPane), groupName);
			List<GroupRunDuration> groupDurationList = groupMap.get(group.getGroupId());
			HistogramGraph panel = new HistogramGraph();
			int i = 0;
			if (groupDurationList != null && !groupDurationList.isEmpty()) {
				for (GroupRunDuration groupRunDuration : groupDurationList) {
					panel.addHistogramColumn(Integer.toString(groupRunDuration.getDay()),
							calculateVolume(groupRunDuration.getDurationInSeconds()), colors[i++ % colors.length]);
				}
			}

			panel.layoutHistogram();
			sprinklerGroupPane.add(panel);
		}
	}

	private double calculateVolume(long duration) {
		return new Double(duration) * 0.05;
	}

	// Panel for temperature Settings with Flowlayout and it has 3 buttons
	private void buttonPane() {
		functionalityPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));

		functionalityPane.setPreferredSize(new Dimension(150, 90));
		functionalityPane.setBorder(new LineBorder(new Color(0, 102, 0), 2, true));

		JPanel overrideTemp = new JPanel();
		overrideTemp.setName("Set Temperature Limit");
		overrideTemp.setLayout(new GridLayout(0, 2));

		JLabel lblSelectGrp = new JLabel("Select Group");
		lblSelectGrp.setPreferredSize(new Dimension(80, 40));
		Vector<String> vg = new Vector<String>();
		vg.add("North");
		vg.add("South");
		vg.add("East");
		vg.add("West");
		JComboBox<String> selectGroup = new JComboBox<String>(vg);
		selectGroup.setPreferredSize(new Dimension(150, 20));
		overrideTemp.add(lblSelectGrp);
		overrideTemp.add(selectGroup);

		overrideTemp.add(new JLabel("Upper Limit (˚F)"));
		JTextField upperLimit = new JTextField();

		overrideTemp.add(upperLimit);

		overrideTemp.add(new JLabel("Lower Limit (˚F)"));
		JTextField lowerLimit = new JTextField();

		overrideTemp.add(lowerLimit);

		btnOverrideButton = new JButton("Temperature Setting");
		btnOverrideButton.setActionCommand("");
		btnOverrideButton.setPreferredSize(new Dimension(180, 50));
		btnOverrideButton.setOpaque(true);
		btnOverrideButton.setBorderPainted(false);

		btnOverrideButton.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int result = JOptionPane.showConfirmDialog(null, overrideTemp, "Temperature Setting",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				OverrideTemperatureConfiguration tempConfig = new OverrideTemperatureConfiguration();
				if (result == JOptionPane.OK_OPTION) {
					String upLimit = upperLimit.getText();
					String lowLimit = lowerLimit.getText();
					if (upLimit != null && !upLimit.isEmpty())
						tempConfig.setTempUpperLimit(Integer.valueOf(upLimit));
					if (lowLimit != null && !lowLimit.isEmpty())
						tempConfig.setTempLowerLimit(Integer.valueOf(lowLimit));
					for (int i = 0; i < groupList.size(); i++) {
						if (vg.get(selectGroup.getSelectedIndex()).equals(groupList.get(i).getGroupName())) {
							tempConfig.setGroupId(groupList.get(i).getGroupId());
						}
					}
					service.setTemperatureConfiguration(tempConfig);
				}
			}

		});
		functionalityPane.add(btnOverrideButton);

		JPanel changeTemperature = new JPanel();
		changeTemperature.setLayout(new GridLayout(1, 1));
		changeTemperature.add(new JLabel("Enter the new Temperature"));
		JTextField changeTemp = new JTextField();
		changeTemperature.add(changeTemp);

		btnSetTemp = new JButton("Change Temperature");
		btnSetTemp.setPreferredSize(new Dimension(180, 50));
		// btnSetTemp.setBackground(new Color(128, 255, 128));
		btnSetTemp.setOpaque(true);
		btnSetTemp.setBorderPainted(false);

		btnSetTemp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int result = JOptionPane.showConfirmDialog(null, changeTemperature, "Change Temperature",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {

					try {
						int newTempValue = Integer.parseInt(changeTemp.getText());

						lblWeather.setText(Integer.toString(newTempValue) + "˚F");
						TemperatureSensor.setCurrentTemperature(newTempValue);

					} catch (NumberFormatException exception) {

						JOptionPane.showMessageDialog(null,
								"You didn't enter a Valid Number. So the temperature is not changed", "Warning",
								JOptionPane.WARNING_MESSAGE);

					}

				}

			}
		});
		functionalityPane.add(btnSetTemp);

		btnNextScreen = new JButton("Sprinkler Config");
		btnNextScreen.setPreferredSize(new Dimension(180, 50));
		// btnNextScreen.setBackground(new Color(128, 255, 128));
		btnNextScreen.setOpaque(true);
		btnNextScreen.setBorderPainted(false);
		btnNextScreen.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				// frame.dispose();
				Settings settings = new Settings();
			}
		});
		functionalityPane.add(btnNextScreen);

	}

}
