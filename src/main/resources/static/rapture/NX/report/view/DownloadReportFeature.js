/**
 * @since 3.24
 */
Ext.define('NX.report.view.DownloadReportFeature', {
  extend: 'NX.view.drilldown.Drilldown',
  alias: 'widget.nx-report-downloadreportfeature',

  iconName: 'download-component-default',

  masters: [
    {xtype: 'nx-report-download-report-list'},
    {xtype: 'nx-report-download-report'}
  ]
});
