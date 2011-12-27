package com.willmeyer.commander;

import java.util.Date;
import java.util.LinkedList;

public final class CommandLogger {

	protected static final int LOG_LEN = 20;
	protected LinkedList<LoggedCmd> m_cmdHistory = new LinkedList<LoggedCmd>();
	
	static class LoggedCmd {

		public String srcDesc;
		public String cmdStr;
		public Date when;
		
		public LoggedCmd (String sourceDesc, String commandString) {
			srcDesc = sourceDesc;
			cmdStr = commandString;
			when = new Date();
		}
	}
	
	public LinkedList<LoggedCmd> getRecentCommands() {
		return m_cmdHistory;
	}
	
	void logCommand(String srcDesc, String cmdName, String[] params) {
		if (m_cmdHistory.size() == LOG_LEN) {
			m_cmdHistory.removeLast();
		}
		String cmdStr = cmdName + " ";
		if (params != null) {
			for (String param : params) {
				cmdStr += (param + " ");
			}
		}
		LoggedCmd logged = new LoggedCmd(srcDesc, cmdStr);
		m_cmdHistory.addFirst(logged);
	}

}
