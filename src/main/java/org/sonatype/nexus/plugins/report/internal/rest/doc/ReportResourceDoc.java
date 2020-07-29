/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.report.internal.rest.doc;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.sonatype.nexus.repository.rest.internal.resources.ComponentsResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Swagger documentation for {@link ComponentsResource}
 *
 * @since 3.4.0
 */
@Api(value = "Report")
public interface ReportResourceDoc {
    @ApiOperation(value = "Download the report")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Insufficient permissions to download a report"),
            @ApiResponse(code = 422, message = "Parameter 'repository' is required") })
    Response downloadReport(@ApiParam("Name of the repository") final String repositoryName) throws IOException;
}
