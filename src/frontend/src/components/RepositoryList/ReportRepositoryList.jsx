import React, { useState, useEffect } from 'react';
import UIStrings from '../../constants/UIStrings';
import './ReportRepositoryList.scss';
import Axios from 'axios';
import { faChevronRight, faPlusCircle, faRedo } from '@fortawesome/free-solid-svg-icons';
import {
  Button,
  ContentBody,
  NxFilterInput,
  NxFontAwesomeIcon,
  NxLoadWrapper,
  NxTable,
  NxTableBody,
  NxTableCell,
  NxTableHead,
  NxTableRow,
  Section,
  Utils,
  SectionActions,
  SectionHeader
} from 'nexus-ui-plugin';
import { useContext } from 'react';

const INITIAL_VALUE = {};

export default function ReportRepositoryList() {
  const [repositoryList, setRepositoryList] = useState(INITIAL_VALUE);
  const isLoaded = repositoryList !== INITIAL_VALUE;
  const isLoading = !isLoaded;
  const [filterText, setFilterText] = useState('');


  useEffect(() => {
    if (isLoaded) {
      return;
    }
    Axios.get('/service/rest/v1/repositories')
      .then(response => response.data)
      .then(data => { setRepositoryList(data) })
      .catch((error => {

      }));
  });

  function handleReport(repositoryName) {
    window.open(Utils.urlFromPath('/service/rest/v1/report/' + repositoryName), '_blank');
    // Axios.get('/service/rest/v1/report/' + repositoryName)
    // .then((response) => {

    // })
    // .catch((error) => {
    //   //ExtJS.showErrorMessage(UIStrings.REPORT_FORM.MESSAGES.DOWNLOAD_ERROR);
    //   console.error(error);
    // });
  }

  function filter(value) {
    setFilterText(value);
  }

  function clearFilter() {
    setFilterText('');
  }

  function sortRepositoriesList(key, dir) {

  }

  return <ContentBody className='nxrm-repository-list'>
    <Section>
      <SectionActions>
        <NxFilterInput
          inputId="filter"
          value={filterText}
          onChange={filter}
          onClear={clearFilter}
          placeholder={UIStrings.REPOSITORY_LIST.filterPlaceHolder} />
      </SectionActions>
      <NxTable>
        <NxTableHead>
          <NxTableRow>
            <NxTableCell isSortable>{UIStrings.REPOSITORY_LIST.repositoryName}</NxTableCell>
            <NxTableCell isSortable>{UIStrings.REPOSITORY_LIST.format}</NxTableCell>
            <NxTableCell isSortable>{UIStrings.REPOSITORY_LIST.type}</NxTableCell>
            <NxTableCell isSortable>{UIStrings.REPOSITORY_LIST.url}</NxTableCell>
            <NxTableCell hasIcon />
          </NxTableRow>
        </NxTableHead>
        <NxTableBody isLoading={isLoading}>
          {Object.keys(repositoryList).filter(repo => repositoryList[repo].name.includes(filterText)).map((repo) => (
            <NxTableRow key={repositoryList[repo].name} isClickable onClick={() => handleReport(repositoryList[repo].name)}>
              <NxTableCell>{repositoryList[repo].name}</NxTableCell>
              <NxTableCell>{repositoryList[repo].format}</NxTableCell>
              <NxTableCell>{repositoryList[repo].type}</NxTableCell>
              <NxTableCell>{repositoryList[repo].url}</NxTableCell>
              <NxTableCell hasIcon><NxFontAwesomeIcon icon={faChevronRight} /></NxTableCell>
            </NxTableRow>
          ))}
        </NxTableBody>
      </NxTable>
    </Section>
  </ContentBody>;
}