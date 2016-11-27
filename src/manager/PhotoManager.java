package manager;

import photo.Photo;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Manages the saving and loading of Photos. Have refactored it as observer design pattern.
 */
public class PhotoManager implements Observer{

	// Using set to avoid duplicated in one tag.
	private Map<String, Set<Photo>> taggedPhoto;

	// Read the file from certain path.
	public PhotoManager(String filePath) throws ClassNotFoundException, IOException {
		taggedPhoto = new HashMap<String, Set<Photo>>();
		// Use this key to store the photo which exsited in the map
		// But maybe not exsists in map right now, this tag won't be showed
		// in GUI. (Encounter a bug in GUI part when delete all tags of 
		// the photo, the history name will disappear, so add this 
		// to fix the bug)
		taggedPhoto.put("Photo existed in map", new HashSet<Photo>());
		File file = new File(filePath);
		if (file.exists()) {
			readFromSERFile(filePath);
		} else {
			file.createNewFile();
		}
	}

	// Read from '.ser' file at the given path and put it into map.
	public void readFromSERFile(String path) throws ClassNotFoundException {
		try {
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			taggedPhoto = (Map<String, Set<Photo>>) input.readObject();
			input.close();
			MyLogging.log(Level.FINE, "Read the map from .ser file" + "\n");
		} catch (IOException e) {
			MyLogging.log(Level.SEVERE, "Cannot read from input.", e);
		}
	}

	// Save the modified map to '.ser' file.
	public void saveToSERFile(String path) throws IOException {
		try {
			OutputStream file = new FileOutputStream(path);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(taggedPhoto);
			output.close();
			MyLogging.log(Level.FINE, "Write new map to the .ser file" + "\n");
		} catch (Exception e) {
			MyLogging.log(Level.SEVERE, "Cannot write to file.", e);
		}
	}
	
	
	@Override
	/**
	 * Add the given photo to the map. It is possible the photo is already
	 * in the map. We may just want to add the new tag for the photo.
	 * 
	 * @param photo:
	 *                photo in Photo class.
	 */
	public void update(Observable o, Object arg) {
		String tag1 = "Photo existed in map";
		Set<String> tags = ((Photo) arg).getTags();
		// This part is to delete the tags.
		// Make a copy of map otherwise it may catch 
		// ConcurrentModificationException
		HashMap<String, Set<Photo>> newmp = new HashMap<String, Set<Photo>>(this.taggedPhoto);
		taggedPhoto.keySet().remove(tag1);
		for (String tag:taggedPhoto.keySet()){
			if(!tags.contains(tag) && newmp.get(tag).contains(arg)){
				newmp.get(tag).remove(arg);
				if(newmp.get(tag).isEmpty()){
					newmp.remove(tag);
					MyLogging.log(Level.FINE, "Remove the tag: "
							+ tag + " whose value is empty." + "\n");
				}
				MyLogging.log(Level.FINE, "Remove the tag: " + 
						tag + " from " + ((Photo) arg).getNameWOTags() + "\n");
			}
		}
		this.taggedPhoto = newmp;
		for (String tag : tags) {
			if (taggedPhoto.containsKey(tag)) {
				Set<Photo> photos = taggedPhoto.get(tag);
				// This will remove the photo with old tags
				photos.remove(arg);
				// This will add the photo with new tags
				photos.add((Photo) arg);
				taggedPhoto.put(tag, photos);
				MyLogging.log(Level.FINE, "Add the photo " + 
						((Photo) arg).getNameWOTags() + 
						" to the tag: " + tag + "\n");
			// If the tag doesn't exist in map's keyset,
			// then we
			// should create a new set for it.
			} else {
				Set<Photo> newsets = new HashSet<>();
				newsets.add((Photo) arg);
				taggedPhoto.put(tag, newsets);
				MyLogging.log(Level.FINE, "Add the photo " + 
						((Photo) arg).getNameWOTags() + 
						" to a new tag: " + tag + "\n");
			}
			// Update the photo in this special tags
			
			this.taggedPhoto.get(tag1).remove(arg);
			this.taggedPhoto.get(tag1).add((Photo) arg);
		}
	}

	/**
	 * Get all tagged photo in the map. It is probably good to do this in
	 * AdditionalManagerFunction Class.
	 * 
	 * @return Set<Photo>
	 */
	public Set<Photo> getAllPhotos() {
		// The reason using Set is it is easy to get rid of
		// repeated photos.
		Set<Photo> photos = new HashSet<Photo>();
		for (Set<Photo> subphotos : taggedPhoto.values()) {
			photos.addAll(subphotos);
		}
		return photos;
	}

	/**
	 * This method is used in the main class which every time when user
	 * reopen the app and select the photo, it will check if the map has
	 * already saved the photo, if the map has the photo, it will return the
	 * photo with the same path in map, if the map doesn't have the given
	 * photo(path of photo), it will just return the given photo
	 * 
	 * @param photo:
	 *                the new instantiated photo
	 */
	public Photo getPhoto(Photo photo) {
		Set<Photo> phtsinmap = getAllPhotos();
		for (Photo photoInMap : phtsinmap) {
			if (photoInMap.equals(photo)) {
				MyLogging.log(Level.FINE, "Find the photo: " + 
						photoInMap.getNameWOTags() + 
						" in the map. Returning the one"
						+ " in map" + "\n");
				return photoInMap;
				
			}
		}
		MyLogging.log(Level.FINE, "The photo: " + photo.getNameWOTags()
				+ " is not in the map. Returning the one new "
				+ "instantiate" + "\n");
		return photo;
	}
	// Remove the tag that unnecessary for user
	public Set<String> getTagsInMap(){
		String tag1 = "Photo existed in map";
		HashSet<String> tagsSets  = new HashSet<>(taggedPhoto.keySet());
		tagsSets.remove(tag1);
		return tagsSets;
	}

	public Map<String, Set<Photo>> getTagedphts() {
		return this.taggedPhoto;
	}
	
	@Override
	public String toString(){
		String tmpString = "{";
		Set<String> theTag = getTagsInMap();
		for (String tag: theTag){
			tmpString = tmpString + tag + "=[";
			Set<Photo> phts = taggedPhoto.get(tag);
			for (Photo photo: phts){
				tmpString = tmpString + photo.getPhotoPath() + ", ";
			}
			tmpString = tmpString.substring(0, tmpString.length()-2);
			tmpString = tmpString + "], \n";
		}
		if (tmpString.length() > 1){
		tmpString = tmpString.substring(0, tmpString.length()-3);
		}
		tmpString = tmpString + "}";
		return tmpString;
	}



}
