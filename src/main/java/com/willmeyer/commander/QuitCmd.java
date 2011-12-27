package com.willmeyer.commander;

import java.io.*;

/**
 * A command specifically made to quit the app.
 */
public final class QuitCmd extends CmdBase {

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public void validateParams (String[] params) {
		// Already validated that there are 0 params, by virtue of using the dflt 0,0 constructor
	}

	@Override
	public final boolean runForHuman (PrintStream out, String[] params) {
		return false;
	}

	@Override
	public void printReadableHelpDetail(PrintStream dest) {
		dest.println("Exit this interface.");
	}
}
