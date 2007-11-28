/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.core.impl.scripting;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.scripting.SlingScript;

public class DefaultSlingScriptServlet implements Servlet {

    private final SlingScript script;

    public DefaultSlingScriptServlet(SlingScript script) {
        this.script = script;
    }

    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        DefaultSlingScriptResolver.evaluateScript(script,
            (SlingHttpServletRequest) req, (SlingHttpServletResponse) res);
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public String getServletInfo() {
        return "Servlet for script " + script.getScriptResource().getURI();
    }

    public void init(ServletConfig config) {
    }

    public void destroy() {
    }

}