package org.sonatype.nexus.plugins.report.internal;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.plugins.report.internal.builders.ComponentInfos;
import org.sonatype.nexus.plugins.report.internal.builders.ExcelReportBuilder;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.storage.Query;
import org.sonatype.nexus.repository.storage.StorageFacet;
import org.sonatype.nexus.repository.storage.StorageTx;

@Named
@Singleton
public class ReportService extends ComponentSupport {

    private static final String EXCEL_TYPE = "excel";

    private RepositoryManager repositoryManager;

    @Inject
    public ReportService(final RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public Download downloadReport(final String repositoryName, final String type) throws Exception {
        ByteArrayOutputStream out;

        switch (type) {
        case EXCEL_TYPE:
            ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
            excelBuilder.buildSheet(repositoryName, components);
            out = excelBuilder.buildExcelFile();
            break;
        default:
            throw new Exception("No file type defined");
        }

        int size = out.size();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new Download(size, in);
    }

    private List<ComponentInfos> getComponentsInfos(String repositoryName) {
        List<ComponentInfos> componentsInfos = new ArrayList<>();
        Repository repository = repositoryManager.get(repositoryName);
        StorageTx tx = repository.facet(StorageFacet.class).txSupplier().get();
        tx.begin();
        Iterable<Component> components = tx.findComponents(Query.builder().build(), Arrays.asList(repository));
        components.forEach(component -> {
            ComponentInfos componentInfos = new ComponentInfos();
            componentInfos.setSize(0L);
            componentInfos.setCreatedBy("system");

            Iterable<Asset> assets = tx.browseAssets(component);
            assets.forEach (asset -> {

                // component size = sum of all asset size
                componentInfos.setSize(componentInfos.getSize() + asset.size());

                // component lastDownloaded = download dateTime of most recently downloaded asset
                if (asset.lastDownloaded() != null) {
                    if (componentLastDownloaded == null || componentLastDownloaded.isBefore(asset.lastDownloaded())) {
                        componentLastDownloaded = asset.lastDownloaded();
                    }
                }

                // component lastUpdated = update dateTime of most recently updated asset
                if (asset.lastUpdated() != null) {
                    if (componentLastUpdated == null || componentLastUpdated.isBefore(asset.lastUpdated())) {
                        componentLastUpdated = asset.lastUpdated();
                    }
                }

                // component createdBy = createdBy field of the asset if it's not "system"
                if (asset.createdBy() != null && !asset.createdBy().isEmpty() && !asset.createdBy().equals("system")) {
                    componentCreatedBy = asset.createdBy();
                }
            });


            String repositoryId = repo.name
            String componentId = component.getEntityMetadata().id.value
            String encoded = new String(Base64.getUrlEncoder().withoutPadding().encode("$repositoryId:$componentId".bytes))
            //String componentIdEnc =  new String(Base64.getUrlEncoder().withoutPadding().encode("$componentId".bytes))
            //String decoded = new String(Base64.getUrlDecoder().decode(encoded))


        });
        tx.close();
        return componentsInfos;
    }
}
