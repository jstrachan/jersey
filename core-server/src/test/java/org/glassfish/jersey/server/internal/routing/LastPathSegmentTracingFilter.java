/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.jersey.server.internal.routing;

import java.util.regex.MatchResult;

import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.process.internal.ResponseProcessor.RespondingContext;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

import com.google.common.base.Function;

import javax.inject.Inject;

@RequestScoped
class LastPathSegmentTracingFilter implements Router {

    private final RespondingContext<ContainerResponse> respondingContext;
    private final RoutingContext routingContext;

    @Inject
    public LastPathSegmentTracingFilter(
             RespondingContext<ContainerResponse> respondingContext,
             RoutingContext routingContext) {

        this.respondingContext = respondingContext;
        this.routingContext = routingContext;
    }

    @Override
    public Router.Continuation apply(ContainerRequest request) {
        final String wholePath = getWholeMatchedPath();
        final String lastMatch = getLastMatch();
        final String lastSegment = wholePath.isEmpty() ? wholePath : wholePath.substring(0, wholePath.length() - lastMatch.length());

        respondingContext.push(new Function<ContainerResponse, ContainerResponse>() {

            @Override
            public ContainerResponse apply(ContainerResponse response) {
                response.setEntity(response.getEntity() + "-" + lastSegment);
                return response;
            }
        });
        return Router.Continuation.of(request);
    }

    private String getWholeMatchedPath() {
        final MatchResult mr = routingContext.peekMatchResult();
        final String path = (mr == null) ? "" : mr.group();
        return (path.startsWith("/")) ? path.substring(1) : path;
    }

    private String getLastMatch() {
        final String match = routingContext.getFinalMatchingGroup();
        return (match == null) ? "" : match;
    }
}
