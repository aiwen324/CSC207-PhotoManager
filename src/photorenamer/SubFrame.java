package photorenamer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import manager.PhotoManager;
import photo.Photo;

public class SubFrame extends JFrame implements WindowListener{
	
	private PhotoManager pm;
	
	private Photo img;
	
	private JButton rename;
	
	private JComboBox<String> nameHistory;
	
	private TagList tagList;
	
	private DefaultComboBoxModel<String> comboboxModel;
	
	public SubFrame(PhotoManager pm, Photo img){
		super("Tags Manager");
		this.pm = pm;
		this.img = img;
		Collection<String> namecol = this.img.getAllHistoryNames();
		String[] nameHis = namecol.toArray(new String[namecol.size()]);
		this.comboboxModel = new DefaultComboBoxModel<>(nameHis);
		this.nameHistory = new JComboBox(comboboxModel);
		tagList = new TagList(pm, img, nameHistory);
		this.rename = new JButton("Rename");
		this.rename.addActionListener(new RenameButtonListener());
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(this.nameHistory);
		bottomPanel.add(this.rename);
		add(tagList, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		addWindowListener(this);
		pack();
		setVisible(true);
	}
	
	class RenameButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			tagList.updateTags((String) nameHistory.getSelectedItem());
			Collection<String> namecol = img.getAllHistoryNames();
			String[] nameHis = namecol.toArray(new String[namecol.size()]);
			comboboxModel = new DefaultComboBoxModel<>(nameHis);
			nameHistory.setModel(comboboxModel);
		}
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.dispose();
		
	}
		

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
}
