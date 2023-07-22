import { useState } from 'react';
import { styled } from 'styled-components';
import { createContext, useRef, useCallback } from 'react';
import { IoIosArrowDropupCircle } from 'react-icons/io';
import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchWalkMates } from '../../api/walkMateAxios';
import { toast } from 'react-toastify';
import WalkMatesHeader from './WalkMatesHeader';
import WalkMatesSerchBar from './WalkMatesSerchBar';
import WalkMatesFilters from './WalkMatesFilters';
import WalkMatesCard from './WalkMatesCard';
import WalkMatesMap from './WalkMatesMap';
import { SibaLoadingSpinner } from '../../components/styles/LoaodingSpinner';

export const WalkMateAllContext = createContext<any>(null);

export type Article = {
  articleId: number;
  imageUrls: string[];
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

const pageSize = 4;

export type SelectedFilter = {
  period: { value: string; label: string };
  viewOrder: {
    value: string;
    label: string;
  };
};

// component
const WalkMateAll = () => {
  console.log('WalkMateAll rendered');

  const [selectedFilter, setSelectedFilter] = useState<SelectedFilter>({
    period: { value: '30', label: '30일 이내' },
    viewOrder: {
      value: 'asc',
      label: '모임 가까운순',
    },
  });
  const [searchQuery, setSearchQuery] = useState('');

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
    queryKey: ['articles', selectedFilter, searchQuery],
    queryFn: ({ pageParam = 1 }) =>
      fetchWalkMates(pageParam, pageSize, selectedFilter, searchQuery),
    // getNextPageParam: (lastPage, pages) => {
    //   return lastPage?.data.articles.length === pageSize
    //     ? pages.length + 1
    //     : undefined;
    // },
    getNextPageParam: (lastPage) => {
      return lastPage?.data.pageinfo.currentPage ===
        lastPage?.data.pageinfo.totalPage
        ? undefined
        : lastPage?.data.pageinfo.currentPage + 1;
    },
    // cacheTime: 0,
    // keepPreviousData: true,
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
    let errorMessage = '';
    console.log(error);

    if ((error as any)?.response) {
      errorMessage =
        `${(error as any)?.response.data.status}: ${
          (error as any)?.response.data.message
        }` || '네트워크 에러가 발생했습니다';
    } else {
      errorMessage = `${(error as any)?.message}`;
    }

    toast.error(errorMessage);

    return (
      <StyledWalkMateAllContainer>
        <p>{errorMessage}</p>
      </StyledWalkMateAllContainer>
    );
  }

  if (isLoading) {
    return <SibaLoadingSpinner />;
  }

  // Filter 에 내려줄 유저 주소
  const userAddress = data?.pages[0].data.userInfo.address;

  const content = data?.pages.map((page) => {
    return page.data.articles.map((article: Article, index: number) => {
      if (page.data.articles.length === index + 1) {
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
        <WalkMatesSerchBar
          setSearchQuery={setSearchQuery}
          isLoading={isLoading}
        />
        <WalkMatesFilters
          selectedFilter={selectedFilter}
          setSelectedFilter={setSelectedFilter}
          searchQuery={searchQuery}
          userAddress={userAddress}
          isLoading={isLoading}
        />
        <StyeldMainContentContainer>
          <StyledWalkMatesListContainer>
            {isLoading ? (
              <img
                src="/src/assets/loading-spinner-dog-1.gif"
                alt="dog-loading-spinner"
                className="dog-loading-spinner"
              />
            ) : (
              content
            )}
          </StyledWalkMatesListContainer>
          <WalkMatesMap setSelectedCard={setSelectedCard} />
        </StyeldMainContentContainer>
        {isFetchingNextPage && (
          <img
            src="/src/assets/loading-spinner-dog-1.gif"
            alt="dog-loading-spinner"
            className="dog-loading-spinner"
          />
        )}
        {!hasNextPage && <p>( 더 이상 개설된 모임이 없습니다 )</p>}
        <IoIosArrowDropupCircle
          className="scroll-to-top-icon"
          onClick={handleScrollIconClick}
        />
      </StyledWalkMateAllContainer>
    </WalkMateAllContext.Provider>
  );
};

export default WalkMateAll;

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

  .dog-loading-spinner {
    width: 300px;
    align-self: center;
    border-radius: 90px;
  }
`;

const StyeldMainContentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 25px;
`;

const StyledWalkMatesListContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 55%;
`;
