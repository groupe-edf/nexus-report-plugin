package fr.edf.nexus.plugins.report.internal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
@Priority(Integer.MAX_VALUE - 300) // after nexus-rapture
public class UiPluginDescriptorImpl extends org.sonatype.nexus.rapture.UiPluginDescriptorSupport {

    @Inject
    public UiPluginDescriptorImpl(String artifactId) {
        super("nexus-report-plugin");
        setNamespace("NX.report");
        setConfigClassName("NX.report.app.PluginConfig");
    }
}
