package com.radicaldevs.javadiscordapi;

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
	 * Construct a new bot.
	 * 
	 * @param token The bot's token.
	 */
	public Bot(String token) {
		this.token = token;
	}

}
