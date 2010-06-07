package com.zyeeda.jofm.handlers;

import java.io.File;

import com.zyeeda.framework.entities.Entity;
import com.zyeeda.framework.managers.EntityManager;
import com.zyeeda.framework.services.Server;
import com.zyeeda.jofm.commands.Command;
import com.zyeeda.jofm.commands.CustomCommand;

public class EntityOperationHandler extends AbstractHandler {
	
	public EntityOperationHandler(Server server) {
		super(server);
	}

	public File create(Command cmd) throws Exception {
		EntityManager entityMgr = this.getPersistenceService().getEntityManager();
		
		Entity entity = null; //BeanUtils.generateBean(cmd.getParameterMap());
		entityMgr.save(entity);
		
		CustomCommand customCmd = new CustomCommand();
		customCmd.setScope(cmd.getScope());
		customCmd.setOperation(this.getJofmService().getNextOperation(cmd.getScope(), cmd.getOperation()));
		customCmd.setId(cmd.getId());
		
		File file = (File) this.getCommandService().execCommand(customCmd);
		return file;
	}
}
