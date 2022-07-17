package com.radicaldevs.javadiscordapi.file;

import java.io.File;
import java.util.HashMap;

/**
 * 
 * A utility to manage files.
 * 
 * @author Myles Deslippe
 * @since 0.0.2-SNAPSHOT
 *
 */
public class FileManager {

	/**
	 * The registered files.
	 */
	private HashMap<String, File> files;

	/**
	 * Construct a new file manager.
	 */
	public FileManager() {
		this.files = new HashMap<String, File>();
	}

	/**
	 * Get a file that is managed by the file manager.
	 * 
	 * @param key The key that maps to the file.
	 * 
	 * @return The file.
	 */
	public File getFile(String key) {
		return this.files.get(key);
	}

	/**
	 * Get a HashMap containing all of the files being managed by the file manager.
	 * 
	 * <p>
	 * Note: Modifications to the HashMap will affect the files stored in the file
	 * manager.
	 * </p>
	 * 
	 * @return A HashMap containing all of the files being managed by the file
	 *         manager.
	 */
	public HashMap<String, File> getFiles() {
		return this.files;
	}

	/**
	 * Add a file to be managed by the file manager.
	 * 
	 * @param key  The key that will map to the file.
	 * @param file The file.
	 */
	public void addFile(String key, File file) {
		this.files.put(key, file);
	}

	/**
	 * Remove a file that is being managed by the file manager.
	 * 
	 * @param key The key that maps to the file.
	 */
	public void removeFile(String key) {
		if (this.contains(key))
			this.files.remove(key);
	}

	/**
	 * Check if the key specified maps to a file.
	 * 
	 * @param key The key to check for.
	 * 
	 * @return If the key maps to a file that is managed by the file manager or not.
	 */
	public boolean contains(String key) {
		return this.files.containsKey(key);
	}

	/**
	 * Check if a file is being managed by the file manager.
	 * 
	 * <p>
	 * Note: This method only performs a shallow check; i.e. it only checks if the
	 * object pointers match.
	 * </p>
	 * 
	 * @param file The file to check for.
	 * 
	 * @return If the file is being managed by the file manager or not.
	 */
	public boolean contains(File file) {
		return this.files.containsValue(file);
	}

}