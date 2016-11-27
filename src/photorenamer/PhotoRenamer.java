package photorenamer;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import manager.PhotoManager;

// Note: It's not done yet. But it has the basic frame now.
public class PhotoRenamer implements WindowListener {

	public static PhotoManager pm;
	

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Photo Renamer");
		mainFrame.setSize(600, 700);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String path = "photos.ser";
		try {
			pm = new PhotoManager(path);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		PhotoPanel panel = new PhotoPanel(pm);
		mainFrame.setContentPane(panel);
		PhotoRenamer pr = new PhotoRenamer();
		mainFrame.addWindowListener(pr);
		// Always setVisible in the end, otherwise you won't see the
		// components that added after setVisible when the first time
		// the frame pops up.
		mainFrame.setVisible(true);
		// mainFrame.pack();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		

	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			this.pm.saveToSERFile("photos.ser");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);

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
