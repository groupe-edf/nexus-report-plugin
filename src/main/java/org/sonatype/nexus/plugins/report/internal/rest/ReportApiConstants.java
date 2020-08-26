package org.sonatype.nexus.plugins.report.internal.rest;

import static org.sonatype.nexus.rest.APIConstants.V1_API_PREFIX;

public class ReportApiConstants {

    public static final String REPORT_API_NAME = "Report";

    public static final String REPORT_API_PERMISSION = "nexus:report:export";

    public static final String REPORT_API_RESOURCE_PATH = V1_API_PREFIX + "/report";

    public static final String DOWNLOAD_REPORT_API_OPERATION = "Download a report for the given repository name";

    public static final String REPOSITORY_NOT_FOUND = "Repository not found";

    public static final String REPOSITORY_NAME_REQUIRED = "Parameter 'repositoryName' is required";

    public static final String REPOSITORY_PERMISSION_DENIED = "You don't have access to this repository";

    public static final String REPORT_PERMISSION_DENIED = "You don't have access to this repository";

    public static final String REPOSITORY_NAME_DESCRIPTION = "Name of the repository";

    private ReportApiConstants() {
        // constructor for Sonar
    }
}
