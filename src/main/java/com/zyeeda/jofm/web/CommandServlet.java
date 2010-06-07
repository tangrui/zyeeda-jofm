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
package com.zyeeda.jofm.web;

import com.zyeeda.framework.services.Server;
import com.zyeeda.framework.utils.JndiUtils;
import com.zyeeda.jofm.commands.Command;
import com.zyeeda.jofm.commands.IllegalRequestException;
import com.zyeeda.jofm.services.CommandService;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

/**
 * The servlet to make http request act as command.
 * 
 * @author      Rui Tang
 * @version     %I%, %G%
 * @since       1.0
 */
public class CommandServlet extends HttpServlet {

    private static final long serialVersionUID = -7040695263211415257L;

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Server server = (Server) JndiUtils.getObjectFromJndi(Server.JNDI_NAME);
            CommandService cmdSvc = server.getService(CommandService.class);
            Command cmd = cmdSvc.openCommand(request, response);
            File file = (File) cmdSvc.execCommand(cmd);
            
            this.sendFileToResponse(file, response);
        } catch (NamingException e) {
        } catch (IllegalRequestException e) {
        } catch (Exception e) {
		}
    }
    
    private void sendFileToResponse(File file, HttpServletResponse response) {
    	
    }
}
