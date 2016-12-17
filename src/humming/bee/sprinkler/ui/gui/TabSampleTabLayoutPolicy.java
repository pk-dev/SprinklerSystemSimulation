package humming.bee.sprinkler.ui.gui;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

public class TabSampleTabLayoutPolicy {
	static void add(JTabbedPane tabbedPane, String label) {
	    JButton button = new JButton(label);
	    tabbedPane.addTab(label, button);
	  }

}
