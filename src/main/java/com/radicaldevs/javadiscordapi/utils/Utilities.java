package com.radicaldevs.javadiscordapi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radicaldevs.javadiscordapi.Bot;
import com.radicaldevs.javadiscordapi.plugin.Plugin;

/**
 * A class containing general utilities.
 * 
 * @author Myles Deslippe
 * @since 0.0.6
 */
public final class Utilities {

	/**
	 * The bot's logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Bot.class);

	/**
	 * Load a plugin dynamically.
	 * 
	 * @param rawJar The raw jar file to load.
	 * @return The plugin instance if it was loaded sucessfully, otherwise null.
	 */
	public static Plugin loadPlugin(String rawJar) {
		try {
			// Load the jar file.
			File jarFile = new File(rawJar);
			JarFile jar = new JarFile(rawJar);
			String main, name, description, version, author;

			// Load the plugin.yml file inside the jar file.
			ZipEntry pluginYaml = jar.getEntry("plugin.yml");

			// If the plugin.yml file could not be found.
			if (pluginYaml == null) {
				jar.close();
				logger.error("Could not load " + jarFile.getName() + ", plugin.yml not found.");
				return null;
			}

			// Parse the plugin.yml file.
			InputStream input = jar.getInputStream(pluginYaml);
			YamlConfiguration config = new YamlConfiguration();
			config.loadFromString(new String(input.readAllBytes(), StandardCharsets.UTF_8));
			jar.close();

			// If the main class was not specified.
			if ((main = config.getString("main")) == null) {
				logger.error("Could not load " + jarFile.getName() + ", main class not specified.");
				return null;
			}

			// If the name of the plugin was not specified.
			if ((name = config.getString("name")) == null) {
				logger.error("Could not load " + jarFile.getName() + ", plugin name not specified.");
				return null;
			}

			// Load the rest of the variables.
			description = config.getString("description");
			version = config.getString("version");
			author = config.getString("author");

			// Load the main class.
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { jarFile.toURI().toURL() });
			Class<?> pluginClass = classLoader.loadClass(main);
			Object pl = pluginClass.getDeclaredConstructor().newInstance();

			// If the main class does not extend Plugin.
			if (!(pl instanceof Plugin)) {
				logger.error("Could not load " + jarFile.getName() + ", the main class does not extend Plugin.");
				return null;
			}

			// Case the plugin.
			Plugin plugin = (Plugin) pl;
			File pluginDir = new File("./plugins/" + name);

			if (!pluginDir.exists())
				pluginDir.mkdir();

			// Inject the plugin name.
			Field nameField = pluginClass.getSuperclass().getDeclaredField("name");
			nameField.setAccessible(true);
			nameField.set(plugin, name);
			nameField.setAccessible(false);

			// Inject the plugin description.
			Field descriptionField = pluginClass.getSuperclass().getDeclaredField("description");
			descriptionField.setAccessible(true);
			descriptionField.set(plugin, description);
			descriptionField.setAccessible(false);

			// Inject the plugin version.
			Field versionField = pluginClass.getSuperclass().getDeclaredField("version");
			versionField.setAccessible(true);
			versionField.set(plugin, version);
			versionField.setAccessible(false);

			// Inject the plugin author.
			Field authorField = pluginClass.getSuperclass().getDeclaredField("author");
			authorField.setAccessible(true);
			authorField.set(plugin, author);
			authorField.setAccessible(false);

			// Inject the plugin directory.
			Field directoryField = pluginClass.getSuperclass().getDeclaredField("directory");
			directoryField.setAccessible(true);
			directoryField.set(plugin, pluginDir);
			directoryField.setAccessible(false);

			return plugin;

		} catch (IOException e) {
			logger.error("Could not load " + rawJar + ", " + e.getMessage());
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
			logger.error("Could not load " + rawJar + ", could not access the main class");
		} catch (ClassNotFoundException | InstantiationException e) {
			logger.error("Could not load " + rawJar + ", main class not found.");
		} catch (InvocationTargetException | NoSuchMethodException e) {
			logger.error("Could not load " + rawJar + ", invalid main class.");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return null;
	}

}
