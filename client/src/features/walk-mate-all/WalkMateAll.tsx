import { useState } from 'react';
import { styled } from 'styled-components';
import WalkMatesMap from './WalkMatesMap';
import WalkMatesFilters from './WalkMatesFilters';
import WalkMatesHeader from './WalkMatesHeader';
import { createContext, useRef, useCallback } from 'react';
import { IoIosArrowDropupCircle } from 'react-icons/io';
import { useInfiniteQuery } from '@tanstack/react-query';
import WalkMatesCard from './WalkMatesCard';
import { fetchWalkMates } from '../../api/walkMateAxios';
import { toast } from 'react-toastify';

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

const pageSize = 5;

export type SelectedFilter = {
  period: { value: string; label: string };
  viewOrder: {
    value: string;
    label: string;
  };
};

// component
const WalkMateAll = () => {
  const [selectedFilter, setSelectedFilter] = useState<SelectedFilter>({
    period: { value: 'whole-period', label: '전체 기간' },
    viewOrder: {
      value: 'close-to-deadline',
      label: '마감 임박순',
    },
  });

  const [selectedCard, setSelectedCard] = useState<number | null>(null);

  const handleScrollIconClick = () => {
    window.scrollTo(0, 0);
  };

  const {
    data,
    error,
    hasNextPage,
    fetchNextPage,
    isLoading,
    isError,
    isFetchingNextPage,
  } = useInfiniteQuery({
    queryKey: ['articles', selectedFilter],
    queryFn: ({ pageParam = 1 }) =>
      fetchWalkMates(pageParam, pageSize, selectedFilter),
    getNextPageParam: (lastPage, pages) => {
      // console.log(lastPage);
      // console.log(pages);
      // 서버와 연결되면 pageSize 조건문 수정하면됨
      return lastPage?.data.length === pageSize ? pages.length + 1 : undefined;
    },
  });

  console.log(data);

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

  if (isError) {
    toast.error(`Error: ${(error as any).message}`);

    // Check if error.message exists and use it, otherwise provide a fallback error message
    const errorMessage =
      (error as any)?.message || '네트워크 에러가 발생했습니다';

    return <p>Error: {errorMessage}</p>;
  }
  if (isLoading) {
    return (
      <StyledWalkMateAllContainer>
        <img
          src="/src/assets/loading-spinner-dog-1.gif"
          alt="dog-loading-spinner"
        />
      </StyledWalkMateAllContainer>
    );
  }
  // if (isFetchingNextPage) {
  //   toast('Loading more data...', { toastId: 'loading' });
  // }

  const content = data?.pages.map((page) => {
    return page.data.map((article: Article, index: number) => {
      if (page.data.length === index + 1) {
        return (
          <WalkMatesCard
            ref={lastArticleRef}
            key={article.articleId}
            article={article}
            selectedCard={selectedCard}
          />
        );
      }
      return (
        <WalkMatesCard
          key={article.articleId}
          article={article}
          selectedCard={selectedCard}
        />
      );
    });
  });

  return (
    <WalkMateAllContext.Provider value={data}>
      <StyledWalkMateAllContainer>
        <WalkMatesHeader />
        <WalkMatesFilters
          selectedFilter={selectedFilter}
          setSelectedFilter={setSelectedFilter}
        />
        <StyeldMainContentContainer>
          <StyledWalkMatesListContainer>{content}</StyledWalkMatesListContainer>
          {/* {isFetchingNextPage && <p>Loading More Articles...</p>} */}
          <WalkMatesMap setSelectedCard={setSelectedCard} />
        </StyeldMainContentContainer>
        {!hasNextPage && <p>( 더 이상 개설된 모임이 없습니다 )</p>}
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
  gap: 20px;
  width: 55%;
`;
