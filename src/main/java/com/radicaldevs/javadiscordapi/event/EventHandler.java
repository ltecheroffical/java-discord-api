package com.radicaldevs.javadiscordapi.event;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

/**
 * An annotation to declare a method as an event handler.
 * 
 * @author Myles Deslippe
 * @since 0.0.2
 */
@Target(METHOD)
public @interface EventHandler {

	/**
	 * The priority of the event handler.
	 */
	EventPriority priority() default EventPriority.NORMAL;

}
