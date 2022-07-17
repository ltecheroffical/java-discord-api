package com.radicaldevs.javadiscordapi.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

/**
 * An abstract command class.
 * 
 * @author Myles Deslippe
 * @since 0.0.1
 */
public abstract class Command {

	/**
	 * The name of the command.
	 */
	private String name;

	/**
	 * The description of the command.
	 */
	private String description;

	/**
	 * The command's aliases.
	 */
	private List<String> aliases;

	/**
	 * The command's permission check predicate.
	 */
	Predicate<Member> permissionCheck;

	/**
	 * A list of the command's sub-commands.
	 */
	private List<Command> subCommands;

	/**
	 * Construct a new command.
	 * 
	 * @param name            The name of the command.
	 * @param description     The command's description.
	 * @param aliases         The command's aliases.
	 * @param permissionCheck A predicate to check if the user has access to the
	 *                        command.
	 */
	public Command(String name, String description, List<String> aliases, Predicate<Member> permissionCheck) {
		this.name = name;
		this.description = description;
		this.aliases = aliases;
		this.permissionCheck = permissionCheck;
		this.subCommands = new ArrayList<Command>();
	}

	/**
	 * The method that will be invoked when the user executes a command, and is
	 * permitted to use the command.
	 * 
	 * @param guild      The guild the command was executed in.
	 * @param member     The member that executed the command.
	 * @param channel    The channel the command was executed in.
	 * @param rawMessage The raw command.
	 * @param args       Arguments passed in with the command.
	 */
	public abstract void onCommand(Guild guild, Member member, Channel channel, String rawMessage, String[] args);

	/**
	 * The method that will be invoked when the user executes a command, is not
	 * permitted to use the command.
	 * 
	 * @param guild      The guild the command was executed in.
	 * @param member     The member that executed the command.
	 * @param channel    The channel the command was executed in.
	 * @param rawMessage The raw command.
	 * @param args       Arguments passed in with the command.
	 */
	public abstract void onPermissionDenied(Guild guild, Member member, Channel channel, String rawMessage,
			String[] args);

	/**
	 * Add a subcommand to the command.
	 * 
	 * @param command The subcommand to add.
	 */
	protected void addSubCommand(Command command) {
		this.subCommands.add(command);
	}

	/**
	 * Check if the command has a subcommand.
	 * 
	 * @param command The subcommand to check for.
	 * @return If the command has the subcommand or not.
	 */
	public boolean containsSubCommand(Command command) {
		return this.subCommands.contains(command);
	}

	/**
	 * Remove a subcommand.
	 * 
	 * @param command The subcommand to remove.
	 */
	protected void removeSubCommand(Command command) {
		this.subCommands.remove(command);
	}

}
