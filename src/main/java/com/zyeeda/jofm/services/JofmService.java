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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyeeda.framework.helpers.LoggerHelper;
import com.zyeeda.framework.services.AbstractService;
import com.zyeeda.framework.services.Server;
import com.zyeeda.jofm.annotations.Operation;
import com.zyeeda.jofm.annotations.OperationGroup;
import com.zyeeda.jofm.annotations.OperationGroups;
import com.zyeeda.jofm.annotations.Scope;
import com.zyeeda.jofm.commands.Command;
import com.zyeeda.jofm.managers.FormManager;

public class JofmService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(JofmService.class);

    public static final String SCOPE_CLASSES = "scopeClasses.class";

    private static final String DEFAULT_VIEW_OPERATION_CLASS_NAME = "com.zyeeda.ofm.handlers.ViewOperationHandler";
    private static final String DEFAULT_SHOW_CREATE_VIEW_OPERATION_NAME = "show_create_view";
    private static final String DEFAULT_SHOW_CREATE_VIEW_METHOD_NAME = "getCreateView";
    private static final String DEFAULT_SHOW_UPDATE_VIEW_OPERATION_NAME = "show_update_view";
    private static final String DEFAULT_SHOW_UPDATE_VIEW_METHOD_NAME = "getUpdateView";
    private static final String DEFAULT_SHOW_DELETE_VIEW_OPERATION_NAME = "show_delete_view";
    private static final String DEFAULT_SHOW_DELETE_VIEW_METHOD_NAME = "getDeleteView";
    private static final String DEFAULT_SHOW_DETAIL_VIEW_OPERATION_NAME = "show_detail_view";
    private static final String DEFAULT_SHOW_DETAIL_VIEW_METHOD_NAME = "getDetailView";
    private static final String DEFAULT_SHOW_LIST_VIEW_OPERATION_NAME = "show_list_view";
    private static final String DEFAULT_SHOW_LIST_VIEW_METHOD_NAME = "getListView";

    private static final String DEFAULT_MODEL_OPERATION_CLASS_NAME = "com.zyeeda.ofm.handlers.ModelOperationHandler";
    private static final String DEFAULT_GET_CREATE_MODEL_OPERATION_NAME = "get_create_model";
    private static final String DEFAULT_GET_CREATE_MODEL_METHOD_NAME = "getCreateModel";
    private static final String DEFAULT_GET_UPDATE_MODEL_OPERATION_NAME = "get_update_model";
    private static final String DEFAULT_GET_UPDATE_MODEL_METHOD_NAME = "getUpdateModel";
    private static final String DEFAULT_GET_DELETE_MODEL_OPERATION_NAME = "get_delete_model";
    private static final String DEFAULT_GET_DELETE_MODEL_METHOD_NAME = "getDeleteModel";
    private static final String DEFAULT_GET_DETAIL_MODEL_OPERATION_NAME = "get_detail_model";
    private static final String DEFAULT_GET_DETAIL_MODEL_METHOD_NAME = "getDetailModel";
    private static final String DEFAULT_GET_LIST_MODEL_OPERATION_NAME = "get_list_model";
    private static final String DEFAULT_GET_LIST_MODEL_METHOD_NAME = "getListModel";

    private static final String DEFAULT_ENTITY_OPERATION_CLASS_NAME = "com.zyeeda.ofm.handlers.EntityOperationHandler";
    private static final String DEFAULT_CREATE_OPERATION_NAME = "create";
    private static final String DEFAULT_CREATE_METHOD_NAME = "create";
    private static final String DEFAULT_UPDATE_OPERATION_NAME = "update";
    private static final String DEFAULT_UPDATE_METHOD_NAME = "update";
    private static final String DEFAULT_DELETE_OPERATION_NAME = "delete";
    private static final String DEFAULT_DELETE_METHOD_NAME = "delete";

    private Map<String, Class<?>> scopeClasses = new HashMap<String, Class<?>>();
    private Map<String, Map<String, Method>> operationMethods = new HashMap<String, Map<String, Method>>();
    private Map<String, Map<String, String>> nextOperations = new HashMap<String, Map<String, String>>();

    private FormManager formMgr = new FormManager();

    public JofmService(Server server) {
        super(server);
    }

    @Override
        public void init(Configuration config) throws Exception {
            Object classes = config.getProperty(SCOPE_CLASSES);
            if (classes instanceof Collection<?>) {
                Collection<?> classNameList = (Collection<?>) classes;
                for (Object className : classNameList) {
                    this.extractMappingAnnotations((String) className);
                }
            } else if (classes instanceof String) {
                String className = (String) classes;
                this.extractMappingAnnotations(className);
            } else {
                throw new InvalidConfigurationException("在 " + SCOPE_CLASSES + " 附近出现错误");
            }
        }

    private void extractMappingAnnotations(String className) throws Exception {
        LoggerHelper.debug(logger, "class name = {}", className);
        Class<?> clazz = this.getClass().getClassLoader().loadClass(className);

        String scopeName = null;
        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
            scopeName = scopeAnnotation.value();
            this.putClass(scopeAnnotation.value(), clazz);
        } else {
            LoggerHelper.warn(logger, "annotation not present", clazz);
            scopeName = clazz.getSimpleName();
            this.putClass(clazz.getSimpleName(), clazz);
        }

        Map<String, Method> methods = new HashMap<String, Method>();
        this.operationMethods.put(scopeName, methods);

        Class<?> defaultViewOperationClass = this.getClass().getClassLoader().loadClass(DEFAULT_VIEW_OPERATION_CLASS_NAME);
        methods.put(DEFAULT_SHOW_CREATE_VIEW_OPERATION_NAME, defaultViewOperationClass.getMethod(DEFAULT_SHOW_CREATE_VIEW_METHOD_NAME, Command.class));
        methods.put(DEFAULT_SHOW_UPDATE_VIEW_OPERATION_NAME, defaultViewOperationClass.getMethod(DEFAULT_SHOW_UPDATE_VIEW_METHOD_NAME, Command.class));
        methods.put(DEFAULT_SHOW_DELETE_VIEW_OPERATION_NAME, defaultViewOperationClass.getMethod(DEFAULT_SHOW_DELETE_VIEW_METHOD_NAME, Command.class));
        methods.put(DEFAULT_SHOW_DETAIL_VIEW_OPERATION_NAME, defaultViewOperationClass.getMethod(DEFAULT_SHOW_DETAIL_VIEW_METHOD_NAME, Command.class));
        methods.put(DEFAULT_SHOW_LIST_VIEW_OPERATION_NAME, defaultViewOperationClass.getMethod(DEFAULT_SHOW_LIST_VIEW_METHOD_NAME, Command.class));

        Class<?> defaultModelOperationClass = this.getClass().getClassLoader().loadClass(DEFAULT_MODEL_OPERATION_CLASS_NAME);
        methods.put(DEFAULT_GET_CREATE_MODEL_OPERATION_NAME, defaultModelOperationClass.getMethod(DEFAULT_GET_CREATE_MODEL_METHOD_NAME, Command.class));
        methods.put(DEFAULT_GET_UPDATE_MODEL_OPERATION_NAME, defaultModelOperationClass.getMethod(DEFAULT_GET_UPDATE_MODEL_METHOD_NAME, Command.class));
        methods.put(DEFAULT_GET_DELETE_MODEL_OPERATION_NAME, defaultModelOperationClass.getMethod(DEFAULT_GET_DELETE_MODEL_METHOD_NAME, Command.class));
        methods.put(DEFAULT_GET_DETAIL_MODEL_OPERATION_NAME, defaultModelOperationClass.getMethod(DEFAULT_GET_DETAIL_MODEL_METHOD_NAME, Command.class));
        methods.put(DEFAULT_GET_LIST_MODEL_OPERATION_NAME, defaultModelOperationClass.getMethod(DEFAULT_GET_LIST_MODEL_METHOD_NAME, Command.class));

        Class<?> defaultEntityOperationClass = this.getClass().getClassLoader().loadClass(DEFAULT_ENTITY_OPERATION_CLASS_NAME);
        methods.put(DEFAULT_CREATE_OPERATION_NAME, defaultEntityOperationClass.getMethod(DEFAULT_CREATE_METHOD_NAME, clazz, Map.class));
        methods.put(DEFAULT_UPDATE_OPERATION_NAME, defaultEntityOperationClass.getMethod(DEFAULT_UPDATE_METHOD_NAME, clazz, Map.class));
        methods.put(DEFAULT_DELETE_OPERATION_NAME, defaultEntityOperationClass.getMethod(DEFAULT_DELETE_METHOD_NAME, clazz, Map.class));

        Map<String, String> nextOps = new HashMap<String, String>();
        this.nextOperations.put(scopeName, nextOps);

        if (clazz.isAnnotationPresent(OperationGroup.class)) {
            OperationGroup group = clazz.getAnnotation(OperationGroup.class);
            this.extractOperationAnnotations(clazz, group, methods, nextOps);
        }

        if (clazz.isAnnotationPresent(OperationGroups.class)) {
            OperationGroups groups = clazz.getAnnotation(OperationGroups.class);
            for (OperationGroup group : groups.value()) {
                this.extractOperationAnnotations(clazz, group, methods, nextOps);
            }
        }
    }

    private void extractOperationAnnotations(Class<?> clazz, OperationGroup group, Map<String, Method> methods, Map<String, String> nextOps) throws Exception {
        String defaultClassName = group.className();

        Operation[] ops = group.operations();
        for (Operation op : ops) {
            if (op.excluded()) {
                methods.remove(op.name());
                continue;
            }

            String cName = "".equals(op.className()) ? defaultClassName : op.className();
            String mName = "".equals(op.methodName()) ? op.name() : op.methodName();
            Class<?> c = this.getClass().getClassLoader().loadClass(cName);
            Method m = c.getMethod(mName, Command.class);
            methods.put(op.name(), m);

            if (!"".equals(op.nextOperation())) {
                nextOps.put(op.name(), op.nextOperation());
            }
        }
    }

    private void putClass(String key, Class<?> clazz) throws Exception {
        Class<?> tempClass = this.scopeClasses.get(key);
        if (tempClass != null) {
            throw new ScopeDuplicateException(key);
        }
        this.scopeClasses.put(key, clazz);
    }

    public Class<?> getScopeClass(String scope) throws InvalidScopeException {
        Class<?> clazz = this.scopeClasses.get(scope);
        if (clazz == null) {
            throw new InvalidScopeException(scope);
        }
        return clazz;
    }

    public Method getOperationMethod(String scope, String operation) throws InvalidScopeException, InvalidOperationException {
        Map<String, Method> methods = this.operationMethods.get(scope);
        if (methods == null) {
            throw new InvalidScopeException(scope);
        }
        Method method = methods.get(operation);
        if (method == null) {
            throw new InvalidOperationException(operation);
        }
        return method;
    }

    public String getNextOperation(String scope, String operation) throws InvalidScopeException {
        Map<String, String> nextOps = this.nextOperations.get(scope);
        if (nextOps == null) {
            throw new InvalidScopeException(scope);
        }
        return nextOps.get(operation);
    }

    public FormManager getFormManager() {
        return this.formMgr;
    }

}
