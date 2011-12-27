package com.willmeyer.commander;

/**
 * An interface on which commands can be submitted to the system for execution.
 */
public abstract class CmdInputInterface {

	private String interfaceName = null;
	
	public abstract boolean isForHumans();

	public abstract boolean isForMachines();
	
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * Get a string that describes how the interface can be accessed...which port, whatever.
	 */
	public abstract String getInterfaceAccessInfo();

	public CmdInputInterface(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	public abstract void spawnInterface() throws Exception;

	public abstract void stopHandler();
	
}
