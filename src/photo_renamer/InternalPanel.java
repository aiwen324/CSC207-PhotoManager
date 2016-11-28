package photo_renamer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import manager.PhotoManager;
import photo.Photo;

public class InternalPanel extends JPanel {
	//TODO: Set the size for 2 Labels
	public JLabel photoNameLabel = new JLabel("This will show the icon for photo");
	
	public JScrollPane scrollPane = new JScrollPane(photoNameLabel);
	
	public JLabel txtLabel = new JLabel("This will show some result");
	
	private PhotoManager photoManager;
	protected Photo img;
	
	String[] additionalFunction = { "Change tags or rename",
					"Find most similar photos", 
					"Find most common tags",
					"Find the photo with most tags", 
					"Clear tags for all photo", 
					"Reset all changes" };
	public JComboBox addFunction = new JComboBox(additionalFunction);
	
	public JButton okay = new JButton("Confirm");
	public InternalPanel(PhotoManager pm, Photo img) {
		this.photoManager = pm;
		this.img = img;
		this.okay.addActionListener(new ConfirButtonListener());
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		addFunction.setSelectedIndex(0);
		addFunction.setAlignmentX(Component.LEFT_ALIGNMENT);
		// Specify the layout
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addComponent(txtLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(addFunction)
					.addComponent(okay)));
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(scrollPane).addComponent(txtLabel)
					.addGroup(layout.createSequentialGroup()
						.addComponent(addFunction)
						.addComponent(okay))));

	}
	
	public void setIMG(Photo img){
		this.img = img;
	}
	
	class ConfirButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (img == null){
				txtLabel.setText("Choose A Photo PLZZZZZZZ");
			}
			else if (addFunction.getSelectedIndex() == 0){
				txtLabel.setText("Tags Editor Initializing...");
				new SubFrame(photoManager, img).setVisible(true);
			} else if (addFunction.getSelectedIndex() != 0){
				txtLabel.setText("The methods are not implemented");
			}
		}
	}
	

	
}
