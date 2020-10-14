package org.sonatype.nexus.plugins.report.internal.security;

import org.junit.Test;
import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.MemorySecurityConfiguration;

public class ReportSecurityContributorTest extends TestSupport {
    
    @Test
    public void getContribution_test() {
        ReportSecurityContributor securityContributor = new ReportSecurityContributor();
        MemorySecurityConfiguration securityConfiguration = securityContributor.getContribution();
        assert securityConfiguration.getPrivileges() != null;
        assert securityConfiguration.getPrivileges().size() == 1;
        CPrivilege privilege = securityConfiguration.getPrivileges().get(0);
        assert privilege.getId().equals("nx-report-download");
        assert privilege.getDescription().equals("Download repository report permission");
        assert privilege.getType().equals("application");
        assert privilege.getProperty("domain") != null;
        assert privilege.getProperty("domain").equals("report");
        assert privilege.getProperty("actions") != null;
        assert privilege.getProperty("actions").equals("export");
    }

}
