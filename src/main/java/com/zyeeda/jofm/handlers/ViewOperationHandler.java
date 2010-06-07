package com.zyeeda.jofm.handlers;

import java.io.File;

import com.zyeeda.framework.services.Server;
import com.zyeeda.jofm.commands.Command;
import com.zyeeda.jofm.managers.FormManager;
import com.zyeeda.jofm.services.InvalidScopeException;
import com.zyeeda.jofm.services.JofmService;

public class ViewOperationHandler extends AbstractHandler {

	public ViewOperationHandler(Server server) {
		super(server);
	}
	
	public File getCreateView(Command cmd) throws InvalidScopeException {
		JofmService jofmSvc = this.getJofmService();
		FormManager formMgr = jofmSvc.getFormManager();
		Class<?> clazz = jofmSvc.getScopeClass(cmd.getScope());
		File file = formMgr.generateCreateForm(clazz);
		return file;
	}
	
}
