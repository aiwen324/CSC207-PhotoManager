package photorenamer;

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TagPanel extends JPanel {
	
	public JList tagList = new JList();
	
	public JScrollPane scrollTags = new JScrollPane(tagList);
	
	// List all selected photo's previous names
	public JComboBox previousNames = new JComboBox();
	
	public JButton rename = new JButton("rename");
	
	public TagPanel() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		previousNames.setSelectedIndex(0);
		previousNames.setAlignmentX(Component.LEFT_ALIGNMENT);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(scrollTags)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(previousNames)
					.addComponent(rename)));
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(scrollTags)
					.addGroup(layout.createSequentialGroup()
						.addComponent(previousNames)
						.addComponent(rename))));
	}
	
}

