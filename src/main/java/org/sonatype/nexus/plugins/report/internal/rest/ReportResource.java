package org.sonatype.nexus.plugins.report.internal.rest;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

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
import org.sonatype.nexus.common.wonderland.DownloadService;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.plugins.report.internal.ReportService;
import org.sonatype.nexus.plugins.report.internal.rest.doc.ReportResourceDoc;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.validation.Validate;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
@Singleton
@Path(ReportResource.RESOURCE_PATH)
public class ReportResource extends ComponentSupport implements Resource, ReportResourceDoc {

    public static final String RESOURCE_PATH = "internal/ui/report";

    private ReportService reportService;
    private ObjectMapper objectMapper;

    @Inject
    public ReportResource(final DownloadService downloadService, final ReportService downloadReportService,
            final ObjectMapper objectMapper) {
        this.reportService = downloadReportService;
        this.objectMapper = objectMapper;
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
        String fileName = repositoryName + ".xlsx";
        log.info("Report name : {}", fileName);
        try {
            Download report = reportService.downloadReport(repositoryName, "excel");
            //Download report = new Download(0, new ByteArrayInputStream(new String("").getBytes()));
            return Response.ok(report.getBytes())
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(CONTENT_LENGTH, report.getLength()).build();
        } catch (Exception e) {
            log.error("Unable to download the report {}", fileName, e);
            return Response.ok().type(MediaType.TEXT_HTML_TYPE).entity(e.getMessage()).build();
        }
    }
}
