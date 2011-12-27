package com.willmeyer.commander;

import java.io.*;

/**
 * The base class for all commands that represent an actual operation of some kind.
 */
public abstract class OperationalCmdBase extends CmdBase {

	@Override
	public final boolean runForHuman (PrintStream out, String[] params) 
		throws UsageException, ExecutionException {
		this.executeForHuman (out, params);
		return true;
	}
	
	@Override
	public final CommandResponse runForMachine (String[] params)
		throws UsageException, ExecutionException {
		return this.executeForMachine (params);
	}

	public abstract void executeForHuman (PrintStream out, String[] params)
	    throws UsageException, ExecutionException;
	
	public abstract CommandResponse executeForMachine (String[] params) 
		throws UsageException, ExecutionException;

	public OperationalCmdBase (int minParams, int maxParams) {
		super (minParams, maxParams);
	}

	public OperationalCmdBase () {
		super (0, 0);
	}
}
