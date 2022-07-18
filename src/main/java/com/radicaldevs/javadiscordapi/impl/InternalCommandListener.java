package com.radicaldevs.javadiscordapi.impl;

import java.util.Arrays;
import java.util.List;

import com.radicaldevs.javadiscordapi.command.Command;
import com.radicaldevs.javadiscordapi.command.CommandManager;
import com.radicaldevs.javadiscordapi.event.EventHandler;
import com.radicaldevs.javadiscordapi.event.Listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.radicaldevs.javadiscordapi.event.EventPriority;

/**
 * The api's internal command listener.
 * 
 * @author Myles Deslippe
 * @since 0.0.2
 */
public class InternalCommandListener implements Listener {

	/**
	 * Prefixes that trigger the internal command listener.
	 */
	private List<String> prefixes;

	/**
	 * A command manager containing commands that the internal command listener will
	 * trigger.
	 */
	private CommandManager commandManager;

	/**
	 * Construct an internal command listener.
	 * 
	 * @param prefixes Prefixes that trigger the command listener.
	 * @param The command manager whose commands will be listened for.
	 */
	public InternalCommandListener(List<String> prefixes, CommandManager commandManager) {
		this.prefixes = prefixes;
		this.commandManager = commandManager;
	}

	/**
	 * The event handler that trigger command listeners.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(MessageReceivedEvent event) {
		// Ensure the message was a command.
		boolean isCmd = false;
		String message = event.getMessage().getContentRaw();
		
		for (String prefix : this.prefixes) {
			if (message.startsWith(prefix.toLowerCase())) {
				message = message.substring(prefix.length());
				isCmd = true;
				break;
			}
		}
		
		String[] messageSplit = message.split(" ");
		
		if (!isCmd)
			return;
		
		// Loop through all the registered commands, if they match execute them.
		for (Command command : this.commandManager.getCommands()) {
			// Check if the command name matches.
			if (command.getName().equals(messageSplit[0])) {
				command.internalCommandHandler(
					event.getGuild(), 
					event.getMember(), 
					event.getChannel(), 
					event.getMessage(),
					Arrays.copyOfRange(messageSplit, 1, messageSplit.length)
				);
				continue;
			}

			// Check if one of the command's aliases match.
			for (String aliases : command.getAliases()) {
				if (aliases.equals(messageSplit[0])) {
					command.internalCommandHandler(
						event.getGuild(), 
						event.getMember(), 
						event.getChannel(), 
						event.getMessage(),
						Arrays.copyOfRange(messageSplit, 1, messageSplit.length)
					);
					break;
				}
			}
		}
	}

}
