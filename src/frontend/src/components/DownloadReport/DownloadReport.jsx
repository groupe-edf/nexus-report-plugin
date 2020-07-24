import React, {useState, useEffect} from 'react';
import Axios from 'axios';
import './DownloadReport.scss';
import UIStrings from '../../constants/UIStrings';
import { Button, ContentBody, Checkbox, Textfield, Select, FieldWrapper, Section, SectionFooter } from 'nexus-ui-plugin';

export default function DownloadReport() {
  const [downloadReport, setDownloadReport] = useState({
    enabled: false,
    fileName: ''
  });
  const {enabled, fileName} = downloadReport;
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
  
  return <ContentBody className='nxrm-download-report-field'>
    <Section>
      <Textfield
        name='fileName'
        value={fileName}
        onChange={handleInputChange}
        isRequired={!isLoading}
        className='nxrm-download-report-field'
      />
      <SectionFooter>
        <Button
            variant='primary'
        >
          {UIStrings.REPORT_FORM.ACTIONS.downloadReport}
        </Button>
        <Button
        >
          {UIStrings.REPORT_FORM.ACTIONS.cancel}
        </Button>
      </SectionFooter>
    </Section>
  </ContentBody>;
}