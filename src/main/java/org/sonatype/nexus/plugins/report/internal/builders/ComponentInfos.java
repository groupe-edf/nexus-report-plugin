package org.sonatype.nexus.plugins.report.internal.builders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComponentInfos {

    private String group;
    private String name;
    private String version;
    private String format;
    private Long size;
    private Long sizeMo;
    private Long sizeGo;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private LocalDateTime lastDownloaded;
    private String encoded;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSizeMo() {
        return sizeMo;
    }

    public void setSizeMo(Long sizeMo) {
        this.sizeMo = sizeMo;
    }

    public Long getSizeGo() {
        return sizeGo;
    }

    public void setSizeGo(Long sizeGo) {
        this.sizeGo = sizeGo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastDownloaded() {
        return lastDownloaded;
    }

    public void setLastDownloaded(LocalDateTime lastDownloaded) {
        this.lastDownloaded = lastDownloaded;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    protected static List<String> getComponentInfosTitles() {
        List<String> componentInfosTitles = new ArrayList<>();
        componentInfosTitles.add("Group");
        componentInfosTitles.add("Name");
        componentInfosTitles.add("Version");
        componentInfosTitles.add("Format");
        componentInfosTitles.add("Size (octets)");
        componentInfosTitles.add("Size (Mo)");
        componentInfosTitles.add("Size (Go)");
        componentInfosTitles.add("Created by");
        componentInfosTitles.add("Last updated");
        componentInfosTitles.add("Last downloaded");
        componentInfosTitles.add("repCompId");
        return componentInfosTitles;
    }
}
