package fr.edf.nexus.plugins.report.internal;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.validation.Validate;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
@Singleton
@Path(DownloadReport.RESOURCE_PATH)
public class DownloadReport extends ComponentSupport implements Resource {

    public static final String RESOURCE_PATH = "internal/report/download";

    private DownloadService downloadService;
    private DownloadReportService downloadReportService;
    private ObjectMapper objectMapper;

    @Inject
    public DownloadReport(final DownloadService downloadService, final DownloadReportService downloadReportService,
            final ObjectMapper objectMapper) {
        this.downloadService = downloadService;
        this.downloadReportService = downloadReportService;
        this.objectMapper = objectMapper;
    }

    @Timed
    @ExceptionMetered
    @Validate
    @GET
    @Path("{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @RequiresPermissions("nexus:component:read")
    public Response getReport(@Context final HttpServletRequest request) {
        String fileName = downloadReportService.getFileName();
        try {
            Download report = downloadReportService.downloadReport(request);
            return Response.ok(report.getBytes())
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(CONTENT_LENGTH, report.getLength()).build();
        } catch (Exception e) {
            log.error("Unable to download the report {}", fileName, e);
            return Response.ok().type(MediaType.TEXT_HTML_TYPE).entity(e.getMessage()).build();
        }
    }
}
