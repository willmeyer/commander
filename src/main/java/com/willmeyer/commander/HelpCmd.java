package com.willmeyer.commander;

import java.io.*;
import java.util.*;

/**
 * A command used to get help, either globally or on other commands, for human-interaction mode.  
 * It leverages the processor to get access to all commands.
*/
public final class HelpCmd extends OperationalCmdBase {

	@Override
	public String getReadableUsageString() {
		return "<command>";
	}

	@Override
	public CommandResponse executeForMachine(String[] params)
		throws UsageException, ExecutionException {
		throw new UsageException("We do not provide help for machine mode!");
	}

	@Override
	public void validateParams (String[] params) {

		// Already validated that there is 0 or 1 param, so true
	}

	public HelpCmd() {
		super (0, 1);
	}
	
	@Override
	public String getName () {
		return "help";
	}
	
	@Override
	public void executeForHuman (PrintStream out, String[] params) {
		CommandSet app = CommandSet.getInstance();	
		if (params.length == 0) {
			
			// This is the general help command, not command-specific
			Iterator<CmdBase> cmds = app.getAvailableCommands().iterator();
			out.println("All commands (use \"help <command>\" for command-specific help):");
			while (cmds.hasNext()) {
				CmdBase cmd = cmds.next();
				HelpCmd.printCommandSummaryLine(out, cmd.getName(), cmd.getReadableUsageString());
			}
		} else {
		
			// Specific command help
			CmdBase cmd = app.findCommandByName(params[0]);
			if (cmd == null) {
				out.println("Unknown command \"" + params[0] + "\"");
			} else {
				HelpCmd.printFullCommandHelp(out, cmd.getName(), cmd.getReadableUsageString());
				out.println();
				cmd.printReadableHelpDetail(out);
			}
		}
		return;
	}

	@Override
	public void printReadableHelpDetail(PrintStream dest) {
		dest.println("Lists help on available commands, or on a specific command in detail.");
	}
	
	private static void printFullCommandHelp (PrintStream out, String cmdName, String paramUsage) {
		out.println("Usage: " + cmdName + " " + paramUsage);
	}

	private static void printCommandSummaryLine (PrintStream out, String cmdName, String paramUsage) {
		out.println("  " + cmdName + " " + paramUsage);
	}

}
