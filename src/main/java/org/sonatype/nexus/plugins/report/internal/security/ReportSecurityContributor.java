/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.report.internal.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.MemorySecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityContributor;
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege;

/**
 * Nexus report security configuration.
 *
 * @since 3.0
 */
@Named
@Singleton
class ReportSecurityContributor implements SecurityContributor {
    @Override
    public MemorySecurityConfiguration getContribution() {

        /** Privilege to download a report */

        MemoryCPrivilege privilege = new MemoryCPrivilege();
        privilege.setId("nx-report-download");
        privilege.setDescription("Download repository report permission");
        privilege.setType("application");
        privilege.setProperty("domain", "report");
        privilege.setProperty("actions", "export");

        List<CPrivilege> privileges = new ArrayList<>();
        privileges.add(privilege);

        MemorySecurityConfiguration securityConfiguration = new MemorySecurityConfiguration();
        securityConfiguration.setPrivileges(privileges);

        return securityConfiguration;

    }
}
