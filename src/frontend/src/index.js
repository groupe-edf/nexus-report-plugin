import UIStrings from './constants/UIStrings';
import ReportRepositoryList from './components/ReportRepositoryList/ReportRepositoryList';

window.plugins.push({
  id: 'nexus-report-plugin',

  features: [
    {
      mode: 'browse',
      path: '/report',
      ...UIStrings.REPORT_FORM.MENU,
      view: ReportRepositoryList,
      iconCls: 'x-fa fa-file',
      visibility: {
        bundle: 'org.sonatype.nexus.plugins.nexus-report-plugin',
        permissions: ['nexus:report:export']
      }
    }
  ]
});
