package com.willmeyer.commander;

/**
 * Something that can process and help execute commands.  A CommandProcessor may or may not have
 * full direct control over the input channel; it might just be used as a helper by something that
 * does.
 */
abstract class CommandProcessor {

	protected CommandSet commands;
	protected CommandLogger commandLogger;
	
	CommandProcessor(CommandSet commands, CommandLogger logger) {
		this.commands = commands;
		this.commandLogger = logger;
	}
	
}
