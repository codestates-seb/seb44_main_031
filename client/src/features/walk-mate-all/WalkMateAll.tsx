import { useState } from 'react';
import { styled } from 'styled-components';
import WalkMatesMap from './WalkMatesMap';
import WalkMateFilters from './WalkMateFilters';
import WalkMatesHeader from './WalkMatesHeader';
import { createContext, useRef, useCallback } from 'react';
import { IoIosArrowDropupCircle } from 'react-icons/io';
import { axiosInstance, getArticlesUrl } from '../../api/walkMateAxios';
import { useInfiniteQuery } from '@tanstack/react-query';
import WalkMateCard from './WalkMateCard';

export const WalkMateAllContext = createContext<any>(null);

export type Article = {
  articleId: number;
  imgUrl: string;
  startDate: string;
  endDate: number;
  title: string;
  body: string;
  location: string;
  latitude: number;
  longitude: number;
  attendant: number;
  lefts: number;
  isViewerJoining: boolean;
};

// fetch data
const fetchWalkMates = async (
  pageParam = 1,
  size = 10,
  selectedFilter: string
) => {
  const response = await axiosInstance.get(
    getArticlesUrl(pageParam, size, selectedFilter)
  );
  return response.data;
};

// component
const WalkMateAll = () => {
  const [selectedFilter, setSelectedFilter] = useState<string>('');

  const handleScrollIconClick = () => {
    window.scrollTo(0, 0);
  };

  const {
    data,
    error,
    hasNextPage,
    fetchNextPage,
    isLoading,
    isFetchingNextPage,
    status,
  } = useInfiniteQuery({
    queryKey: ['articles', selectedFilter],
    queryFn: ({ pageParam = 1 }) =>
      fetchWalkMates(pageParam, 10, selectedFilter),
    getNextPageParam: (lastPage, pages) => {
      return lastPage?.length ? pages.length + 1 : undefined;
    },
  });

  const intObserver = useRef<IntersectionObserver | null>(null);
  const lastArticleRef = useCallback(
    (lastArticle: HTMLElement | null) => {
      if (isFetchingNextPage) return;

      if (intObserver.current) intObserver.current.disconnect();

      intObserver.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage) {
          console.log('마지막 산책 글 근처임');
          fetchNextPage();
        }
      });

      if (lastArticle) intObserver.current.observe(lastArticle);
    },
    [isFetchingNextPage, fetchNextPage, hasNextPage]
  );

  if (status === 'error') {
    // Check if error.message exists and use it, otherwise provide a fallback error message
    const errorMessage = (error as any)?.message || 'An error occurred';

    return <p>Error: {errorMessage}</p>;
  }
  if (isLoading) return <p>로딩중 입니다</p>;

  const content = data?.pages.map((page) => {
    return page.map((article: Article, index: number) => {
      if (page.length === index + 1) {
        return (
          <WalkMateCard
            ref={lastArticleRef}
            key={article.articleId}
            article={article}
          />
        );
      }
      return <WalkMateCard key={article.articleId} article={article} />;
    });
  });

  return (
    <WalkMateAllContext.Provider value={data}>
      <StyledWalkMateAllContainer>
        <WalkMatesHeader />
        <WalkMateFilters setSelectedFilter={setSelectedFilter} />
        <StyeldMainContentContainer>
          <StyledWalkMatesListContainer>{content}</StyledWalkMatesListContainer>
          {isFetchingNextPage && <p>Loading More Articles...</p>}
          <WalkMatesMap />
        </StyeldMainContentContainer>
        <IoIosArrowDropupCircle
          className="scroll-to-top-icon"
          onClick={handleScrollIconClick}
        />
      </StyledWalkMateAllContainer>
    </WalkMateAllContext.Provider>
  );
};

const StyledWalkMateAllContainer = styled.div`
  width: 1000px;
  margin: 60px auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 6px;

  .scroll-to-top-icon {
    position: fixed;
    bottom: 4vh;
    right: 2vw;
    fill: var(--pink-400);
    width: 60px;
    height: 60px;
    cursor: pointer;
  }
`;

const StyeldMainContentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 25px;
`;

export default WalkMateAll;

const StyledWalkMatesListContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 60%;
`;
