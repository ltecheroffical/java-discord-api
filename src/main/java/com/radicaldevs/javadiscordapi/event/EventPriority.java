package com.radicaldevs.javadiscordapi.event;

/**
 * Event priorities.
 * 
 * @author Myles Deslippe
 * @since 0.0.2
 */
public enum EventPriority {
	
	/**
	 * The highest event priority.
	 */
	HIGHEST,

	/**
	 * An event priority slightly higher than normal.
	 */
	HIGH,

	/**
	 * The default event priority.
	 */
	NORMAL,

	/**
	 * An event priority slightly lower than normal.
	 */
	LOW,

	/**
	 * The lowest event priority.
	 */
	LOWEST;

}
