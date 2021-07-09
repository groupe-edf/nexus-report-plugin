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
package fr.edf.nexus.plugins.report.internal.rest.doc;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.sonatype.nexus.repository.rest.internal.resources.ComponentsResource;

import fr.edf.nexus.plugins.report.internal.rest.ReportApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Swagger documentation for {@link ComponentsResource}
 *
 * @author Mathieu Delrocq
 * @since 3.4.0
 */
@Api(value = ReportApiConstants.REPORT_API_NAME)
public interface ReportResourceDoc {
    @ApiOperation(value = ReportApiConstants.DOWNLOAD_REPORT_API_OPERATION)
    @ApiResponses(value = { @ApiResponse(code = 403, message = ReportApiConstants.REPORT_PERMISSION_DENIED),
            @ApiResponse(code = 400, message = ReportApiConstants.REPOSITORY_NAME_REQUIRED) })
    Response downloadExcelReport(@ApiParam(ReportApiConstants.REPOSITORY_NAME_DESCRIPTION) final String repositoryName)
            throws IOException;
    @ApiOperation(value = ReportApiConstants.DOWNLOAD_REPORT_API_OPERATION)
    @ApiResponses(value = { @ApiResponse(code = 403, message = ReportApiConstants.REPORT_PERMISSION_DENIED),
            @ApiResponse(code = 400, message = ReportApiConstants.REPOSITORY_NAME_REQUIRED) })
    Response downloadJsonReport(@ApiParam(ReportApiConstants.REPOSITORY_NAME_DESCRIPTION) final String repositoryName)
            throws IOException;
}
