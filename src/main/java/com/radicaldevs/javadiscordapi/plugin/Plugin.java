package com.radicaldevs.javadiscordapi.plugin;

/**
 * A generic plugin class. Extend this class in classes you wish to make
 * plugins.
 * 
 * @author Myles Deslippe
 * @since 0.0.6
 */
public abstract class Plugin {

	/**
	 * The name of the plugin.
	 */
	private String name;

	/**
	 * The plugin's description.
	 */
	private String description;

	/**
	 * The plugin's version.
	 */
	private String version;

	/**
	 * The author of the plugin.
	 */
	private String author;

	/**
	 * Get the name of the plugin.
	 * 
	 * @return The name of the plugin.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get a description of the plugin.
	 * 
	 * @return The plugin's description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the version of the plugin.
	 * 
	 * @return The plugin's version.
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Get the author of the plugin.
	 * 
	 * @return The plugin's author.
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * This method will be invoked when the plugin is enabled.
	 */
	public abstract void onEnable();

	/**
	 * This method will be invoked when the plugin is disabled.
	 */
	public abstract void onDisable();

}
