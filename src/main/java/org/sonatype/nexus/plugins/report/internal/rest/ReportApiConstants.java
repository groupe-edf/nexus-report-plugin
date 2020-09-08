package org.sonatype.nexus.plugins.report.internal.rest;

import static org.sonatype.nexus.rest.APIConstants.V1_API_PREFIX;

/**
 * All constants used by {@code /report} API
 * 
 * @author Mathieu Delrocq
 *
 */
public final class ReportApiConstants {

    // ReportResource

    public static final String REPORT_API_PERMISSION = "nexus:report:export";

    public static final String REPORT_API_RESOURCE_PATH = V1_API_PREFIX + "/report";

    public static final String REPOSITORY_NOT_FOUND = "Repository not found";

    public static final String REPOSITORY_NAME_REQUIRED = "Parameter 'repositoryName' is required";

    public static final String REPOSITORY_PERMISSION_DENIED = "You don't have access to this repository";

    // ReportResourceDoc

    public static final String REPORT_API_NAME = "Report";

    public static final String REPOSITORY_NAME_DESCRIPTION = "Name of the repository";

    public static final String DOWNLOAD_REPORT_API_OPERATION = "Download a report for the given repository name";

    public static final String REPORT_PERMISSION_DENIED = "You don't have the required permission to download a report";

    private ReportApiConstants() {
        // to make Sonar happy
    }
}
