package org.sonatype.nexus.plugins.report.internal.rest;

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.plugins.report.internal.ReportService;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.rest.api.RepositoryNotFoundException;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;

public class ReportResourceTest extends TestSupport {

    @Mock
    private RepositoryManager repositoryManager;

    @Mock
    private RepositoryPermissionChecker repositoryPermissionChecker;

    @Mock
    private ReportService reportService;

    @Mock
    private Format format;

    @Mock
    private EventManager eventManager;

    @Mock
    private Repository repository;

    private ReportResource reportResource;

    @Before
    public void setup() {
        reportResource = new ReportResource(reportService, repositoryManager, repositoryPermissionChecker);
    }

    @Test
    public void downloadExcelReport_should_return_Parameter_repositoryName_is_required() {
        Response response = reportResource.downloadExcelReport(null);
        assert response.getStatus() == 400;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_NAME_REQUIRED;
    }

    @Test
    public void downloadExcelReport_should_return_Repository_not_found() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(null);
        Response response = reportResource.downloadExcelReport("test");
        assert response.getStatus() == 400;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_NOT_FOUND;
    }

    @Test
    public void downloadExcelReport_should_return_You_don_t_have_access_to_this_repository() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(repository);
        when(repositoryPermissionChecker.userCanBrowseRepository(repository)).thenReturn(false);
        Response response = reportResource.downloadExcelReport("test");
        assert response.getStatus() == 403;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_PERMISSION_DENIED;
    }

    @Test
    public void downloadExcelReport_should_return_INTERNAL_SERVER_ERROR() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(repository);
        when(repositoryPermissionChecker.userCanBrowseRepository(repository)).thenReturn(true);
        try {
            when(reportService.downloadReport(repository, "excel")).thenThrow(new IOException("error"));
        } catch (IOException | RepositoryNotFoundException e) {
            Assert.fail();
        }
        Response response = reportResource.downloadExcelReport("test");
        assert response.getStatus() == 500;
        assert response.getEntity().toString().equals("error");
    }

    @Test
    public void downloadExcelReport_should_return_response_with_empty_file() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(repository);
        when(repositoryPermissionChecker.userCanBrowseRepository(repository)).thenReturn(true);
        InputStream in = new ByteArrayInputStream("".getBytes());
        try {
            when(reportService.downloadReport(repository, "excel")).thenReturn(new Download(0, in));
        } catch (IOException | RepositoryNotFoundException e) {
            Assert.fail();
        }
        Response response = reportResource.downloadExcelReport("test");
        assert response.getEntity().equals(in);
        assert response.getHeaders().containsKey(com.google.common.net.HttpHeaders.CONTENT_DISPOSITION);
        assert response.getHeaders().containsKey(com.google.common.net.HttpHeaders.CONTENT_LENGTH);
        assert response.getHeaders().get(com.google.common.net.HttpHeaders.CONTENT_LENGTH).get(0).toString()
                .equals("0");
    }
    
    @Test
    public void downloadJsonReport_should_return_Parameter_repositoryName_is_required() {
        Response response = reportResource.downloadJsonReport(null);
        assert response.getStatus() == 400;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_NAME_REQUIRED;
    }

    @Test
    public void downloadJsonReport_should_return_Repository_not_found() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(null);
        Response response = reportResource.downloadJsonReport("test");
        assert response.getStatus() == 400;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_NOT_FOUND;
    }

    @Test
    public void downloadJsonReport_should_return_You_don_t_have_access_to_this_repository() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(repository);
        when(repositoryPermissionChecker.userCanBrowseRepository(repository)).thenReturn(false);
        Response response = reportResource.downloadJsonReport("test");
        assert response.getStatus() == 403;
        assert response.getEntity().toString() == ReportApiConstants.REPOSITORY_PERMISSION_DENIED;
    }

    @Test
    public void downloadJsonReport_should_return_INTERNAL_SERVER_ERROR() {
        String repositoryName = "test";
        when(repositoryManager.get(repositoryName)).thenReturn(repository);
        when(repositoryPermissionChecker.userCanBrowseRepository(repository)).thenReturn(true);
        try {
            when(reportService.getComponentsInfos(repository)).thenThrow(new RepositoryNotFoundException());
        } catch (RepositoryNotFoundException e) {
            Assert.fail();
        }
        Response response = reportResource.downloadJsonReport("test");
        assert response.getStatus() == 500;
    }

}
