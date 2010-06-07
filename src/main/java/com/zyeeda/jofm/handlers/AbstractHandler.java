package com.zyeeda.jofm.handlers;

import com.zyeeda.framework.services.PersistenceService;
import com.zyeeda.framework.services.Server;
import com.zyeeda.jofm.services.CommandService;
import com.zyeeda.jofm.services.JofmService;

public class AbstractHandler {

	private Server server;
	
	public AbstractHandler(Server server) {
		this.server = server;
	}
	
	protected JofmService getJofmService() {
		return this.server.getService(JofmService.class);
	}
	
	protected PersistenceService getPersistenceService() {
		return this.server.getService(PersistenceService.class);
	}
	
	protected CommandService getCommandService() {
		return this.server.getService(CommandService.class);
	}
}
