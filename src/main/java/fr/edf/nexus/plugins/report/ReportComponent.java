package fr.edf.nexus.plugins.report;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;

import com.softwarementors.extjs.djn.config.annotations.DirectAction;

@Named
@Singleton
@DirectAction(action = "report_Download")
public class ReportComponent extends DirectComponentSupport implements StateContributor {
    
    @Inject
    ReportConfiguration configuration;

    @Override
    public Map<String, Object> getState() {
        Map<String, Object> state = new HashMap<>();
        state.put("report", configuration.isEnabled());
        return state;
    }

}
