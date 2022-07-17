package com.radicaldevs.javadiscordapi;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import com.radicaldevs.javadiscordapi.command.CommandManager;

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
	 * The bot's token.
	 */
	private final String token;

	/**
	 * The discord API instance.
	 */
	private JDA api;

	/**
	 * The bot's command manager.
	 */
	private CommandManager commandManager;

	/**
	 * Construct a new bot.
	 * 
	 * @param token The bot's token.
	 */
	public Bot(@Nonnull String token) {
		this.token = token;
		this.commandManager = new CommandManager();
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
		builder.disableCache(CacheFlag.STICKER, CacheFlag.EMOJI, CacheFlag.MEMBER_OVERRIDES);
		builder.setBulkDeleteSplittingEnabled(false);

		this.api = builder.build().awaitReady();
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
