package com.radicaldevs.javadiscordapi.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.radicaldevs.javadiscordapi.utils.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

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
	private Predicate<Member> permissionCheck;

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
	 * The command's internal command handler.
	 * 
	 * @param guild      The guild the command was executed in.
	 * @param member     The member that executed the command.
	 * @param channel    The channel the command was executed in.
	 * @param rawMessage The raw command.
	 * @param args       Arguments passed in with the command.
	 */
	public void internalCommandHandler(Guild guild, Member member, MessageChannel channel, Message rawMessage, String[] args) {
		// If the member does not have permission to use the command.
		if (!permissionCheck.test(member)) {
			this.onPermissionDenied(guild, member, channel, rawMessage, args);
			return;
		}

		// If no arguments were specified, send it to the command handler.
		if (args.length == 0) {
			Utils.info(member.getEffectiveName() + "(" + member.getIdLong() + ") used " + rawMessage.getContentRaw());
			this.onCommand(guild, member, channel, rawMessage, args);
			return;
		}

		// Check if any subcommands were matched, if so send it to the subcommand
		// handler.
		for (Command sub : this.subCommands) {

			// Check if the subcommand name matches.
			if (sub.getName().equalsIgnoreCase(args[0])) {
				Utils.info(member.getEffectiveName() + "(" + member.getIdLong() + ") used " + rawMessage.getContentRaw());
				sub.internalCommandHandler(guild, member, channel, rawMessage, Arrays.copyOfRange(args, 1, args.length));
				return;
			}

			// Check if the subcommand aliases match.
			for (String alias : sub.aliases) {
				if (alias.equalsIgnoreCase(args[0])) {
					Utils.info(member.getEffectiveName() + "(" + member.getIdLong() + ") used " + rawMessage.getContentRaw());
					sub.internalCommandHandler(guild, member, channel, rawMessage, Arrays.copyOfRange(args, 1, args.length));
					return;
				}
			}

		}

		// If none of the subcommands matched the arguments.
		Utils.info(member.getEffectiveName() + "(" + member.getIdLong() + ") used " + rawMessage.getContentRaw());
		this.onCommand(guild, member, channel, rawMessage, args);
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
	public abstract boolean onCommand(Guild guild, Member member, MessageChannel channel, Message rawMessage, String[] args);

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
	public abstract boolean onPermissionDenied(Guild guild, Member member, MessageChannel channel, Message rawMessage, String[] args);

	/**
	 * Get the name of the command.
	 * 
	 * @return The name of the command.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the command's description.
	 * 
	 * @return The command's description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the command's aliases.
	 * 
	 * @return The command's aliases.
	 */
	public List<String> getAliases() {
		return this.aliases;
	}

	/**
	 * Get the command's permission check.
	 * 
	 * @return The command's permission check.
	 */
	public Predicate<Member> getPermissionCheck() {
		return this.permissionCheck;
	}

	/**
	 * Get the command's subcommands.
	 * 
	 * @return The command's subcommands.
	 */
	public List<Command> getSubCommands() {
		return this.subCommands;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.description, this.aliases, this.permissionCheck, this.subCommands);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Command))
			return false;

		Command that = (Command) obj;

		return this.name.equals(that.name) 
			&& this.description.equals(that.description)
			&& this.aliases.equals(that.aliases) 
			&& this.permissionCheck.equals(that.permissionCheck)
			&& this.subCommands.equals(that.subCommands);
	}

}
