package photorenamer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import manager.PhotoManager;
import photo.Photo;

public class PhotoPanel extends JPanel {

	public JLabel photoDirectoryLabel = new JLabel("No photo selected");
	public JButton photoChooser = new JButton("Choose Photo");
	public InternalPanel internalPanel;
	private PhotoManager photoManager;
	protected Photo img;

	public PhotoPanel(PhotoManager pm) {
		this.photoManager = pm;
		this.setLayout(new BorderLayout());
		internalPanel = new InternalPanel(photoManager, img);
		// photoDirectoryLabel.setBackground(Color.green);
		// photoDirectoryLabel.setOpaque(true);
		// (The above 2 lines can paint the background, delete it if
		// it is unnecessary in the end)
		photoDirectoryLabel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
						BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		photoDirectoryLabel.setPreferredSize(new Dimension(600, 20));
		this.add(photoDirectoryLabel, BorderLayout.NORTH);
		this.add(internalPanel, BorderLayout.CENTER);
		this.add(photoChooser, BorderLayout.SOUTH);
		GUIListener mylistener = new GUIListener();
		photoChooser.addActionListener(mylistener);
	}
	
	
	class GUIListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			if (e.getSource() == photoChooser){
				System.out.println("Photo Chooser button is clicked");
				JFileChooser jfc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
				jfc.addChoosableFileFilter(filter);
				// TODO: Need to fix this
				int returnVal = jfc.showOpenDialog(new PhotoPanel(photoManager));
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File file = jfc.getSelectedFile();
					photoDirectoryLabel.setText(file.getAbsolutePath());
					String filename = file.getName();
					String filetype = filename.substring(
							filename.lastIndexOf(".")+1);
					String[] extension = new String[] {"jpg", "png", "gif", "bmp"};
					if (Arrays.asList(extension).contains(filetype)) {
						Photo ph = new Photo(file.getAbsolutePath());
						img = photoManager.getPhoto(ph);
						img.addObserver(photoManager);
						internalPanel.setIMG(img);
						BufferedImage image = null;
						try {
						image = ImageIO.read(file);
						} catch (IOException e1){
							
						}
						ImageIcon icon = new ImageIcon(image);
						internalPanel.photoNameLabel.setIcon(icon);
						if (icon != null){
							internalPanel.photoNameLabel.setText(null);
						}
					}
				}
			}
		}
	}
	
	
	
}
