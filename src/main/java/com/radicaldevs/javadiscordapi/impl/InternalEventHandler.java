package com.radicaldevs.javadiscordapi.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
		String genericType = event.getClass().getName();
		Class<?> genericClass = event.getClass();
		
		// Seperate the handlers based on their priority.
		List<Method> highestPriority = new ArrayList<Method>();
		List<Method> highPriority = new ArrayList<Method>();
		List<Method> normalPriority = new ArrayList<Method>();
		List<Method> lowPriority = new ArrayList<Method>();
		List<Method> lowestPriority = new ArrayList<Method>();
		
		for (Listener listener : this.listenerManager.getListeners()) {
			for (Method method : listener.getClass().getMethods()) {
				if (method.getParameterCount() == 0)
					continue;
				
				if (method.getDeclaredAnnotation(EventHandler.class) != null && method.getParameters()[0].getClass().getName().equals(genericType)) {
					
					switch (method.getDeclaredAnnotation(EventHandler.class).priority()) {
					case HIGHEST:
						highestPriority.add(method);
						break;
					case HIGH:
						highPriority.add(method);
						break;
					case NORMAL:
						normalPriority.add(method);
						break;
					case LOW:
						lowPriority.add(method);
						break;
					case LOWEST:
						lowestPriority.add(method);
						break;
					}	
				}
			}
		}
		
		// Run the event listeners.
		for (Method method : highestPriority) 
			this.run(method, genericClass, event);
		
		for (Method method : highPriority) 
			this.run(method, genericClass, event);
		
		for (Method method : normalPriority) 
			this.run(method, genericClass, event);
		
		for (Method method : highestPriority) 
			this.run(method, genericClass, event);
		
		for (Method method : highestPriority) 
			this.run(method, genericClass, event);
	}

	/**
	 * Run a {@link EventHandler}.
	 * @param method The event handler method.
	 * @param genericClass The class type of the event.
	 * @param event The event.
	 */
	private void run(Method method, Class<?> eventType, GenericEvent event) {
		Runnable runnable = () ->  {
			try {
				method.invoke(null, eventType.cast(event));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		};
		
		runnable.run();
	}
	
}
