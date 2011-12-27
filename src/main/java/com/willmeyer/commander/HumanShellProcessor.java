package com.willmeyer.commander;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import com.willmeyer.util.*;

/**
 * A CommandProcessor that helps execute commands from within a shell designed for use by humans.
 * Loosely, this means a shell that shows a prompt, accepts a line of input, executes something, 
 * and prints human-readable results back to the shell.
 */
public class HumanShellProcessor extends CommandProcessor {

	private String prompt= ">>";

	private final Logger logger = LoggerFactory.getLogger(HumanShellProcessor.class);
	
	private List<FallbackProcessor> fallbacks = new LinkedList<FallbackProcessor>();
	
	public static interface FallbackProcessor {
		
		public boolean fallback(String commandLine, PrintStream out);
		
	}
	
	public HumanShellProcessor(CommandSet commands, CommandLogger logger) {
		super(commands, logger);
	}
	
	public void addFallback(FallbackProcessor fallback) {
		this.fallbacks.add(fallback);
	}
	
	/**
	 * Prints a welcome message to the console.
	 */
	public void consoleWelcome(PrintStream out) {
		out.println("");
		out.println("Console activated. Enter \"help\" for usage. ");
		out.println("");
		out.flush();
	}
	
	/**
	 * Prints a goodbye message to the console.
	 */
	public void consoleGoodbye(PrintStream out) {
		out.println("ttyl");
		out.flush();
	}

	/**
	 * Inspects a BufferedReader for an input line. Inspects the line for the appropriate 
	 * command, executes it, and writes the output.
	 * 
	 * @return true if further command processing should continue, or false if the 
	 * just-executed command means the shell should complete.
	 */
	public boolean acceptAndDispatch(String srcDesc, BufferedReader reader, PrintStream out) throws IOException {
		out.print(this.prompt);
		out.flush();
		String line = reader.readLine();
		if (line != null)
			return processInputLine(srcDesc, out, line);
		else
			return true;
	}
	
	/**
	 * A helper -- asks the user to confirm a question, (y/n)-style.
	 * 
	 * @return boolean answer
	 */
	public boolean consoleConfirmPrompt(PrintStream out, InputStream in, String question) {
		out.print(question + " (y/n)> ");
		try {
			String line = new BufferedReader(new InputStreamReader (in)).readLine();
			if (line != null) {
				line = line.trim().toLowerCase();
				return (line.charAt(0) == 'y');
			}
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Inspects a single line of input for an appropriate command, executes it, and writes the 
	 * output.
	 * 
	 * @return true if further command processing should continue, or false if the 
	 * just-executed command means the shell should complete.
	 */
	public boolean processInputLine (String srcDesc, PrintStream out, String line) 
	{
		logger.debug("Processing line of input '{}'", line);
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
	 * @return boolean true if processing should continue, or false to quit
	 */
	protected boolean handleCommand(String srcDesc, String cmdName, String[] params, PrintStream out) {
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
					keepProcessing = cmd.runForHuman (out, params);
					//out.println("Done.");
				} catch (UsageException e) {
					
					// The command did not validate -- print the message then run the help 
					// command on it instead
					out.println("Invalid use of \"" + cmdName + "\": " + e.getMessage());
					out.println("Here's some help on the \"" + cmdName + "\" command:");
					out.println();	
					printCommandHelp(out, cmdName);
				} catch (ExecutionException e) {
				
					// Some problem while the command was consoleRunning...
					out.println("ERROR " + e.getStatusCode() + ": " + e.getExplanation());
					out.println();	
					printCommandHelp(out, cmdName);
				}
			} else {
				
				// We don't recognize this command, do we have any fallback processors?
				boolean handled = false;
				if (fallbacks.size() > 0) {
					String commandLine = cmdName;
					for (String param : params) {
						commandLine += " " + param;
					}
					for (FallbackProcessor fallback : fallbacks) {
						handled = fallback.fallback(commandLine, out);
						if (handled) break;
					}
				}
				if (!handled) {
					out.println("Sorry, I don't understand. Try asking me something else, or typing \"help\" for a list of formal commands.");
				}
			}	
		}
		return keepProcessing;
	}

	private void printCommandHelp(PrintStream out, String cmdName) {
		CmdBase cmd = commands.findCommandByClassName ("com.willmeyer.commander.HelpCmd");
		String[] dummy = {cmdName};
		try {
			cmd.runForHuman (out, dummy);
			out.println();
		} catch (Exception e) {
			// should not get here
			e.printStackTrace();
		}
	}

}
