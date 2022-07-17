package com.radicaldevs.javadiscordapi.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * A listener management utility.
 * 
 * @author Myles Deslippe
 * @since 0.0.4-SNAPSHOT
 */
public class ListenerManager {

	/**
	 * The listeners being managed by this manager.
	 */
	private List<Listener> listeners;

	/**
	 * Construct a new listener manager.
	 */
	public ListenerManager() {
		this.listeners = new ArrayList<Listener>();
	}

	/**
	 * Get the listener's that this listener manager is managing.
	 * 
	 * <p>
	 * Note: Modifications to this list will affect the original.
	 * </p>
	 * 
	 * @return The listeners.
	 */
	public List<Listener> getListeners() {
		return this.listeners;
	}

	/**
	 * Add a listener to the listener manager.
	 * 
	 * <p>
	 * Note: Duplicate listeners will be added.
	 * </p>
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(@Nonnull Listener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Check if the listener manager contains a listener.
	 * 
	 * @param listener The listener to check for.
	 * @return If the listener manager contains the listener or not.
	 */
	public boolean containsListener(Listener listener) {
		return this.listeners.contains(listener);
	}

	/**
	 * Remove a listener from the listener manager.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(Listener listener) {
		this.listeners.remove(listener);
	}

}
