/**
 * Download report controller.
 *
 * @since 3.24
 */
Ext.define('NX.report.controller.DownloadReport', {
  extend: 'NX.controller.Drilldown',
  requires: [
    'NX.Bookmarks',
    'NX.Conditions',
    'NX.Permissions',
    'NX.I18n',
    'NX.controller.ExtDirect'
  ],
  masters: [
    'nx-report-downloadreportfeature nx-report-download-report-list',
    'nx-report-downloadreportfeature nx-report-download-report'
  ],
  stores: [
    'ReportDefinition'
  ],
  models: [
    'ReportDefinition'
  ],

  views: [
    'DownloadReportFeature',
    'DownloadReport',
    'DownloadReportList'
  ],

  refs: [
    {ref: 'feature', selector: 'nx-report-downloadreportfeature'},
    {ref: 'repositoryList', selector: 'nx-report-downloadreportfeaturee nx-report-download-report-list'},
    {ref: 'uploadComponent', selector: 'nx-report-downloadreportfeature nx-report-download-report'},
    {ref: 'successMessage', selector: '#nx-coreui-upload-success-message'}
  ],

  icons: {
    'upload': {
      file: 'upload.png',
      variants: ['x16', 'x32']
    },
    'tick': {
      file: 'tick.png',
      variants: ['x16', 'x32']
    }
  },

  /**
   * @override
   */
  init: function() {
    var me = this;

    me.features = {
      mode: 'browse',
      path: '/DownloadReport',
      text: NX.I18n.get('Download_Report_Text'),
      description: NX.I18n.get('Download_Report_Description'),
      view: 'NX.report.view.DownloadReportFeature',
      group: false,
      iconCls: 'x-fa fa-upload',
      authenticationRequired: false,
      visible: function() {
          return NX.Permissions.check('nexus:component:create');
      }
    };

    me.callParent();

    me.listen({
      controller: {
        '#Refresh': {
          refresh: me.loadStores
        }
      },
      component: {
        'nx-report-downloadreportfeature nx-report-download-report-list': {
          beforerender: me.onBeforeRender
        },
        'nx-report-download-report button[action=remove_upload_asset]': {
          click: me.removeUploadAsset
        },
        'nx-report-download-report button[action=upload]': {
          click: me.doUpload
        },
        'nx-report-download-report button[action=cancel]': {
          click: me.discardUpload
        },
        'nx-report-download-report button[action=add_asset]': {
          click: me.addAsset
        },
        'nx-report-download-report textfield[name$=extension]': {
          change: me.onExtensionChange
        },
        'nx-report-download-report checkbox[name=generate-pom]' : {
          change: me.onGeneratePomChange
        }
      }
    });
  },

  /**
   * @override
   */
  getDescription: function(model) {
    return model.get('name');
  },

  /**
   * @override
   * When a list managed by this controller is clicked, route the event to the proper handler
   */
  onSelection: function(list, model) {
    this.loadUploadPage(model);
  },

  loadUploadPage: function(repoModel) {
    var downloadReportDefinition = this.getStore('DownloadReportDefinition').getById(repoModel.get('format'));
    this.getUploadComponent().loadRecord(downloadReportDefinition, repoModel);
  },

  /**
   * @override
   */
  onNavigate: function() {
    if (this.getFeature()) {
      this.onBeforeRender();
    }
  },

  loadView: function (index, model) {
    this.callParent(arguments);
    if (model) {
        //redraw the panel after visible, to get around issue where file field can be drawn at invalid size
        this.loadUploadPage(model);
    }
  },

  /**
   * @private
   * Load stores based on the bookmarked URL
   */
  onBeforeRender: function() {
    var me = this,
        uploadComponent = me.getUploadComponent(),
        bookmark = NX.Bookmarks.getBookmark(),
        list_ids = bookmark.getSegments().slice(1),
        repoStore = me.getRepositoryList().getStore(),
        repoModel;

    // If the list hasn’t loaded, don't do anything
    if (!uploadComponent) {
      return;
    }

    repoStore.removeAll();

    this.getStore('UploadComponentDefinition').load(function (data) {
        var formats = [];

        data.forEach(function(def) {
          formats.push(def.get('format'));
        });

        repoStore.addFilter([{
          property: 'format',
          filterFn: function(item) {
            return formats.indexOf(item.get('format')) !== -1;
          }
        }, {
          property: 'type',
          value: 'hosted'
        }, {
          property: 'versionPolicy',
          filterFn: function(item) {
            return item.get('versionPolicy') == null || item.get('versionPolicy') !== 'SNAPSHOT';
          }
        }, {
          property: 'status',
          filterFn: function(item) {
            return item.get('status') == null || item.get('status').online !== false;
          }
        }]);
        repoStore.load(function () {
            // Load the asset upload page
            if (list_ids[1]) {
                repoModel = repoStore.getById(decodeURIComponent(list_ids[0]));
                uploadComponent.getStore().load(function () {
                    me.onModelChanged(0, repoModel);
                    me.onSelection(undefined, repoModel);
                });
            }
            // Load the asset list view or repository list view
            else {
                me.reselect();
            }
        });
    });
  },

  removeUploadAsset: function(fileUploadField) {
    var me = this;

    fileUploadField.up('#nx-coreui-upload-component-assets').remove(fileUploadField.up());
    me.refreshRemoveButtonState();
    me.updatePomFileState();
  },

  doUpload: function(button) {
    var me = this,
        fp = button.up('form');

    if(fp.getForm().isValid()) {
      me.setSuccessMessage();

      fp.getForm().submit({
        waitMsg: NX.I18n.get('FeatureGroups_Upload_Wait_Message'),
        success: function(form, action) {
          var message = NX.I18n.format('FeatureGroups_Upload_Successful_Text', form.getValues().repositoryName);
          if (NX.Permissions.check('nexus:search:read')) {
            message += ", " + NX.util.Url.asLink(
                '#browse/search=' + encodeURIComponent('keyword="' + action.result.data + '"'),
                NX.I18n.get('FeatureGroups_Upload_Successful_Link_Text'), '_self');
          }
          me.setSuccessMessage(message);
          me.resetForm();
        },
        failure: function(form, action) {
          var transaction;

          if (!action.result || !action.result.length) {
            NX.Messages.error('An unknown error occurred uploading components');
            console.error(action);
          }
          else {
            transaction = {
              result: action.result[0]
            };
            transaction.result.success = false;
            NX.getApplication().getExtDirectController().checkResponse(null, transaction);
          }
        }
      });
    }
  },

  setSuccessMessage: function (message) {
      var me = this,
          successMessage = me.getSuccessMessage();

      if (message) {
          successMessage.setTitle(message);
          successMessage.show();
      }
      else {
          successMessage.hide();
      }
  },

  discardUpload: function() {
    var me = this;
    me.resetForm();
    me.loadView(me.BROWSE_INDEX);
  },

  resetForm: function() {
    var me = this,
        form = me.getUploadComponent().down('form');

    form.getForm().reset();

    // remove rows
    form.query('fileuploadfield').forEach(function(fileUploadField) {
      me.removeUploadAsset(fileUploadField);
    });

    // create new row
    me.addAsset();

    // clearOnSubmit prevents normal form reset from working...
    form.down('fileuploadfield').inputEl.dom.value = '';
  },

  addAsset: function() {
    var me = this,
        uploadComponent = me.getUploadComponent(),
        form = uploadComponent.down('form');

    uploadComponent.addAssetRow();
    me.refreshRemoveButtonState();
    me.updatePomFileState();
    form.isValid();
  },

  onExtensionChange: function() {
    var me = this,
        form = me.getUploadComponent().down('form');

    me.updatePomFileState();
    form.isValid();
  },

  updatePomFileState: function() {
    var me = this,
        form = me.getUploadComponent().down('form'),
        componentCoordinatesFieldset = form.down('fieldset[title="Component coordinates"]'),
        isPomFilePresent = form.query('textfield[name$=extension][value=pom]').length !== 0;

    if (componentCoordinatesFieldset === null) {
        return;
    }

    componentCoordinatesFieldset.setDisabled(isPomFilePresent);
    if (isPomFilePresent) {
      componentCoordinatesFieldset.mask(NX.I18n.get('FeatureGroups_Upload_Form_DetailsFromPom_Mask'), 'nx-mask-without-spinner');
    }
    else {
      componentCoordinatesFieldset.unmask();
    }
  },

  /**
   * @private
   * Hide remove buttons if there is only one asset displayed
   */
  refreshRemoveButtonState: function() {
    var me = this,
        buttons = me.getUploadComponent().query('button[action=remove_upload_asset]'),
        hidden = (buttons.length === 1);

    buttons.forEach(function(button) {
      button.setVisible(!hidden);
    });
  },

  /**
   * @private
   * Change disabled state of packaging field based on generate pom checkbox
   */
  onGeneratePomChange: function(element) {
    element.up('form').down('textfield[name=packaging]').setDisabled(!element.getValue());
  }
});
