package com.radicaldevs.javadiscordapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radicaldevs.javadiscordapi.command.CommandManager;
import com.radicaldevs.javadiscordapi.event.ListenerManager;
import com.radicaldevs.javadiscordapi.impl.InternalCommandListener;
import com.radicaldevs.javadiscordapi.impl.InternalEventHandler;
import com.radicaldevs.javadiscordapi.plugin.Plugin;
import com.radicaldevs.javadiscordapi.plugin.PluginManager;
import com.radicaldevs.javadiscordapi.plugin.PluginNameInUseException;
import com.radicaldevs.javadiscordapi.utils.Utilities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * A generic bot class.
 * 
 * @author Myles Deslippe
 * @since 0.0.1
 */
public abstract class Bot {

	/**
	 * The bot's logger.
	 */
	private Logger logger;

	/**
	 * The bot's token.
	 */
	private final String token;

	/**
	 * The bot's command prefixes.
	 */
	private List<String> commandPrefixes;

	/**
	 * The discord API instance.
	 */
	private JDA api;

	/**
	 * The bot's command manager.
	 */
	private CommandManager commandManager;

	/**
	 * The bot's listener manager.
	 */
	private ListenerManager listenerManager;

	/**
	 * The bot's plugin manager.
	 */
	private PluginManager pluginManager;

	/**
	 * The bot's internal command listener.
	 */
	private InternalCommandListener internalCommandListener;

	/**
	 * Tht bot's internal event handler.
	 */
	private InternalEventHandler internalEventHandler;

	/**
	 * The directory where plugins will be loaded from.
	 */
	private File pluginDirectory;

	/**
	 * Construct a new bot.
	 * 
	 * @param token    The bot's token.
	 * @param prefixes The bot's command prefixes.
	 */
	public Bot(@Nonnull String token, List<String> prefixes) {
		this.logger = LoggerFactory.getLogger(Bot.class);
		this.token = token;
		this.commandPrefixes = prefixes;
		this.commandManager = new CommandManager();
		this.listenerManager = new ListenerManager();
		this.pluginManager = new PluginManager(this.listenerManager, this.commandManager);

		
		this.internalCommandListener = new InternalCommandListener(this.commandPrefixes, this.commandManager);
		this.internalEventHandler = new InternalEventHandler(this.listenerManager);

		this.pluginDirectory = new File("./plugins");
		
		this.listenerManager.addListener(this.internalCommandListener);
	}

	/**
	 * Get the bot's logger.
	 * 
	 * @return The bot's logger.
	 */
	private Logger getLogger() {
		return this.logger;
	}

	/**
	 * The bot's command prefixes.
	 * 
	 * <p>
	 * Note: Modifications to this list will affect the command prefixes.
	 * </p>
	 * 
	 * @return The command prefixes.
	 */
	public List<String> getCommandPrefixes() {
		return this.commandPrefixes;
	}

	/**
	 * Get the api instance.
	 * 
	 * <p>
	 * Note: This will be null if {@link #start()} has not been called.
	 * </p>
	 * 
	 * @return The api instance.
	 */
	public JDA getAPI() {
		return this.api;
	}

	/**
	 * Get the bot's command manager.
	 * 
	 * @return The bot's command manager.
	 */
	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	/**
	 * Get the bot's event manager.
	 * 
	 * @return The bot's event manager.
	 */
	public ListenerManager getEventManager() {
		return this.listenerManager;
	}

	/**
	 * Get the bot's plugin manager.
	 * 
	 * @return The bot's plugin manager.
	 */
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	/**
	 * Get the directory where plugins will be loaded from.
	 * 
	 * @return The plugin directory.
	 */
	public File getPluginDirectory() {
		return this.pluginDirectory;
	}

	/**
	 * Load the plugins.
	 * 
	 * <p>
	 * If the plugin directory does not exist, this will generate it.
	 * </p>
	 */
	private void loadPlugins() {
		// If the plugins directory does not exist.
		if (!this.getPluginDirectory().exists()) {
			this.getPluginDirectory().mkdir();
			return;
		}

		// Walk the plugins directory, and load the plugins.
		try {
			Files.walk(this.getPluginDirectory().toPath(), 1).filter(file -> file.toString().endsWith(".jar"))
					.forEach(jar -> {
						Plugin plugin = Utilities.loadPlugin(jar.toString());

						if (plugin == null)
							return;

						try {
							if (plugin != null)
								this.getPluginManager().registerPlugin(plugin);

							String successMessage = "Loaded " + plugin.getName();

							if (plugin.getVersion() != null)
								successMessage += " v" + plugin.getVersion();

							if (plugin.getAuthor() != null)
								successMessage += " by " + plugin.getAuthor();

							successMessage += ".";

							this.getLogger().info(successMessage);
						} catch (PluginNameInUseException e) {
							this.getLogger()
									.error("Could not load " + jar.toString() + ", plugin name already in use.");
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unload all of the plugins.
	 */
	private void unloadPlugins() {
		this.getPluginManager().unloadAllPlugins();
	}

	/**
	 * Start the bot.
	 * 
	 * @throws LoginException        If the bot could not be authenticated with
	 *                               discord's servers.
	 * @throws InterruptedException  If the thread was interrupted while attempting
	 *                               to login.
	 * @throws IllegalStateException if the bot is already running.
	 */
	public void start() throws LoginException, InterruptedException {
		if (this.getAPI() != null)
			throw new IllegalStateException("The bot is already running");

		JDABuilder builder = JDABuilder.createDefault(this.token);
		builder.addEventListeners(this.internalEventHandler);
		builder.disableCache(CacheFlag.STICKER, CacheFlag.EMOJI, CacheFlag.MEMBER_OVERRIDES);
		builder.setBulkDeleteSplittingEnabled(false);

		this.api = builder.build().awaitReady();
		this.loadPlugins();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.unloadPlugins();
		}));
	}

	/**
	 * Stop the bot.
	 * 
	 * @param now If the bot should stop immedately, or processes all queued events
	 *            before stopping.
	 * 
	 * @throws IllegalStateException if the bot is not running.
	 */
	public void stop(boolean now) {
		if (this.getAPI() == null)
			throw new IllegalStateException("The bot is not running");

		if (now)
			this.api.shutdownNow();
		else
			this.api.shutdown();
	}

}
