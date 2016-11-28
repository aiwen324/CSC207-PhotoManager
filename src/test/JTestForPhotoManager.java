package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import manager.PhotoManager;
import photo.Photo;

public class JTestForPhotoManager {
	
	private PhotoManager pm;
	private Photo img1;
	private Photo img2;
	private Photo img3;
	
	@Before
	public void setUp() throws Exception{
		String path = "phototest.ser";
		try {
		pm = new PhotoManager(path);
		} catch (Exception e){
			
		}
		img1 = new Photo("%h/desktop/1.jpg");
		img2 = new Photo("%h/desktop/2.jpg");
		img3 = new Photo("%h/desktop/3.jpg");
		img1.addObserver(pm);
		img2.addObserver(pm);
		img3.addObserver(pm);
		img1.addTag(new String[]{"img1", "red", "1st"});
		img2.addTag(new String[]{"img2", "red", "2nd"});
		img3.addTag(new String[]{"img3", "green", "3rd"});
		pm.saveToSERFile(path);
	}
	
	// Test for add case
	@Test
	public void testForUpdate() {
		Set<String> tagSet = new HashSet<String>();
		img1.addTag(new String[] {"yellow"});
		tagSet.add("img1");
		tagSet.add("img2");
		tagSet.add("img3");
		tagSet.add("red");
		tagSet.add("green");
		tagSet.add("1st");
		tagSet.add("2nd");
		tagSet.add("3rd");
		tagSet.add("yellow");
		assertEquals(tagSet, pm.getTagsInMap());
		
	}
	
	// Test for deleting case
	@Test
	public void testForUpdate2() {
		Set<String> tagSet = new HashSet<String>();
		String[]  tags = new String[] {"red", "green", "1st", "2nd", "3rd"};
		// Test if delete not existing tag would cause error
		img1.deleteTag(new String[]{"not existing tags"});
		img1.deleteTag(new String[] {"img1"});
		img2.deleteTag(new String[] {"img2"});
		img3.deleteTag(new String[] {"img3"});
		tagSet.addAll(Arrays.asList(tags));
		assertEquals(tagSet, pm.getTagsInMap());
	}
	
	// Test for rename the photo
	@Test
	public void testForUpdate3() {
		Date date = new Date();
		img1.renamePhoto(date, "%h/desktop/1.jpg @notimg @blue @RER", false);
		Set<String> tagSet = new HashSet<String>();
		String[] tags = new String[] {"notimg", "blue", "RER", "img2", 
				"red", "2nd", "img3", "green", "3rd"};
		tagSet.addAll(Arrays.asList(tags));
		assertEquals(tagSet, pm.getTagsInMap());
		
	}
	
	// Test for get photo from map(by photopath)
	@Test
	public void testForgetPhoto() {
		Photo tmpimg = new Photo("%h/desktop/1.jpg");
		tmpimg = pm.getPhoto(tmpimg);
		Set<String> tags = new HashSet<>();
		tags.add("img1");
		tags.add("red");
		tags.add("1st");
		assertEquals(tags, tmpimg.getTags());
	}
	
}

	

