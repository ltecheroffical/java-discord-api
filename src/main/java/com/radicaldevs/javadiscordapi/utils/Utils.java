package com.radicaldevs.javadiscordapi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.simpleyaml.configuration.file.YamlConfiguration;

import com.radicaldevs.javadiscordapi.plugin.Plugin;

/**
 * A class containing general utilities.
 * 
 * @author Myles Deslippe
 * @since 0.0.6
 */
public final class Utils {

	/**
	 * The logger's time formatter.
	 */
	private final static SimpleDateFormat timeFormatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

	/**
	 * Log a message to the console.
	 * 
	 * @param message The message to log.
	 */
	public static void info(Object message) {
		System.out.println(timeFormatter.format(Calendar.getInstance().getTime()) + " INFO >> " + String.valueOf(message));
	}

	/**
	 * Log a warning to the console.
	 * 
	 * @param message The message to log.
	 */
	public static void warn(Object message) {
		System.out.println(timeFormatter.format(Calendar.getInstance().getTime()) + " WARNING >> " + String.valueOf(message));
	}

	/**
	 * Lod an error to the console.
	 * 
	 * @param message The error to log.
	 */
	public static void error(Object message) {
		System.out.println(timeFormatter.format(Calendar.getInstance().getTime()) + " ERROR >> " + String.valueOf(message));
	}
	
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
				Utils.error("Could not load " + jarFile.getName() + ", plugin.yml not found.");
				return null;
			}

			// Parse the plugin.yml file.
			InputStream input = jar.getInputStream(pluginYaml);
			YamlConfiguration config = new YamlConfiguration();
			config.loadFromString(new String(input.readAllBytes(), StandardCharsets.UTF_8));
			jar.close();

			// If the main class was not specified.
			if ((main = config.getString("main")) == null) {
				Utils.error("Could not load " + jarFile.getName() + ", main class not specified.");
				return null;
			}

			// If the name of the plugin was not specified.
			if ((name = config.getString("name")) == null) {
				Utils.error("Could not load " + jarFile.getName() + ", plugin name not specified.");
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
				Utils.error("Could not load " + jarFile.getName() + ", the main class does not extend Plugin.");
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
			Utils.error("Could not load " + rawJar + ", " + e.getMessage());
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
			Utils.error("Could not load " + rawJar + ", could not access the main class");
		} catch (ClassNotFoundException | InstantiationException e) {
			Utils.error("Could not load " + rawJar + ", main class not found.");
		} catch (InvocationTargetException | NoSuchMethodException e) {
			Utils.error("Could not load " + rawJar + ", invalid main class.");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return null;
	}

}
