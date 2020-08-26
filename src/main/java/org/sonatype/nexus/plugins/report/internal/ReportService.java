package org.sonatype.nexus.plugins.report.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.joda.time.DateTime;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.DownloadService.Download;
import org.sonatype.nexus.plugins.report.internal.builders.ComponentInfos;
import org.sonatype.nexus.plugins.report.internal.builders.ExcelReportBuilder;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.rest.api.RepositoryNotFoundException;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.storage.Query;
import org.sonatype.nexus.repository.storage.StorageFacet;
import org.sonatype.nexus.repository.storage.StorageTx;

@Named
@Singleton
public class ReportService extends ComponentSupport {

    private static final String EXCEL_TYPE = "excel";

    public Download downloadReport(final Repository repository, final String type)
            throws IOException, RepositoryNotFoundException {
        ByteArrayOutputStream out;

        List<ComponentInfos> componentInfos = getComponentsInfos(repository);

        switch (type) {
        case EXCEL_TYPE:
            ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
            excelBuilder.buildSheet(repository.getName(), componentInfos);
            out = excelBuilder.buildExcelFile();
            break;
        default:
            throw new IOException("No file type defined");
        }

        int size = out.size();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new Download(size, in);
    }

    private List<ComponentInfos> getComponentsInfos(Repository repository) throws RepositoryNotFoundException {
        List<ComponentInfos> componentsInfos = new ArrayList<>();
        if (null == repository) {
            throw new RepositoryNotFoundException();
        }
        StorageTx tx = repository.facet(StorageFacet.class).txSupplier().get();
        tx.begin();
        Iterable<Component> components = tx.findComponents(Query.builder().build(), Arrays.asList(repository));
        for (Component component : components) {
            componentsInfos.add(getComponentInfos(repository, tx, component));
        }
        tx.close();
        return componentsInfos;
    }

    private ComponentInfos getComponentInfos(Repository repository, StorageTx tx, Component component) {
        ComponentInfos componentInfos = new ComponentInfos();

        Long componentSize = 0L;
        String componentCreatedBy = null;
        DateTime componentLastDownloaded = null;
        DateTime componentLastUpdated = null;

        Iterable<Asset> assets = tx.browseAssets(component);
        for (Asset asset : assets) {

            // component size = sum of all asset size
            componentSize += asset.size();

            // component lastDownloaded = download dateTime of most recently downloaded
            // asset
            if (asset.lastDownloaded() != null
                    && (componentLastDownloaded == null || componentLastDownloaded.isBefore(asset.lastDownloaded()))) {
                componentLastDownloaded = asset.lastDownloaded();
            }

            // component lastUpdated = update dateTime of most recently updated asset
            if (asset.lastUpdated() != null
                    && (componentLastUpdated == null || componentLastUpdated.isBefore(asset.lastUpdated()))) {
                componentLastUpdated = asset.lastUpdated();
            }

            // component createdBy = createdBy field of the asset if it's not "system"
            if (asset.createdBy() != null && !asset.createdBy().isEmpty() && !asset.createdBy().equals("system")) {
                componentCreatedBy = asset.createdBy();
            }
        }
        String encoded = new String(Base64.getUrlEncoder().withoutPadding().encode(
                repository.getName().concat(":").concat(component.getEntityMetadata().getId().getValue()).getBytes()));

        componentInfos.setGroup(component.group());
        componentInfos.setName(component.name());
        componentInfos.setVersion(component.version());
        componentInfos.setFormat(component.format());
        componentInfos.setSize(componentSize);
        componentInfos
                .setSizeMo(BigDecimal.valueOf(componentSize).divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).doubleValue());
        componentInfos
                .setSizeGo(BigDecimal.valueOf(componentSize).divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).doubleValue());
        componentInfos.setCreatedBy(null != componentCreatedBy ? componentCreatedBy : "system");
        componentInfos.setLastUpdated(
                null != componentLastUpdated ? componentLastUpdated.toString("yyyy-MM-dd'T'hh:mm:ss") : "");
        componentInfos.setLastDownloaded(
                null != componentLastDownloaded ? componentLastDownloaded.toString("yyyy-MM-dd'T'hh:mm:ss") : "never");
        componentInfos.setEncoded(encoded);

        return componentInfos;
    }
}
