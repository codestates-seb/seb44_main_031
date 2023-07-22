import React from 'react';
import { styled } from 'styled-components';
import { Link } from 'react-router-dom';
import { myPageUrl } from '../../api/reactRouterUrl';
import { TbCurrentLocation } from 'react-icons/tb';
import Select from 'react-select';
import { useState } from 'react';
import { SelectedFilter } from './WalkMateAll';
import { getFirstThreeChunks } from './utils/formattingArticle';

interface Option {
  value: string;
  label: string;
}

const optionPeriod = [
  { value: '30', label: '30일 이내' },
  { value: '7', label: '7일 이내' },
  { value: '2', label: '2일 이내' },
];
const optionViewOrder = [
  { value: 'asc', label: '모임 가까운순' },
  { value: 'desc', label: '모임 늦은순' },
];
const optionDistance = [{ value: 'within-3km-radius', label: '반경 3km 이내' }];

const customTheme = (theme: any): any => {
  return {
    ...theme,
    colors: {
      ...theme?.colors,
      primary25: 'rgba(250, 244, 229, 1)',
      primary: 'rgba(255, 64, 129, 1)',
    },
  };
};

const customStyles = {
  option: (provided: any) => ({
    ...provided,
    fontSize: '14px',
    cursor: 'pointer',
  }),
  control: (provided: any) => ({
    ...provided,
    backgroundColor: 'rgba(250, 244, 229, 1)',
    border: 'none',
    bordeRadius: '12px',
    fontSize: '14px',
    height: '40px',
    cursor: 'pointer',
  }),
};

interface WalkMatesFiltersProps {
  selectedFilter: SelectedFilter;
  setSelectedFilter: React.Dispatch<React.SetStateAction<SelectedFilter>>;
  searchQuery: string;
  userAddress: string;
  isLoading: boolean;
  // Add other prop types as needed
}

const WalkMatesFilters = ({
  selectedFilter,
  setSelectedFilter,
  searchQuery,
  userAddress,
  isLoading,
}: WalkMatesFiltersProps) => {
  // 서비스에 거리 단위 필터 기능이 추가되면 WalkMateAll 의 selectedFilter state에 distance state를 병합시키고 삭제하면 됩니다.
  const [distance, setDistance] = useState<Option | null>({
    value: 'within-3km-radius',
    label: '반경 3km 이내',
  });

  const searchResult =
    searchQuery === '' ? null : (
      <StyeldSearchResult>
        <span className="search-result-keyword">"{searchQuery}"</span>
        <span className="search-result-info">검색 결과입니다</span>
      </StyeldSearchResult>
    );

  return (
    <>
      <StyledFilterContainer>
        {/* <h2>서울특별시 인사동 근처</h2> */}
        <h2>{userAddress && getFirstThreeChunks(userAddress)} 근처</h2>
        <Select
          className="select-period"
          options={optionPeriod}
          theme={customTheme}
          styles={customStyles}
          isSearchable={false}
          defaultValue={selectedFilter.period}
          onChange={(selectedOption) => {
            console.log(selectedOption);
            setSelectedFilter((prev) => {
              return {
                ...prev,
                period: selectedOption as { value: string; label: string },
              };
            });
          }}
          isDisabled={isLoading}
        />
        <Select
          className="select-view-order"
          options={optionViewOrder}
          theme={customTheme}
          styles={customStyles}
          isSearchable={false}
          defaultValue={selectedFilter.viewOrder}
          onChange={(selectedOption) => {
            console.log(selectedOption);
            setSelectedFilter((prev) => {
              return {
                ...prev,
                viewOrder: selectedOption as { value: string; label: string },
              };
            });
          }}
          isDisabled={isLoading}
        />
        <Select
          className="select-distance"
          theme={customTheme}
          styles={customStyles}
          isSearchable={false}
          options={optionDistance}
          defaultValue={distance}
          onChange={setDistance}
          isDisabled={true}
        />
        <Link to={myPageUrl} className="location-link">
          <span>내 위치 변경</span>
          <TbCurrentLocation />
        </Link>
      </StyledFilterContainer>
      {searchResult}
    </>
  );
};

const StyledFilterContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  gap: 20px;
  height: 80px;
  background-color: white;
  margin-top: 10px;

  position: sticky;
  top: 73px;
  z-index: 100;

  h2 {
    font-size: 16px;
  }

  .location-link {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 4px;

    color: black;
    font-size: 14px;
    font-weight: 500;
    text-align: center;

    margin-left: auto;
    margin-right: 10px;
    background-color: var(--beige-100);
    width: 120px;
    height: 40px;
    border-radius: 12px;
  }

  .select-period,
  .select-view-order {
    span {
      display: none;
    }
  }
`;

const StyeldSearchResult = styled.div`
  /* text-align: center; */
  margin-bottom: 10px;
  width: 100%;
  background-color: white;
  height: 30px;

  position: sticky;
  top: 153px;
  z-index: 99;

  .search-result-keyword {
    color: var(--pink-400);
    margin-left: 10px;
    margin-right: 8px;
  }
`;

export default WalkMatesFilters;
