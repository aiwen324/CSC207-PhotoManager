package photorenamer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import manager.PhotoManager;
import photo.Photo;

public class TagList extends JPanel implements ListSelectionListener {

	// Passed by it's frame
	private Photo img;
	private PhotoManager pm;

	// All tags that photomanager has
	public Set<String> alltags;

	// public Collection<String> name;
	public Set<String> tagsPhotoHas;

	// The tags exsist in the photomanager but the photo has
	public JComboBox<String> exsistingTags = new JComboBox<>();

	// public Set<String> nameHistory;

	// A button to delete tag
	public JButton delete = new JButton("delete");

	// A button to add tag
	public JButton add = new JButton("add");

	// The list of tags
	private JList list = new JList();
	
	private DefaultListModel<String> listModel;
	
	private DefaultComboBoxModel<String> comboboxModel;

	private JComboBox<String> nameHistory;
	
	public TagList(PhotoManager pm, Photo img, JComboBox<String> nameHistory) {
		setLayout(new BorderLayout());
		this.nameHistory = nameHistory;
		this.pm = pm;
		this.img = img;
		this.listModel = new DefaultListModel<>();
		add.setEnabled(false);
		loadTags();
		// This allow user select one item each time
		// The method in Photo does support change the selection mode
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(this);
		// Add scroll pane in case there are too many tags
		JScrollPane listScrollPane = new JScrollPane(this.list);
		this.exsistingTags.setEditable(true);
		// Add tooltip for add text
		add.setToolTipText("add tags you select or write");
		delete.setToolTipText("delete the tag you select");
		// Add some listener
		this.add.addActionListener(new AddTagListener());
		this.delete.addActionListener(new DeleteTagListener());
		((JTextField) this.exsistingTags.getEditor().getEditorComponent()).getDocument()
				.addDocumentListener(new TextAreaListener());
		// This is the bottomPanel of this taglist panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(add, BorderLayout.WEST);
		bottomPanel.add(this.exsistingTags, BorderLayout.CENTER);
		bottomPanel.add(delete, BorderLayout.EAST);
		
		add(listScrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

	}

	public void updateTags(String name) {
		Date date = new Date();
		this.img.renamePhoto(date, name, false);
		loadTags();
	}

	// Something to note: return a collection in a class, the reference
	// still point to the original collection, it doesn't make a new copy
	// so remember to make new copy if you don't want to change things
	// in original list
	public void loadTags() {
		System.out.println("loadTags method is called");
		this.alltags = new HashSet<String>(this.pm.getTagsInMap());
		// TODO:
		System.out.println(alltags);
		System.out.println(this.pm);
		this.tagsPhotoHas = new HashSet<String>(this.img.getTags());
		// TODO:
		System.out.println(tagsPhotoHas);
		System.out.println(this.img);
		// Remove the tags that photo has and let exsistingTags get the
		// array of tags
		for (String tag : this.tagsPhotoHas) {
			if (this.alltags.contains(tag)) {
				this.alltags.remove(tag);
			}
		}
		// Let JComboBox load the tags but photo has
		String[] tagsArray = (String[]) this.alltags.toArray(new String[this.alltags.size()]);

		this.comboboxModel = new DefaultComboBoxModel(tagsArray);
		this.exsistingTags.setModel(comboboxModel);
		this.exsistingTags.setSelectedIndex(-1);
		// Let JList load the tags photo has
		String[] tagInPhoto = this.tagsPhotoHas.toArray(new String[this.tagsPhotoHas.size()]);

		this.listModel.removeAllElements();
		for(String i:this.tagsPhotoHas){
			this.listModel.addElement(i);
		}
		this.list.setModel(this.listModel);
		if (tagInPhoto.length == 0) {
			delete.setEnabled(false);
		} else {
			list.setSelectedIndex(0);
		}
		Collection<String> namecol = img.getAllHistoryNames();
		String[] nameHis = namecol.toArray(new String[namecol.size()]);
		DefaultComboBoxModel<String> tmpcombmod = new DefaultComboBoxModel<>(nameHis);
		this.nameHistory.setModel(tmpcombmod);
	}

	// This listener is used for deleting tags
	class DeleteTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: Delete it after debug
			System.out.println("delete bottom pressed");
			// it will help JList to setSelectedIndex
			int index = list.getSelectedIndex();
			// The method in photo supports to delete several tags
			// in one time
			String deleteTag = (String) list.getSelectedValue();
			String[] deleteTags = { deleteTag };
			// Let img delete the tag we select, it has renamePhoto
			// method inside
			img.deleteTag(deleteTags);
			// Let photomanager update the img
			// upadate JComboBox and JList in this JPanel
			loadTags();
			// check some problem in case messing up the program
			int size = tagsPhotoHas.size();
			if (size == 0) {
				delete.setEnabled(false);
			} else {// make sure the it would not catch the
				// exception
				if (index == tagsPhotoHas.size()) {
					index--;
				}
			}
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}

	}

	// This listener is for adding tag
	class AddTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: Delete it
			System.out.println("add bottom pressed");
			String addtag = (String) exsistingTags.getSelectedItem();
			if (addtag.trim().length() == 0 || tagsPhotoHas.contains(addtag)) {
				Toolkit.getDefaultToolkit().beep();
				exsistingTags.getEditor().selectAll();
				return; // End this process
			}
			String[] addTags = { addtag };
			// Let img add the tag we select from JComboBox or
			// we write
			img.addTag(addTags);
			// TODO
			System.out.println(img);
			// Update the photoManager with the img
			System.out.println(pm);
			// upadate JComboBox and JList in this JPanel
			loadTags();
			int index = tagsPhotoHas.size();
			list.setSelectedIndex(index - 1);
			list.ensureIndexIsVisible(index - 1);
			exsistingTags.setSelectedIndex(-1);

		}

	}

	class TextAreaListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			add.setEnabled(true);

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			add.setEnabled(e.getDocument().getLength() > 0);

		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			add.setEnabled(e.getDocument().getLength() > 0);

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				// No selection, disable fire button.
				this.delete.setEnabled(false);

			} else {
				// Selection, enable the fire button.
				this.delete.setEnabled(true);
			}
		}
	}
}
