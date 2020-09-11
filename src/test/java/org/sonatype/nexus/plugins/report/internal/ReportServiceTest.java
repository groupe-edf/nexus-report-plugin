package org.sonatype.nexus.plugins.report.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.common.entity.EntityMetadata;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.rest.api.RepositoryNotFoundException;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.storage.StorageFacet;
import org.sonatype.nexus.repository.storage.StorageTx;

import com.google.common.base.Supplier;

public class ReportServiceTest extends TestSupport {

    @Mock
    private Repository repository;

    @Mock
    private StorageTx tx;

    @Mock
    private Component component;

    @Mock
    private Asset asset;

    @Mock
    private StorageFacet facet;

    @Mock
    private EntityMetadata entityMetadata;

    @Mock
    private EntityId entityId;

    @Mock
    private Supplier<StorageTx> txSupplier;

    private ReportService reportService = new ReportService();

    @Before
    public void setUp() {
//        Supplier<StorageTx> txSupplier = Suppliers.ofInstance(tx);
        when(repository.facet(StorageFacet.class)).thenReturn(facet);
        when(facet.txSupplier()).thenReturn(txSupplier);
        when(repository.getName()).thenReturn("test");
        when(component.getEntityMetadata()).thenReturn(entityMetadata);
        when(entityMetadata.getId()).thenReturn(entityId);
        when(entityId.getValue()).thenReturn("entityId");
        when(txSupplier.get()).thenReturn(tx);
        doNothing().when(tx).begin();
        doNothing().when(tx).close();

    }

    @Test
    public void should_not_throw_exception_with_minimum_values() {
        Iterable<Component> components = Arrays.asList(component);
        Iterable<Asset> assets = Arrays.asList(asset);
        when(tx.findComponents(any(), any())).thenReturn(components);
        when(tx.browseAssets(component)).thenReturn(assets);
        when(asset.size()).thenReturn(1L);
        try {
            reportService.downloadReport(repository, "excel");
        } catch (IOException | RepositoryNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void should_throw_repository_not_found_exception() {
        Iterable<Component> components = Arrays.asList(component);
        Iterable<Asset> assets = Arrays.asList(asset);
        when(tx.findComponents(any(), any())).thenReturn(components);
        when(tx.browseAssets(component)).thenReturn(assets);
        when(asset.size()).thenReturn(1L);
        try {
            reportService.downloadReport(null, "excel");
        } catch (RepositoryNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(true);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void should_throw_IOException_no_type_defined() {
        Iterable<Component> components = Arrays.asList(component);
        Iterable<Asset> assets = Arrays.asList(asset);
        when(tx.findComponents(any(), any())).thenReturn(components);
        when(tx.browseAssets(component)).thenReturn(assets);
        when(asset.size()).thenReturn(1L);
        try {
            reportService.downloadReport(repository, "");
        } catch (RepositoryNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            assert e.getMessage().equals("No file type defined");
        }
    }
}
