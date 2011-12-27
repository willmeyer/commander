package com.willmeyer.commander;

import java.util.*;

public final class CommandSet {
	
	private static CommandSet theInstance = null;
	
	private ArrayList<CmdBase> cmdList = null;
	
	public ArrayList<CmdBase> getAvailableCommands() {
		return cmdList;
	}
	
	public void clearCommands() {
		cmdList.clear();
	}
	
	public CmdBase findCommandByName(String cmdName) {
		CmdBase foundCmd = null;
		Iterator<CmdBase> cmds = cmdList.iterator();
		while ((foundCmd == null) && cmds.hasNext()) {
			CmdBase cmd = cmds.next();
			if (cmd.isCommand (cmdName)) {
				foundCmd = cmd;
			}
		}
		return foundCmd;
	}

	public CmdBase findCommandByClassName(String className) {
		CmdBase foundCmd = null;
		Iterator<CmdBase> cmds = cmdList.iterator();
		while ((foundCmd == null) && cmds.hasNext()) {
			CmdBase cmd = cmds.next();
			if (cmd.getClass().getName().equalsIgnoreCase (className)) {
				
				// This is the command
				foundCmd = cmd;
			}
		}
		return foundCmd;
	}
	
	public static CommandSet getInstance() {
		if (theInstance == null) {
			new CommandSet();
		}
		return theInstance;
	}

	public CommandSet() {
		assert theInstance == null;
		theInstance = this;
		cmdList = new ArrayList<CmdBase>();
		cmdList.add(new HelpCmd());
		cmdList.add(new QuitCmd ());
	}
	
	public void installCommand(OperationalCmdBase cmd) {
		cmdList.add(0, cmd);
	}
	
}
