package com.willmeyer.commander;

import java.util.*;

import org.slf4j.*;

public final class CmdInterfaceManager {

	protected List<CmdInputInterface> interfaces;
	protected final Logger logger = LoggerFactory.getLogger(CmdInterfaceManager.class);
	
	public void installInterface(CmdInputInterface inputInterface) {
		logger.info("Installing interface '{}'...", inputInterface.getInterfaceName());
		this.interfaces.add(inputInterface);
	}
	
	public CmdInterfaceManager() {
		this.interfaces = new LinkedList<CmdInputInterface>();
	}
	
	public void stopInterfaces() {
		for (CmdInputInterface inputInterface : interfaces) {
			logger.info("Stopping interface {}...", inputInterface.getInterfaceName());
			inputInterface.stopHandler();
		}
	}

	public void startInterfaces() {
		for (CmdInputInterface inputInterface : interfaces) {
			logger.info("Installing interface {}...", inputInterface.getInterfaceName());
			try {
				inputInterface.spawnInterface();
				logger.info("Interface '{}' active at: '{}'", inputInterface.getInterfaceName(), inputInterface.getInterfaceAccessInfo());
			} catch (Exception e) {
				logger.info("Interface '{}' could not start", inputInterface.getInterfaceName(), e);
			}
		}
	}
}
