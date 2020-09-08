package fr.edf.nexus.plugins.report.builder.pojo;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.nexus.plugins.report.internal.builders.ComponentInfos;

public class PojoBuilder {

    public static List<ComponentInfos> buildComponentInfosList() {
        List<ComponentInfos> componentInfosList = new ArrayList<>();
        componentInfosList.add(buildComponentInfos("component1"));
        componentInfosList.add(buildComponentInfos("component2"));
        componentInfosList.add(buildComponentInfos("component3"));
        componentInfosList.add(buildComponentInfos("component4"));
        componentInfosList.add(buildComponentInfos("component5"));
        componentInfosList.add(buildComponentInfos("component6"));
        componentInfosList.add(buildComponentInfos("component7"));
        componentInfosList.add(buildComponentInfos("component8"));
        componentInfosList.add(buildComponentInfos("component9"));
        componentInfosList.add(buildComponentInfos("component10"));
        return componentInfosList;
    }
    
    public static ComponentInfos buildComponentInfos(String name) {
        ComponentInfos componentInfos = new ComponentInfos();
        componentInfos.setName(name);
        componentInfos.setCreatedBy("user1");
        componentInfos.setGroup("org.test");
        componentInfos.setSize(Long.valueOf(8900));
        componentInfos.setSizeMo(Double.valueOf(componentInfos.getSize()/1024));
        componentInfos.setSizeGo(Double.valueOf(componentInfos.getSizeMo()/1024));
        componentInfos.setVersion("1.0.0");
        componentInfos.setFormat("jar");
        componentInfos.setLastDownloaded("never");
        componentInfos.setLastUpdated("never");
        componentInfos.setEncoded("encoded");
        return componentInfos;
    }

}
