# Java Discord API
Java Discord API is a fork of [JDA](https://github.com/DV8FromTheWorld/JDA), an existing discord API wrapper. Java Discord API provides additional features allowing developers to easily implement commands, events, plugins, files, and more!

## Features
### Creating a Basic Bot
To create a bot, you simply create a class that extends the Bot class, and invoke the super constructor. To start the bot, you use the start() method.
```java
public class Main {

	public static void main(String[] args) {
		MyBot myBot = new MyBot("token", Arrays.asList("commandPrefix1", "commandPrefix2", "commandPrefix3");
		myBot.start();
	}
  
}

class MyBot extends Bot {

	public MyBot(String token, List<String> comandPrefixes) {
		super(token, commandPrefixes);
	}
  
}
```

### Command API
To create a class that handles a command, you simply extend the Command class, invoke the super constructor with the command-parameters, and implement the onCommand method, and onPermissionDenied method.
```java
public class MyCommand extends Command {

	public MyCommand() {
		// The last argument is a predicate that determines if the member has access to the command or not.
		super("commandName", "commandDescription", Arrays.asList("aliases", "aliases2"), (member) -> member.getIdLong() == 1);
	}

	public boolean onCommand(Guild guild, Member member, MessageChannel channel, Message rawMessage, String[] args) {
		// Handle the command.
	}

	public boolean onPermissionDenied(Guild guild, Member member, MessageChannel channel, Message rawMessage, String[] args) {
		// Handle the command being denied.
	}
    
}
```
### Event API
To create a class that handles events, you simply implement the Listener interface, and create the methods that will handle the events. Each method that handles an event must have the @EventHandler annotation (with an optional event priority), and an [event](https://github.com/DV8FromTheWorld/JDA/wiki/8\)-List-of-Events) as the first parameter.
**Note: The name of the methods do not matter, the events will be dispatched by the parameter type.**
```java
public class MyEvent implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessage(MessageReceivedEvent event) {
		// Handle event
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMessageDelete(MessageDeleteEvent event) {
		// Handle event
	}
	
}
```
### Plugin API
**IMPLEMENTATION IN PROGRESS**

## Common Pitfalls

### Why is the Bot not Working?
You must ensure that you are running the bot with Java 11 or higher.

### How do I get a Discord Bot Token?
To get a discord bot token, you must create an [application](https://discord.com/developers/applications), go to the bot section on your application, and click add bot. You should then be able to copy your token.

## Contributions
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
