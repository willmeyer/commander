package com.willmeyer.commander.input;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.*;

import com.willmeyer.commander.*;

public class NetworkedHumanConsoleInterface extends CmdInputInterface {

	protected int port;
	protected boolean runServer;
	protected HumanShellProcessor humanProcessor;
	
	protected final Logger logger = LoggerFactory.getLogger(NetworkedHumanConsoleInterface.class);
	
	public NetworkedHumanConsoleInterface(int port, HumanShellProcessor humanProcessor) {
		super("network console");
		this.port = port;
		this.humanProcessor = humanProcessor;
	}
	
	@Override
	public String getInterfaceAccessInfo() {
		return "network console on port " + this.port;
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
		this.server.shutdown();
	}
	
	protected ServerThread server;
	
	@Override
	public void spawnInterface() throws IOException {
		this.runServer = true;
		this.server = new ServerThread(this.port);
		this.server.setDaemon(true); // will not hold the process running if this is all that's running
		this.server.start();
	}

	private class ServerThread extends Thread {
		
		private ServerSocket serverSock = null;
		private boolean consoleRunning = true;
		
		public ServerThread(int port) throws IOException {
			assert port > 0;
			logger.info("Starting telnet server on port {}...", port);
			serverSock = new ServerSocket();
			SocketAddress addr = new InetSocketAddress(port);
			serverSock.bind(addr);
			logger.info("Network human console server started.");
		}
		
		public void run() {
			try {
				while (consoleRunning) {
					Socket sock = serverSock.accept();
					new ConnectionHandler(sock);
				}
			} catch (Exception e) {
				if (!consoleRunning) {
					
					// This is an error on shutdown, ok
					logger.debug("Error on shutdown, no biggie: {}", e.getMessage());
				} else {
					logger.error("ERROR accepting socket.", e);
				}
			}
		}
		
		public void shutdown() {
			consoleRunning = false;
			try {
				serverSock.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private final class ConnectionHandler extends Thread {
		
		private Socket myClientSock = null;
		
		public ConnectionHandler(Socket sock) {
			myClientSock = sock;
			this.start();
		}
		
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(myClientSock.getInputStream()));
				PrintStream out = new PrintStream(myClientSock.getOutputStream());
				boolean accept = true;
				out.print("Welcome!  Type 'help' if you need it.");
				while (accept) {
					accept = humanProcessor.acceptAndDispatch("sdfsdf", reader, out);
				}
				humanProcessor.consoleGoodbye(out);
				myClientSock.close();
			} catch (IOException e) {
				logger.error("IO Exception during handling, closing console...", e);
			}
		}
	}
}
