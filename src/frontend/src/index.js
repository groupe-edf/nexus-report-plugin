import UIStrings from './constants/UIStrings';
import DownloadReport from './components/DownloadReport/DownloadReport';

window.plugins.push({
  id: 'nexus-report-plugin',

  features: [
    {
      mode: 'browse',
      path: '/report',
      text: UIStrings.REPORT_FORM.MENU.text,
      description: UIStrings.REPORT_FORM.MENU.description,
      view: DownloadReport,
      authenticationRequired: false,
      iconCls: 'x-fa fa-user',
      visibility: {
        bundle: 'org.sonatype.nexus.plugins.nexus-report-plugin',
        permissions: ['nexus:privileges:read']
      }
    }
  ]
});
