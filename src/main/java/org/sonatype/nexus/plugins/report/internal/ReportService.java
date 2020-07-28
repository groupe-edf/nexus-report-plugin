package org.sonatype.nexus.plugins.report.internal;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;

@Named
@Singleton
public class ReportService extends ComponentSupport {

    public Download downloadReport(final ReportXO reportXO) {
        return null;
    }

    public String getFileName() {
        return null;
    }
}
