import { UIStrings } from 'nexus-ui-plugin';
import React from 'react';

export default {
  ...UIStrings,

  REPORT_FORM: {
    MENU: {
      text: 'Report',
      description: 'Download a report with usage statistics of your repository'
    },
    MESSAGES: {
      LOAD_ERROR: 'An error occurred while loading components statistics, see console for more details',
      DOWNLOAD_SUCCESS: 'Statistics report downloaded',
      DOWNLOAD_ERROR: 'An error occurred while downloading the file, see console for more details'
    },
    ACTIONS: {
      downloadReport: 'Download Report',
      cancel: 'Cancel'
    },
    LABELS: {
      fileName: 'File Name'
    } 
  },
  REPOSITORY_LIST: {
    filterPlaceHolder: 'Filter',
    repositoryName: 'Repository Name',
    format: 'format',
    type: 'type',
    url: 'url'
  }
};
