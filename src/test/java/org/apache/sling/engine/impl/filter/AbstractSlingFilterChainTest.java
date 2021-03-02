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
package org.apache.sling.engine.impl.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.engine.impl.SlingHttpServletRequestImpl;
import org.apache.sling.engine.impl.SlingRequestProcessorImpl;
import org.apache.sling.engine.impl.request.RequestData;

import org.apache.sling.testing.mock.sling.junit.SlingContext;

import org.apache.tools.ant.taskdefs.condition.Http;
import org.jmock.Expectations;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractSlingFilterChainTest extends AbstractFilterTest {
    
    @Test
    public void testDoubleCall() throws Exception {
        Filter badFilter = new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() { }
        };

        FilterHandle handle = new FilterHandle(badFilter, null, 1, 1, null, null);

        AbstractSlingFilterChain chain = new AbstractSlingFilterChain(new FilterHandle[]{ handle }) {
            @Override
            protected void render(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
            }
        };
        HttpServletRequest httpReq = whateverRequest();
        final RequestData requestData = new RequestData(new SlingRequestProcessorImpl(), httpReq,
                context.mock(HttpServletResponse.class));
        final SlingHttpServletRequestImpl req = new SlingHttpServletRequestImpl(requestData, httpReq);
        boolean illegalStateCaught = false;
        try {
            chain.doFilter(req, context.mock(SlingHttpServletResponse.class));
        } catch (IllegalStateException e) {
            illegalStateCaught = true;
        }
        assertTrue("an illegal state exception should have been caught", illegalStateCaught);
    }

}
