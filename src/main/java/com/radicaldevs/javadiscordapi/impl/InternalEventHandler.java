package com.radicaldevs.javadiscordapi.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import com.radicaldevs.javadiscordapi.event.EventHandler;
import com.radicaldevs.javadiscordapi.event.Listener;
import com.radicaldevs.javadiscordapi.event.ListenerManager;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * The api's internal event listener.
 * 
 * @author Myles
 * @since 0.0.2
 */
public class InternalEventHandler implements EventListener {

	/**
	 * The listener's the internal event handler is proxying events for.
	 */
	private ListenerManager listenerManager;

	/**
	 * Construct a new internal event handler.
	 * 
	 * @param listenerManager A listener manager containing listeners for events
	 *                        that will be proxied by the internal event handler.
	 */
	public InternalEventHandler(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
	}

	@Override
	public void onEvent(GenericEvent event) {
		Class<?> genericClass = event.getClass();

		// Seperate the handlers based on their priority.
		HashMap<Method, Listener> highestPriority = new HashMap<Method, Listener>();
		HashMap<Method, Listener> highPriority = new HashMap<Method, Listener>();
		HashMap<Method, Listener> normalPriority = new HashMap<Method, Listener>();
		HashMap<Method, Listener> lowPriority = new HashMap<Method, Listener>();
		HashMap<Method, Listener> lowestPriority = new HashMap<Method, Listener>();

		for (Listener listener : this.listenerManager.getListeners()) {
			for (Method method : listener.getClass().getMethods()) {
				if (method.getParameterCount() == 0)
					continue;

				if (method.getDeclaredAnnotation(EventHandler.class) != null && genericClass.toString() .equals(method.getParameters()[0].getParameterizedType().toString())) {
					switch (method.getDeclaredAnnotation(EventHandler.class).priority()) {
					case HIGHEST:
						highestPriority.put(method, listener);
						break;
					case HIGH:
						highPriority.put(method, listener);
						break;
					case NORMAL:
						normalPriority.put(method, listener);
						break;
					case LOW:
						lowPriority.put(method, listener);
						break;
					case LOWEST:
						lowestPriority.put(method, listener);
						break;
					}
				}
			}
		}

		// Run the event listeners.
		this.run(event, highestPriority);
		this.run(event, highPriority);
		this.run(event, normalPriority);
		this.run(event, lowPriority);
		this.run(event, lowestPriority);
	}

	/**
	 * Run a {@link EventHandler}.
	 * 
	 * @param method       The event handler method.
	 * @param genericClass The class type of the event.
	 * @param event        The event.
	 */
	private void run(GenericEvent event, HashMap<Method, Listener> map) {
		for (Entry<Method, Listener> entry : map.entrySet()) {
			Runnable runnable = () -> {
				try {
						entry.getKey().invoke(entry.getValue(), event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			};
	
			runnable.run();
		}
	}

}
