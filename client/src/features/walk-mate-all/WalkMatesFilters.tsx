import React from 'react';
import { styled } from 'styled-components';
import { Link } from 'react-router-dom';
import { myPageUrl } from '../../api/reactRouterUrl';
import { TbCurrentLocation } from 'react-icons/tb';
import Select from 'react-select';
import { useState } from 'react';
import { SelectedFilter } from './WalkMateAll';

interface Option {
  value: string;
  label: string;
}

const optionPeriod = [
  { value: 'whole-period', label: '전체 기간' },
  { value: 'tomorrow', label: '내일' },
  { value: '7', label: '1주일내' },
];
const optionViewOrder = [
  { value: 'close-to-deadline', label: '마감 임박순' },
  { value: 'latest-registration', label: '최신 등록순' },
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
    'font-size': '14px',
  }),
  control: (provided: any) => ({
    ...provided,
    backgroundColor: 'rgba(250, 244, 229, 1)',
    border: 'none',
    'border-radius': '12px',
    'font-size': '14px',
    height: '40px',
  }),
};

interface WalkMatesFiltersProps {
  selectedFilter: SelectedFilter;
  setSelectedFilter: React.Dispatch<React.SetStateAction<SelectedFilter>>;
  // Add other prop types as needed
}

const WalkMatesFilters = ({
  selectedFilter,
  setSelectedFilter,
}: WalkMatesFiltersProps) => {
  // 서비스에 거리 단위 필터 기능이 추가되면 WalkMateAll 의 selectedFilter state에 distance state를 병합시키고 삭제하면 됩니다.
  const [distance, setDistance] = useState<Option | null>({
    value: 'within-3km-radius',
    label: '반경 3km 이내',
  });

  return (
    <StyledFilterContainer>
      <h2>서울특별시 인사동 근처</h2>
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
        // setPeriod(selectedOption);
        // selectedOption?.value || ''
        // value={period}
      />
      <Select
        className="select-view-order"
        options={optionViewOrder}
        theme={customTheme}
        styles={customStyles}
        isSearchable={false}
        // onChange={setViewOrder}
        // defaultValue={viewOrder}
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
  );
};

const StyledFilterContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  gap: 20px;
  height: 100px;
  background-color: white;

  position: sticky;
  top: 0px;
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

export default WalkMatesFilters;
