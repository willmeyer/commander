package com.willmeyer.commander.input;

import java.io.*;

import org.slf4j.*;

import com.willmeyer.commander.CmdInputInterface;
import com.willmeyer.commander.HumanShellProcessor;

public class LocalHumanConsoleInterface extends CmdInputInterface {

	protected BufferedReader stdin;
	protected PrintStream stdout;
	protected HumanShellProcessor humanProcessor;
	protected boolean consoleRunning = false;
	protected QuitHandler quitHandler = null;
	protected final Logger logger = LoggerFactory.getLogger(LocalHumanConsoleInterface.class);
	
	public LocalHumanConsoleInterface(BufferedReader stdin, PrintStream stdout, HumanShellProcessor humanProcessor, QuitHandler quitHandler) {
		super("local console");
		this.stdin = stdin;
		this.stdout = stdout;
		this.humanProcessor = humanProcessor;
		this.quitHandler = quitHandler;
	}
	
	@Override
	public String getInterfaceAccessInfo() {
		return "local shell console";
	}

	@Override
	public boolean isForHumans() {
		return true;
	}

	@Override
	public boolean isForMachines() {
		return false;  // TODO could switch modes and stuff automatically...
	}

	@Override
	public void stopHandler() {
		consoleRunning = false;
	}
	
	@Override
	public void spawnInterface() {
		humanProcessor.consoleWelcome(stdout);
		consoleRunning = true;
		new HandlerThread().start();
	}

	public static interface QuitHandler {
		
		public void quit();
		
	}
	
	public class HandlerThread extends Thread {
		
		@Override
		public void run() {
			try {
				boolean keepProcessing = true;
				while (consoleRunning && keepProcessing) {
					keepProcessing = humanProcessor.acceptAndDispatch("sdfsdf", stdin, stdout);
				}
				humanProcessor.consoleGoodbye(stdout);
				if (consoleRunning) {

					// We're quitting because a quit command was received, not because we were shut down
					quitHandler.quit();
					consoleRunning = false;
				}
			} catch (IOException e) {
				logger.error("IO Exception during handling, closing console...", e);
			}
		}
	}
}
