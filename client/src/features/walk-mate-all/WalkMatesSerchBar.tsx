import React from 'react';
import styled from 'styled-components';

interface WalkMatesSerchBarProps {
  setSearchQuery: React.Dispatch<React.SetStateAction<string>>;
  isLoading: boolean;
}

const WalkMatesSerchBar = ({
  setSearchQuery,
  isLoading,
}: WalkMatesSerchBarProps) => {
  const handleSearchSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const searchInputValue = (
      event.currentTarget.querySelector(
        '[name="searchInput"]'
      ) as HTMLInputElement
    )?.value.trim();

    setSearchQuery(searchInputValue);
  };

  return (
    <SearchForm onSubmit={handleSearchSubmit}>
      <SearchInput
        type="text"
        name="searchInput"
        placeholder="검색할 키워드를 입력해 주세요"
        disabled={isLoading}
      />
      <SearchButton type="submit" disabled={isLoading}>
        Search
      </SearchButton>
    </SearchForm>
  );
};

export default WalkMatesSerchBar;

const SearchForm = styled.form`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  background-color: white;
`;

const SearchInput = styled.input`
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 30%;

  &:focus {
    outline: 1px var(--pink-400) solid;
    border: 1px solid pink;
  }
`;

const SearchButton = styled.button`
  padding: 8px 16px;
  margin-left: 8px;
  background-color: var(--pink-400);
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
