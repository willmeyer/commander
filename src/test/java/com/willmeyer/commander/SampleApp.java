package com.willmeyer.commander;

import java.io.*;

import com.willmeyer.commander.input.*;

public class SampleApp implements LocalHumanConsoleInterface.QuitHandler {

	public void quit() {
		
		// The user wanted to quit, ok...
		System.out.println("Shutting down...");
	}

	protected CommandSet commands;
	protected CmdInterfaceManager inputs;
	
	public static class HelloWorldCommand extends OperationalCmdBase {

		@Override
		public void executeForHuman(PrintStream out, String[] params)
			throws UsageException, ExecutionException {
			out.print("Hellow world!");
		}

		@Override
		public CommandResponse executeForMachine(String[] params)
			throws UsageException, ExecutionException {
			return new CommandResponse();
		}

		@Override
		public String getName() {
			return "helloworld";
		}

		@Override
		public String getReadableUsageString() {
			return "[repeat]\n\n repeat: The number of times to repeat"; 
		}

		@Override
		public void printReadableHelpDetail(PrintStream dest) {
			dest.println("Prints hello world a few times.");
		}

		@Override
		public void validateParams(String[] params) throws UsageException {
			if (params.length != 1) throw new UsageException("Missing repetition count!");
			
		}
	}
	
	public SampleApp() {
		
		// Set up our commands
		System.out.println("Setting up commands...");
		commands = new CommandSet();
		commands.installCommand(new HelloWorldCommand());

		// Set up logger
		CommandLogger logger = new CommandLogger();
		
		// Set up the processors our different interfaces can use
		HumanShellProcessor humanShell = new HumanShellProcessor(commands, logger);
		//MachineCommandProcessor machine = new MachineCommandProcessor(commands, logger);
		
		// Set up all interfaces
		System.out.println("Setting up interfaces...");
		inputs = new CmdInterfaceManager();
		CmdInputInterface localConsole = new LocalHumanConsoleInterface(new BufferedReader(new InputStreamReader(System.in)), System.out, humanShell, this);
		inputs.installInterface(localConsole);
		CmdInputInterface networkedConsole = new NetworkedHumanConsoleInterface(12345, humanShell);
		inputs.installInterface(networkedConsole);
		
		// Start everything...
		System.out.println("Starting interfaces...");
		inputs.startInterfaces();
	}
	
	public static void main(String[] params) {
		new SampleApp();
	}
}
