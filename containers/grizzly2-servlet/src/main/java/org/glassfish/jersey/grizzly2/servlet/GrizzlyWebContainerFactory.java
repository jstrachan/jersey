/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.grizzly2.servlet;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.servlet.Servlet;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;

/**
 * Factory for creating and starting Grizzly 2 {@link HttpServer} instances
 * for deploying a Servlet.
 * <p>
 * The default deployed server is an instance of {@link ServletContainer}.
 * <p>
 * If no initialization parameters are declared (or is null) then root
 * resource and provider classes will be found by searching the classes
 * referenced in the java classpath.
 *
 * @author Paul Sandoz
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public final class GrizzlyWebContainerFactory {

    private GrizzlyWebContainerFactory() {
    }

    /**
     * Create a {@link HttpServer} that registers the {@link ServletContainer}.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @return the http server, with the endpoint started
     * @throws java.io.IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(String u)
            throws IOException, IllegalArgumentException {
        if (u == null) {
            throw new IllegalArgumentException("The URI must not be null");
        }

        return create(URI.create(u));
    }

    /**
     * Create a {@link HttpServer} that registers the {@link ServletContainer}.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param initParams the servlet initialization parameters.
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(String u, Map<String, String> initParams)
            throws IOException, IllegalArgumentException {
        if (u == null) {
            throw new IllegalArgumentException("The URI must not be null");
        }

        return create(URI.create(u), initParams);
    }

    /**
     * Create a {@link HttpServer} that registers the {@link ServletContainer}.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(URI u)
            throws IOException, IllegalArgumentException {
        return create(u, ServletContainer.class);
    }

    /**
     * Create a {@link HttpServer} that registers the {@link ServletContainer}.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param initParams the servlet initialization parameters.
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(URI u,
            Map<String, String> initParams) throws IOException {
        return create(u, ServletContainer.class, initParams);
    }

    /**
     * Create a {@link HttpServer} that registers the declared
     * servlet class.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param c the servlet class
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(String u, Class<? extends Servlet> c) throws IOException {
        if (u == null) {
            throw new IllegalArgumentException("The URI must not be null");
        }

        return create(URI.create(u), c);
    }

    /**
     * Create a {@link HttpServer} that registers the declared
     * servlet class.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param c the servlet class
     * @param initParams the servlet initialization parameters.
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(String u, Class<? extends Servlet> c,
            Map<String, String> initParams) throws IOException {
        if (u == null) {
            throw new IllegalArgumentException("The URI must not be null");
        }

        return create(URI.create(u), c, initParams);
    }

    /**
     * Create a {@link HttpServer} that registers the declared
     * servlet class.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param c the servlet class
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(URI u, Class<? extends Servlet> c) throws IOException {
        return create(u, c, null);
    }

    /**
     * Create a {@link HttpServer} that registers the declared
     * servlet class.
     *
     * @param u the URI to create the http server. The URI scheme must be
     *        equal to "http". The URI user information and host
     *        are ignored If the URI port is not present then port 80 will be
     *        used. The URI path, query and fragment components are ignored.
     * @param c the servlet class
     * @param initParams the servlet initialization parameters.
     * @return the http server, with the endpoint started
     * @throws IOException if an error occurs creating the container.
     * @throws IllegalArgumentException if <code>u</code> is null
     */
    public static HttpServer create(URI u, Class<? extends Servlet> c,
            Map<String, String> initParams) throws IOException {
        if (u == null) {
            throw new IllegalArgumentException("The URI must not be null");
        }

        String path = u.getPath();
        if (path == null) {
            throw new IllegalArgumentException("The URI path, of the URI " + u + ", must be non-null");
        } else if (path.isEmpty()) {
            throw new IllegalArgumentException("The URI path, of the URI " + u + ", must be present");
        } else if (path.charAt(0) != '/') {
            throw new IllegalArgumentException("The URI path, of the URI " + u + ". must start with a '/'");
        }

        if (path.length() > 1) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }

        WebappContext context = new WebappContext("GrizzlyContext", path);
        ServletRegistration registration = context.addServlet(c.getName(), c);
        registration.addMapping("");
        if (initParams == null) {
//            registration.setInitParameter(ClassPathResourceConfig.PROPERTY_CLASSPATH,
//                    System.getProperty("java.class.path").replace(File.pathSeparatorChar, ';'));
        } else {
            registration.setInitParameters(initParams);
        }

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(u);
        context.deploy(server);
        return server;
    }
}
