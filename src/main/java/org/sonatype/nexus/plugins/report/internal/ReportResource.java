package org.sonatype.nexus.plugins.report.internal;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
@Path(ReportResource.RESOURCE_PATH)
public class ReportResource extends ComponentSupport implements Resource {

    public static final String RESOURCE_PATH = "internal/ui/report";

    private ReportService downloadReportService;
    private ObjectMapper objectMapper;

    @Inject
    public ReportResource(final DownloadService downloadService, final ReportService downloadReportService,
            final ObjectMapper objectMapper) {
        this.downloadReportService = downloadReportService;
        this.objectMapper = objectMapper;
    }

    @Timed
    @ExceptionMetered
    @Validate
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @RequiresPermissions("nexus:report:export")
    public Response downloadReport(@NotNull @Valid final ReportXO reportXO) {
        String fileName = reportXO.getFileName();
        log.info("Report name : {}", fileName);
        try {
            Download report = downloadReportService.downloadReport(reportXO);
            return Response.ok(report.getBytes())
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(CONTENT_LENGTH, report.getLength()).build();
        } catch (Exception e) {
            log.error("Unable to download the report {}", fileName, e);
            return Response.ok().type(MediaType.TEXT_HTML_TYPE).entity(e.getMessage()).build();
        }
    }
}