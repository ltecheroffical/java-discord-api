package com.radicaldevs.javadiscordapi.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * A command management utility.
 * 
 * @author Myles Deslippe
 * @since 0.0.3-SNAPSHOT
 */
public class CommandManager {

	/**
	 * The commands being managed by this manager.
	 */
	private List<Command> commands;

	/**
	 * Construct a new command manager.
	 */
	public CommandManager() {
		this.commands = new ArrayList<Command>();
	}

	/**
	 * Get the command's that this command manager is managing.
	 * 
	 * <p>
	 * Note: Modifications to this list will affect the original.
	 * </p>
	 * 
	 * @return The commands.
	 */
	public List<Command> getCommands() {
		return this.commands;
	}

	/**
	 * Add a command to the command manager.
	 * 
	 * <p>
	 * Note: Duplicate commands will be added.
	 * </p>
	 * 
	 * @param command The command to add.
	 */
	public void addCommand(@Nonnull Command command) {
		this.commands.add(command);
	}

	/**
	 * Check if the command manager contains a command.
	 * 
	 * @param command The command to check for.
	 * @return If the command manager contains the command or not.
	 */
	public boolean containsCommand(Command command) {
		return this.commands.contains(command);
	}

	/**
	 * Remove a command from the command manager.
	 * 
	 * @param command The command to remove.
	 */
	public void removeCommand(Command command) {
		this.commands.remove(command);
	}
	
}
