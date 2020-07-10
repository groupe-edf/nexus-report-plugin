Ext.define('NX.report.app.PluginStrings', {
  '@aggregate_priority': 90,

  singleton: true,
  requires: [
    'NX.I18n'
  ],

  keys: {
    Download_Report_Text: 'Download Report',
    Download_Report_Description: 'Download report with criterias',
    Onboarding_Authenticate: 'Your <b>admin</b> user password is located in <br><b>{0}</b> on the server.',
    Onboarding_LoadStepsError: 'Failed to download the report'
  },

  bundles: {
    'NX.report.view.DownloadReport': {
      Title: 'Download report',
      Description: '<p>Select what you want to appear in your report</p>',
    }
  }
}, function(obj) {
  NX.I18n.register(obj);
});
