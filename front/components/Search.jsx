import React, { useState } from 'react';
import styled from 'styled-components';
import { getSearchTitle, getSearchAuthor, getItems } from '@/apis/item/item.api';

const Search = ({ onSearchResults }) => {
  const [filter, setFilter] = useState('Title'); // 기본 필터 값은 'Title'
  const [searchTerm, setSearchTerm] = useState('');

  // 필터 변경 핸들러
  const handleFilterChange = (filterOption) => {
    setFilter(filterOption);
  };

  // 검색 실행 핸들러
  const handleSearch = async (e) => {
    e.preventDefault();

    if (!searchTerm.trim()) {
      console.warn('검색어를 입력해주세요');
      return;
    }

    try {
      let searchResults;
      if (filter === 'Title') {
        searchResults = await getSearchTitle(searchTerm);  // 제목으로 검색
      } else {
        searchResults = await getSearchAuthor(searchTerm);  // 사용자로 검색
      }
      
      // 검색 결과가 없을 때 처리
      if (searchResults.length === 0) {
        onSearchResults([], '검색 결과가 없습니다.');
      } else {
        onSearchResults(searchResults); // 검색 결과를 MainPage로 전달
      }
    } catch (error) {
      // 400 에러 발생 시 처리
      if (error.response) {
        onSearchResults([], '검색 결과가 없습니다.');
      } else {
        console.error('검색 실패:', error.response);
      }
    }
  };

  // 전체보기 실행 핸들러
  const handleShowAll = async () => {
    try {
      const allResults = await getItems();
      const sortedItems = allResults.sort((a, b) => b.id - a.id);  // 결과 정렬
      onSearchResults(sortedItems);  // 전체 데이터를 MainPage로 전달
    } catch (error) {
      console.error('전체보기 실패:', error);
      onSearchResults([]); 
    }
  };

  return (
    <AllSearchContainer>
      <AllSearchBtnContainer>
        <AllSeachBtn onClick={handleShowAll}>전체보기</AllSeachBtn> {/* 독립된 전체보기 버튼 */}
      </AllSearchBtnContainer>
      <SearchContainer onSubmit={handleSearch}>
        <FilterSelect value={filter} onChange={(e) => handleFilterChange(e.target.value)}>
          <option value="Title">제목</option>
          <option value="User">사용자</option>
        </FilterSelect>
        <SearchInput
          type="text"
          placeholder="검색어를 입력하세요"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <SearchButton type="submit">검색</SearchButton>
      </SearchContainer>
    </AllSearchContainer>
  );
};

const AllSearchContainer = styled.div`
  display: flex;
  align-items: center;
`;

const AllSearchBtnContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  padding: 10px;
  margin-bottom: 20px;
`;

const AllSeachBtn = styled.button`
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #218838;
  }

  &:active {
    background-color: #1e7e34;
  }
`;

const SearchContainer = styled.form`
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
`;

const FilterSelect = styled.select`
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
`;

const SearchInput = styled.input`
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  width: 300px;
`;

const SearchButton = styled.button`
  padding: 10px 15px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  &:hover {
    background-color: #0056b3;
  }
`;

export default Search;
