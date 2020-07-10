/**
 * @since 3.24
 */
Ext.define('NX.report.model.ReportDefinition', {
  extend: 'Ext.data.Model',
  idProperty: 'format',
  fields: [
    {name: 'format', type: 'string', sortType: 'asUCText'},
    {name: 'name', type: 'string', sortType: 'asUCText'},
    {name: 'fields', type: 'auto' /*object*/},
  ]
});