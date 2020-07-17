import UIStrings from './constants/UIStrings';

window.plugins.push({
  id: 'nexus-report-plugin',

  features: [
    {
      mode: 'browse',
      path: '/DownloadReport',
      text: UIStrings.REPORT_SETTINGS.MENU.text,
      description: UIStrings.REPORT_SETTINGS.MENU.description,
      view: DownloadReport,
      authenticationRequired: false,
      iconCls: 'x-fa fa-user',
      visibility: {
        bundle: 'org.sonatype.nexus.plugins.nexus-report-plugin',
        permissions: ['nexus:privileges:read']
      }
    },
  ]
});
