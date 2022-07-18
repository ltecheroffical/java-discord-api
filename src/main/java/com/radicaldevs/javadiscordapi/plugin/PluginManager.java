package com.radicaldevs.javadiscordapi.plugin;

import java.util.ArrayList;
import java.util.HashMap;

import com.radicaldevs.javadiscordapi.command.Command;
import com.radicaldevs.javadiscordapi.command.CommandManager;
import com.radicaldevs.javadiscordapi.event.Listener;
import com.radicaldevs.javadiscordapi.event.ListenerManager;

/**
 * A plugin management utility.
 * 
 * @author Myles Deslippe
 * @since 0.0.6
 */
public class PluginManager {

	/**
	 * The plugins the plugin manager is managing.
	 */
	private ArrayList<Plugin> registeredPlugins;

	/**
	 * The plugin manager's listener manager.
	 */
	private ListenerManager listenerManager;

	/**
	 * The plugin manager's command manager.
	 */
	private CommandManager commandManager;

	/**
	 * The listener map.
	 */
	private HashMap<Listener, Plugin> listenerMap;

	/**
	 * The command map.
	 */
	private HashMap<Command, Plugin> commandMap;

	/**
	 * Construct a plugin manager.
	 * 
	 * @param listenerManager The listener manager.
	 * @param commandManager  The command manager.
	 */
	public PluginManager(ListenerManager listenerManager, CommandManager commandManager) {
		this.registeredPlugins = new ArrayList<Plugin>();
		this.listenerManager = listenerManager;
		this.commandManager = commandManager;
		this.listenerMap = new HashMap<Listener, Plugin>();
		this.commandMap = new HashMap<Command, Plugin>();
	}

	/**
	 * Register a plugin with the plugin manager.
	 * 
	 * @param plugin The plugin to register.
	 */
	public synchronized void registerPlugin(Plugin plugin) {
		if (this.containsPlugin(plugin.getName()))
			throw new PluginNameInUseException("A plugin is already registered with the name " + plugin.getName());

		this.registeredPlugins.add(plugin);
		plugin.onEnable();
	}

	/**
	 * Get a plugin by it's name.
	 * 
	 * @param name The name of the plugin.
	 * @return The plugin if it is registered, otherwise null.
	 */
	public Plugin getPlugin(String name) {
		for (Plugin plugin : this.registeredPlugins)
			if (plugin.getName().equalsIgnoreCase(name))
				return plugin;

		return null;
	}

	/**
	 * Check if a plugin is registered with the plugin manager by it's name.
	 * 
	 * @param name The name of the plugin.
	 * @return If it is registered with the plugin manager or not.
	 */
	public boolean containsPlugin(String name) {
		return this.getPlugin(name) != null;
	}

	/**
	 * Check if the plugin manager contains a plugin.
	 * 
	 * @param plugin The plugin to check for.
	 * @return If the plugin manager contains the plugin.
	 */
	public boolean containsPlugin(Plugin plugin) {
		return this.registeredPlugins.contains(plugin);
	}

	/**
	 * Reload a plugin.
	 * 
	 * <p>
	 * Reloading plugins may cause issues.
	 * </p>
	 * 
	 * @param plugin The plugin to reload.
	 */
	public void reloadPlugin(Plugin plugin) {
		if (this.containsPlugin(plugin)) {
			plugin.onDisable();
			plugin.onEnable();
		}
	}

	/**
	 * Unload all of the plugins.
	 * 
	 * <p>
	 * This will unregister all of their commands and listeners.
	 * </p>
	 */
	public void unloadAllPlugins() {
		for (Listener listener : this.listenerMap.keySet())
			this.listenerManager.removeListener(listener);

		for (Command command : this.commandMap.keySet())
			this.commandManager.removeCommand(command);

		for (Plugin plugin : this.registeredPlugins)
			plugin.onDisable();
	}

	/**
	 * Remove a plugin from the plugin manager.
	 * 
	 * @param plugin The plugin to remove.
	 */
	public synchronized void removePlugin(Plugin plugin) {
		plugin.onDisable();
		this.registeredPlugins.remove(plugin);
	}

	/**
	 * Register a listener with the plugin manager.
	 * 
	 * @param plugin   The plugin the listener is bound to.
	 * @param listener The listener being registered.
	 */
	public void registerListener(Plugin plugin, Listener listener) {
		if (!this.containsPlugin(plugin))
			throw new IllegalStateException("Plugin not registered");

		this.listenerMap.put(listener, plugin);
		this.listenerManager.addListener(listener);
	}

	/**
	 * Check if a plugin has a listener registered to it.
	 * 
	 * @param plugin   The plugin.
	 * @param listener The listener to check for.
	 * @return If the plugin has the listener or not.
	 */
	public boolean containsListener(Plugin plugin, Listener listener) {
		return this.listenerMap.get(listener) == plugin;
	}

	/**
	 * Unregister a listener from the plugin manager.
	 * 
	 * @param plugin   The plugin the listener is bound to.
	 * @param listener The listener.
	 */
	public void unregisterListener(Plugin plugin, Listener listener) {
		if (this.listenerMap.remove(listener, plugin))
			this.listenerManager.removeListener(listener);
	}

	/**
	 * Register a command with the plugin manager.
	 * 
	 * @param plugin  The plugin that the command is bound to.
	 * @param command The command being registered.
	 */
	public void registerCommand(Plugin plugin, Command command) {
		if (!this.containsPlugin(plugin))
			throw new IllegalStateException("Plugin not registered");

		this.commandMap.put(command, plugin);
		this.commandManager.addCommand(command);
	}

	/**
	 * Check if the plugin manager has a command registered to a plugin.
	 * 
	 * @param plugin  The plugin.
	 * @param command The command to check for.
	 * @return If the plugin has the command registered.
	 */
	public boolean containsCommand(Plugin plugin, Command command) {
		return this.commandMap.get(command) == plugin;
	}

	/**
	 * Unregister a command from the plugin manager.
	 * 
	 * @param plugin  The plugin.
	 * @param command The command to unregister.
	 */
	public void unregisterCommand(Plugin plugin, Command command) {
		if (this.commandMap.remove(command, plugin))
			this.commandManager.removeCommand(command);
	}

}
