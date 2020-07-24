package org.sonatype.nexus.plugins.report.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;

@Named
@Singleton
public class DownloadReportService extends ComponentSupport {

    public Download downloadReport(final HttpServletRequest request) {
        return null;
    }
    
    public String getFileName() {
        return null;
    }
}
