package com.radicaldevs.javadiscordapi.file;

import java.io.File;
import java.nio.file.Path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * 
 * A generic configuration file.
 * 
 * @author Myles Deslippe
 * @since 0.0.2-SNAPSHOT
 * 
 */
public abstract class ConfigurationFile extends File {

	/**
	 * The Serial ID.
	 */
	private static final long serialVersionUID = 6450895964082971119L;

	/**
	 * The default file configuration.
	 */
	private File defaults;

	/**
	 * Construct a new configuration file.
	 * 
	 * @param file The file that will be used to serialize and deserialize the
	 *             configuration.
	 */
	public ConfigurationFile(File file) {
		super(file.toString());
		this.defaults = null;
	}

	/**
	 * Construct a new configuration file.
	 * 
	 * @param path A path to the file that will be used to serialize and deserialize
	 *             the configuration.
	 */
	public ConfigurationFile(String path) {
		super(path);
		this.defaults = null;
	}

	/**
	 * Construct a new configuration file.
	 * 
	 * @param path A path to the file that will be used to serialize and deserialize
	 *             the configuration.
	 */
	public ConfigurationFile(Path path) {
		super(path.toFile().toString());
		this.defaults = null;
	}

	/**
	 * Get the default file that will generated if the file is created.
	 * 
	 * @return The default file that will be generated.
	 */
	public File getDefaults() {
		return this.defaults;
	}

	/**
	 * Set the default file that will be generated if the file is created.
	 * 
	 * @param defaults The default file that will be generated.
	 */
	public void setDefaults(File defaults) {
		this.defaults = defaults;
	}

	/**
	 * Create the file.
	 * 
	 * <p>
	 * The default configuration will be copied if it exists.
	 * </p>
	 * 
	 * <p>
	 * <strong>Note:</strong> This will overwrite the file if it already exists, be
	 * sure the check if it {@link #exists()} before calling the method.
	 * </p>
	 * 
	 * @throws IOException If the file could not be created, or the default
	 *                     configuration could not be found.
	 */
	public synchronized void create() throws IOException {
		this.copyDefaults();
	}

	/**
	 * Create the file if it does not exist.
	 * 
	 * <p>
	 * The default configuration will be copied if it exists.
	 * </p>
	 * 
	 * @throws IOException If the file could not be created, or the default
	 *                     configuration could not be found.
	 */
	public synchronized void createIfNotExists() throws IOException {
		if (!this.exists())
			this.create();
	}

	/**
	 * Load the configuration file.
	 * 
	 * @throws IOException If the file could not be loaded.
	 */
	public abstract void load() throws IOException;

	/**
	 * Save the configuration file.
	 * 
	 * @throws IOException If the file could not be saved.
	 */
	public abstract void save();

	/**
	 * Copy the default configuration.
	 * 
	 * <p>
	 * <strong>Note:</strong> This will overwrite the file if it already exists.
	 * </p>
	 * 
	 * @throws IOException If the file could not be created, or the default
	 *                     configuration could not be found.
	 */
	public synchronized void copyDefaults() throws IOException {
		if (this.defaults == null)
			this.createNewFile();
		else
			Files.copy(defaults.toPath(), this.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

}