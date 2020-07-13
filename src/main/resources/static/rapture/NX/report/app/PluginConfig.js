Ext.define('NX.report.app.PluginConfig', {
  '@aggregate_priority' : 100,

  namespaces : [
    'NX.report'
  ],

  requires : [
    'NX.report.app.PluginStrings'
  ],

  controllers : [
      {
        id : 'NX.report.controller.DownloadReport',
        active : function() {
          return NX.app.Application
              .bundleActive('org.sonatype.nexus.plugins.nexus-report-plugin');
        }
      }, 'NX.report.controller.FeatureGroups'
  ]
});
