package com.radicaldevs.javadiscordapi.plugin;

/**
 * An exception to denote a plugin name is already in use.
 * 
 * @author Myles Deslippe
 * @since 0.0.6
 */
public class PluginNameInUseException extends RuntimeException {

	/**
	 * The exception's serial UID.
	 */
	private static final long serialVersionUID = -3897241270314257299L;

	/**
	 * Construct a new plugin name in use exception.
	 */
	public PluginNameInUseException() {
		super();
	}

	/**
	 * Construct a new plugin name in use exception.
	 * 
	 * @param message A message to pass along with the exception.
	 */
	public PluginNameInUseException(String message) {
		super(message);
	}

}
