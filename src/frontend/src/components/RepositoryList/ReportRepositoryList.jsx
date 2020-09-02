import React, { useState, useEffect } from 'react';
import UIStrings from '../../constants/UIStrings';
import './ReportRepositoryList.scss';
import Axios from 'axios';
import faChevronRight from '@fortawesome/free-solid-svg-icons';
import {
  ContentBody,
  NxFilterInput,
  NxFontAwesomeIcon,
  NxTable,
  NxTableBody,
  NxTableCell,
  NxTableHead,
  NxTableRow,
  Section,
  Utils,
  SectionActions
} from 'nexus-ui-plugin';

const INITIAL_VALUE = {};

export default function ReportRepositoryList() {
  const [repositoryList, setRepositoryList] = useState(INITIAL_VALUE);
  const isLoaded = repositoryList !== INITIAL_VALUE;
  const isLoading = !isLoaded;
  const [filterText, setFilterText] = useState('');
  const [nameSortDir, setNameSortDir] = useState('asc');
  const [formatSortDir, setFormatSortDir] = useState(null);
  const [typeSortDir, setTypeSortDir] = useState(null);

  useEffect(() => {
    if (isLoaded) {
      return;
    }
    Axios.get('/service/rest/v1/repositories')
      .then(response => response.data)
      .then(data => { setRepositoryList(data.sort((a, b) => a.name > b.name ? 1 : -1)) })
      .catch((error => {
        console(error);
      }));
  });

  function handleReport(repositoryName) {
    try {
      window.open(Utils.urlFromPath('/service/rest/v1/report/' + repositoryName), '_blank');
    } catch (error) {
      console.error(error);
    }
  }

  function filter(value) {
    setFilterText(value);
  }

  function clearFilter() {
    setFilterText('');
  }

  function sort(key) {
    var dir;
    switch (key) {
      case 'name':
        dir = nameSortDir === 'asc' ? 'desc' : 'asc';
        setNameSortDir(dir);
        setFormatSortDir(null);
        setTypeSortDir(null);
        break;
      case 'format':
        dir = formatSortDir === 'asc' ? 'desc' : 'asc';
        setFormatSortDir(dir);
        setNameSortDir(null);
        setTypeSortDir(null);
        break;
      case 'type':
        dir = typeSortDir === 'asc' ? 'desc' : 'asc';
        setTypeSortDir(dir);
        setNameSortDir(null);
        setFormatSortDir(null);
        break;
      default:
        break;
    }
    var sortedList = repositoryList.sort((a, b) => {
      if (a[key] === b[key]) { return 0; }
      if (dir === 'asc') {
        return a[key] > b[key] ? 1 : -1;
      } else if (dir === 'desc') {
        return a[key] < b[key] ? 1 : -1;
      } else {
        return 0;
      }
    });
    setRepositoryList(sortedList);
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
            <NxTableCell isSortable onClick={() => sort('name')} sortDir={nameSortDir}>{UIStrings.REPOSITORY_LIST.repositoryName}</NxTableCell>
            <NxTableCell isSortable onClick={() => sort('format')} sortDir={formatSortDir}>{UIStrings.REPOSITORY_LIST.format}</NxTableCell>
            <NxTableCell isSortable onClick={() => sort('type')} sortDir={typeSortDir}>{UIStrings.REPOSITORY_LIST.type}</NxTableCell>
            <NxTableCell>{UIStrings.REPOSITORY_LIST.url}</NxTableCell>
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