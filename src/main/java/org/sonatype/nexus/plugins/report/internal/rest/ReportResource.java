package org.sonatype.nexus.plugins.report.internal.rest;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static org.sonatype.nexus.rest.APIConstants.V1_API_PREFIX;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.plugins.report.internal.ReportService;
import org.sonatype.nexus.plugins.report.internal.rest.doc.ReportResourceDoc;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.validation.Validate;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;

@Named
@Singleton
@Path(ReportResource.RESOURCE_PATH)
public class ReportResource extends ComponentSupport implements Resource, ReportResourceDoc {

    public static final String RESOURCE_PATH = V1_API_PREFIX + "/report";

    private ReportService reportService;
    private RepositoryManager repositoryManager;
    private RepositoryPermissionChecker repositoryPermissionChecker;

    @Inject
    public ReportResource(final ReportService downloadReportService, final RepositoryManager repositoryManager,
            final RepositoryPermissionChecker repositoryPermissionChecker) {
        this.reportService = downloadReportService;
    }

    @Timed
    @ExceptionMetered
    @Validate
    @GET
    @Path("{repositoryName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RequiresPermissions("nexus:report:export")
    public Response downloadExcelReport(@PathParam("repositoryName") final String repositoryName) {
        if (null == repositoryName) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_HTML_TYPE)
                    .entity("Parameter 'repositoryName' is required").build();
        }
        String fileName = repositoryName + ".xlsx";
        log.info("Report name : {}", fileName);
        try {
            Repository repository = repositoryManager.get(repositoryName);
            if (null == repository) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_HTML_TYPE)
                        .entity("Repository not found").build();
            }
            if (!repositoryPermissionChecker.userCanBrowseRepository(repository)) {
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_HTML_TYPE)
                        .entity("You don't have access to this repository").build();
            }
            Download report = reportService.downloadReport(repository, "excel");
            return Response.ok(report.getBytes())
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(CONTENT_LENGTH, report.getLength()).build();
        } catch (Exception e) {
            log.error("Unable to download the report {}", fileName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_HTML_TYPE)
                    .entity(e.getMessage()).build();
        }
    }
}
