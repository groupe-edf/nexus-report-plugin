import React, {useState, useEffect} from 'react';
import Axios from 'axios';
import './DownloadReport.scss';
import UIStrings from '../../constants/UIStrings';
import { Button, ContentBody, Checkbox, Textfield, Select, FieldWrapper, Section, SectionFooter } from 'nexus-ui-plugin';

export default function DownloadReport() {
  const [downloadReport, setDownloadReport] = useState({
    enabled: false,
    repositoryName: ''
  });
  const repositoryName = 'maven-releases';
  const [isLoading, setIsLoading] = useState(true);
  
  const handleInputChange = (event) => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const fileName = target.fileName || target.id;
    setDownloadReport({
      ...downloadReport,
      [fileName]: value
    });
  };

  const handleReport = () => {
      Axios.get('/service/rest/v1/report/' + repositoryName)//repository.get('name'))
      .then(() => {
        //ExtJS.showSuccessMessage(UIStrings.REPORT_FORM.MESSAGES.DOWNLOAD_SUCCESS);
      })
      .catch((error) => {
        //ExtJS.showErrorMessage(UIStrings.REPORT_FORM.MESSAGES.DOWNLOAD_ERROR);
        console.error(error);
      });
  };
  
  return <ContentBody className='nxrm-download-report-field'>
    <Section>
      <SectionFooter>
        <Button
            variant='primary'
            onClick={handleReport}
            download
        >
          {UIStrings.REPORT_FORM.ACTIONS.downloadReport}
        </Button>
        <Button>
          {UIStrings.REPORT_FORM.ACTIONS.cancel}
        </Button>
      </SectionFooter>
    </Section>
  </ContentBody>;
}