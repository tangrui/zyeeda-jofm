/*
 * Copyright 2010 Zyeeda Co. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.zyeeda.jofm.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.zyeeda.jofm.commands.Command;
import com.zyeeda.jofm.commands.IllegalRequestException;
import com.zyeeda.jofm.commands.HttpServletCommand;

import com.zyeeda.framework.services.AbstractService;
import com.zyeeda.framework.services.Server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandService extends AbstractService {

    public CommandService(Server server) {
		super(server);
	}

    public Command openCommand(HttpServletRequest request, HttpServletResponse response) throws IllegalRequestException {
        return new HttpServletCommand(request, response);
    }
    
    public Object execCommand(Command cmd) throws Exception {
    	String scope = cmd.getScope();
    	String operation = cmd.getOperation();
    	
    	Method method = this.getJofmService().getOperationMethod(scope, operation);
    	
    	Constructor<?> ctor = method.getDeclaringClass().getConstructor(Server.class);
    	Object operationInstance = ctor.newInstance(this.getServer());
    	
    	Object result = method.invoke(operationInstance, cmd);
    	return result;
    }
    
    private JofmService getJofmService() {
    	return this.getServer().getService(JofmService.class);
    }
	
}
