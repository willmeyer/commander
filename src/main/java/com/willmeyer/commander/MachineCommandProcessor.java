package com.willmeyer.commander;

import java.io.BufferedReader;
import java.io.PrintStream;

import com.willmeyer.util.NullOutputStream;

/**
 * A CommandProcessor that helps execute commands in some kind of machine-only interface.
 * 
 * This processor can be used in a few ways:
 *    - to interpret shell-style command lines (use acceptAndDispatch or processInputLine)
 *    - to run commands just given a name and params (use handleCommand)
 */
public class MachineCommandProcessor extends CommandProcessor {

	public MachineCommandProcessor(CommandSet commands, CommandLogger logger) {
		super(commands, logger);
	}
	
	/**
	 * Inspects a BufferedReader for an input line. Inspects the line for the appropriate 
	 * command, executes it, and writes the output.
	 * 
	 * @return true if further command processing should continue, or false if the 
	 * just-executed command means the processing should complete.
	 */
	public boolean acceptAndDispatch(String srcDesc, BufferedReader reader, PrintStream out) throws Exception {
		out.flush();
		String line = reader.readLine();
		if (line != null)
			return processInputLine(srcDesc, out, line);
		else
			return true;
	}
	
	/**
	 * Processes a line of input representing a command, executes it, and writes the output.
	 * 
	 * @return true if further command processing should continue, or false if the 
	 * just-executed command means the processing should complete.
	 */
	public boolean processInputLine (String srcDesc, PrintStream out, String line) 
	{
		line = line.trim ();
		int idx = line.indexOf (" ");
		String cmdName = null;
		String params[] = null;
		if (idx < 0) {
			cmdName = line.toLowerCase();
			params = new String[0];
		} else {
			cmdName = line.substring(0, idx);
			line = line.substring (idx + 1);
			if ((params = line.split(" ")) == null)
				params = new String[0]; 	
		}
		return handleCommand(srcDesc, cmdName, params, out);
	}

	/**
	 * Takes a registered command, executes it, and writes the output.
	 * 
	 * @return true if further command processing should continue, or false if the 
	 * just-executed command means the processing should complete.
	 */
	public boolean handleCommand(String srcDesc, String cmdName, String[] params, PrintStream out) {
		assert cmdName != null;
		assert params != null;
		if (out == null) {
			out = new PrintStream(new NullOutputStream());
		}
		boolean keepProcessing = true;
		
		// Find the command, and execute it with the parameters
		CmdBase cmd = null;
		if (cmdName != null) {
			if ((cmd = commands.findCommandByName(cmdName)) != null) {
				
				// This is the command, validate it and execute it with the appropriate params
				try {
					cmd.validateParamCount(params);
					cmd.validateParams (params);
					commandLogger.logCommand(srcDesc, cmdName, params);
					cmd.runForMachine (params);
					//out.println("Done.");
				} catch (UsageException e) {
					
					// The command did not validate -- print the message then run the help 
					// command on it instead
					out.println("Invalid use of \"" + cmdName + "\": " + e.getMessage());
					out.println("Here's some help on the \"" + cmdName + "\" command:");
					out.println();	
					//doCmdHelp(out, cmdName);
				} catch (ExecutionException e) {
				
					// Some problem while the command was consoleRunning...
					out.println("ERROR " + e.getStatusCode() + ": " + e.getExplanation());
					out.println();	
					//doCmdHelp(out, cmdName);
				}
			} else {
				out.println("Sorry, I don't recognize \"" + cmdName + "\" as a command. Type \"help\" if you need it.");	
				//doGlobalHelp(out);
			}	
		}
		return keepProcessing;
	}

}
