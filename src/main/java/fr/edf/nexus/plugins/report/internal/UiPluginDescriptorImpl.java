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
package fr.edf.nexus.plugins.report.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.sisu.Priority;
import org.sonatype.nexus.ui.UiPluginDescriptorSupport;

/**
 * Rapture {@link UiPluginDescriptor} for {@code nexus-report-plugin}.
 *
 * @since 3.17
 */
@Named
@Singleton
@Priority(Integer.MAX_VALUE - 300) // after nexus-proui-plugin
public class UiPluginDescriptorImpl extends UiPluginDescriptorSupport {
  @Inject
  public UiPluginDescriptorImpl() {
    super("nexus-report-plugin");
  }
}
