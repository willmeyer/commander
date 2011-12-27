package com.willmeyer.commander;

import java.io.*;

/**
 * The base of all commands in the framework -- every command is one of these at its most basic
 * level.
 * 
 * A Command is a logical thing that can be executed -- sort of like a Runnable, but with a bit 
 * more structure:
 *  - it consists of a name and a set of string parameters
 *  - it provides human-readable help info
 *  - it is designed to run in an interactive shell with a human at the other end, as well as in
 *    a machine-call model, with structured return data
 * 
 * Users of the library should extend OperationalCmdBase to implement their own commands.
 */
public abstract class CmdBase {

	private int minParams = 0;
	private int maxParams = 0;
	
	public CmdBase () {
		this (0, 0);
	}
	
	public final void validateParamCount (String[] params) throws UsageException {
		if (!((params.length >= minParams) 
			 && ((params.length <= maxParams) || (maxParams == -1))))
			throw new UsageException ("Incorrect number of parameters for command...");
	}
	
	public abstract void validateParams (String[] params) throws UsageException;

	public CmdBase (int minParams, int maxParams) {
		this.minParams = minParams;
		this.maxParams = maxParams;
	}
	
	/**
	 * Provides a string that describes the "usage" of the command when consoleRunning inside an 
	 * interactive human interface
	 */
	public String getReadableUsageString () {
		return "";
	}

	/**
	 * Writes a chunk of text to a human, explaining how the command works when used in a human
	 * interface.  The message should be standalone, with no spacing above or below, and
	 * assuming that there is a usage string already printed above it.
	 * 
	 * @param dest the stream to write the help to
	 */
	public void printReadableHelpDetail (PrintStream dest) {
		dest.println("No specific help defined.");
	}

	public abstract String getName ();
	
	/**
	 * Tests to see if the passed string is in fact the command.
	 */
	public final boolean isCommand (String cmd) {
		return cmd.equalsIgnoreCase(this.getName ());
	}
	
	/**
	 * Runs the command in a human-invoked context.  This means it should expect to print its 
	 * output to the provided stream, and for that output to be understandable/readable.
	 * 
	 * @param out
	 * @param params
	 * @return boolean true if processing of other commands should continue after this command's
	 *         execution
	 * @throws UsageException if there is a problem in the usage of the command.  If the usage is
	 *         correct but the execution has a problem, throw that.  In both cases, the framework 
	 *         gives the resulting message to the user
	 */
	public boolean runForHuman (PrintStream out, String[] params) 
		throws UsageException, ExecutionException {
		throw new UsageException("Sorry, this command is not implemented for humans.");
	}

	/**
	 * Runs the command in a machine-invoked context.  This means the response will be well-formed
	 * structured data, and nothing will be printed to the user along the way.
	 * 
	 * @param out
	 * @param params
	 * @return boolean true if processing of other commands should continue after this command's
	 *         execution
	 * @throws UsageException if there is a problem in the usage of the command.  If the usage is
	 *         correct but the execution has a problem, throw that.  In both cases, the framework 
	 *         gives the resulting message to the user
	 */
	public CommandResponse runForMachine (String[] params) 
		throws UsageException, ExecutionException {
		throw new UsageException("This command is not implemented for machine mode.");
	}
	
}
