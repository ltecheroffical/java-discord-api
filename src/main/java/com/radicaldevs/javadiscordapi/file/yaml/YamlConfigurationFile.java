package com.radicaldevs.javadiscordapi.file.yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import com.radicaldevs.javadiscordapi.file.ConfigurationFile;

/**
 * 
 * A YAML configuration file utility.
 * 
 * @author Myles Deslippe
 * @since 0.0.2-SNAPSHOT
 * 
 */
public class YamlConfigurationFile extends ConfigurationFile {

	/**
	 * The Serial ID.
	 */
	private static final long serialVersionUID = 7950438595670529199L;

	/**
	 * The YAML configuration.
	 */
	private YamlFile configuration;

	/**
	 * Construct a new yaml configuration file.
	 * 
	 * @param file The file that will be used to serialize and deserialize the
	 *             configuration.
	 */
	public YamlConfigurationFile(File file) {
		super(file);
		this.configuration = new YamlFile(this);
	}

	/**
	 * Construct a new yaml configuration file.
	 * 
	 * @param path A path to the file that will be used to serialize and deserialize
	 *             the configuration.
	 */
	public YamlConfigurationFile(String path) {
		super(path);
		this.configuration = new YamlFile(this);
	}

	/**
	 * Construct a new yaml configuration file..
	 * 
	 * @param path A path to the file that will be used to serialize and deserialize
	 *             the configuration.
	 */
	public YamlConfigurationFile(Path path) {
		super(path);
		this.configuration = new YamlFile(this);
	}

	/**
	 * Get the YAML configuration contained in the configuration file.
	 * 
	 * @return The configuration contained in the configuration file.
	 */
	public YamlFile getConfiguration() {
		return this.configuration;
	}

	@Override
	public synchronized void load() throws IOException {
		try {

			this.configuration.load();

		} catch (InvalidConfigurationException e) {

			throw new IOException(e);

		}
	}

	@Override
	public synchronized void save() {
		this.save();
	}

}