/**
 * Registers all feature groups for report plugin.
 *
 * @since 3.0
 */
Ext.define('NX.report.controller.FeatureGroups', {
  extend: 'NX.app.Controller',
  requires: [
    'NX.I18n'
  ],

  /**
   * @override
   */
  init: function () {
    this.getApplication().getFeaturesController().registerFeature([
      {
        mode: 'browse',
        path: '/DownloadReport',
        text: NX.I18n.get('Download_Report_Text'),
        description: NX.I18n.get('Download_Report_Description'),
        group: true,
        iconConfig: {
          file: 'upload.png',
          variants: ['x16', 'x32']
        }
      }]);
  }
});
