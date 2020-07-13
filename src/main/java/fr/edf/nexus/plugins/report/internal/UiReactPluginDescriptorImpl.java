package fr.edf.nexus.plugins.report.internal;

import static java.util.Arrays.asList;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.sisu.Priority;
import org.eclipse.sisu.space.ClassSpace;
import org.sonatype.nexus.ui.UiPluginDescriptor;
import org.sonatype.nexus.ui.UiUtil;

/**
 * UI descriptor {@link UiPluginDescriptor} for {@code nexus-report-plugin}.
 *
 * @since 3.17
 */
@Named
@Singleton
@Priority(Integer.MAX_VALUE - 300) // after nexus-proui-plugin
public class UiReactPluginDescriptorImpl implements UiPluginDescriptor {

    private final List<String> scripts;

    private final List<String> debugScripts;

    private final List<String> styles;

    @Inject
    public UiReactPluginDescriptorImpl(final ClassSpace space) {
        scripts = asList(UiUtil.getPathForFile("nexus-report-bundle.js", space));
        debugScripts = asList(UiUtil.getPathForFile("nexus-report-bundle.debug.js", space));
        styles = asList(UiUtil.getPathForFile("nexus-report-bundle.css", space));
    }
    
    @Override
    public String getName() {
      return "nexus-report-plugin";
    }

    @Nullable
    @Override
    public List<String> getScripts(final boolean isDebug) {
      return isDebug ? debugScripts : scripts;
    }

    @Nullable
    @Override
    public List<String> getStyles() {
      return styles;
    }
}
