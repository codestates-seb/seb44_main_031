import { useState } from 'react';
import { styled } from 'styled-components';
import { createContext, useRef, useCallback } from 'react';
import { IoIosArrowDropupCircle } from 'react-icons/io';
import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchWalkMates } from '../../api/walkMateAxios';
import { toast } from 'react-toastify';
import WalkMatesHeader, {
  StyeldCreateWalkMateLinkButton,
} from './WalkMatesHeader';
import WalkMatesSerchBar from './WalkMatesSerchBar';
import WalkMatesFilters from './WalkMatesFilters';
import WalkMatesCard from './WalkMatesCard';
import WalkMatesMap from './WalkMatesMap';
import { SibaLoadingSpinner } from '../../components/styles/LoaodingSpinner';
import { createWalkMateURL } from '../../api/reactRouterUrl';

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

export type SelectedFilter = {
  period: { value: string; label: string };
  viewOrder: {
    value: string;
    label: string;
  };
};

// 한 페이지당 가져올 article 개수
const pageSize = 4;

// 컴포넌트
const WalkMateAll = () => {
  const [selectedFilter, setSelectedFilter] = useState<SelectedFilter>({
    period: { value: '29', label: '30일 이내' },
    viewOrder: {
      value: 'asc',
      label: '마감 임박순',
    },
  });
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCard, setSelectedCard] = useState<number | null>(null);

  // 무한스크롤을 위한 useInfiniteQuery 훅
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
    getNextPageParam: (lastPage) => {
      return lastPage?.data.pageinfo.currentPage ===
        lastPage?.data.pageinfo.totalPage
        ? undefined
        : lastPage?.data.pageinfo.currentPage + 1;
    },
  });

  // 무한스크롤을 감지를 위한 인터센션 옵저버 ref 생성
  const intObserver = useRef<IntersectionObserver | null>(null);
  const lastArticleRef = useCallback(
    (lastArticle: HTMLElement | null) => {
      if (isFetchingNextPage) return;

      if (intObserver.current) intObserver.current.disconnect();

      intObserver.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage) {
          fetchNextPage();
        }
      });

      if (lastArticle) intObserver.current.observe(lastArticle);
    },
    [isFetchingNextPage, fetchNextPage, hasNextPage]
  );

  // 위로가기 아이콘 눌렀을때 화면의 최상단으로 스크롤 이동
  const handleScrollIconClick = () => {
    window.scrollTo(0, 0);
  };

  // Error 일때 보여줄 화면
  if (isError) {
    let errorMessage = '';

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

  // Loading 중 일때 보여줄 화면
  if (isLoading) {
    return <SibaLoadingSpinner />;
  }

  // Filter 에 내려줄 유저 주소
  const userAddress = data?.pages[0].data.userInfo.address;

  const content = data?.pages.map((page, index) => {
    // 해당 article 이 없을 경우
    if (page.data.articles.length === 0) {
      return (
        <StyledArticlesNotFoundInfo key={index}>
          <p>현재 위치와 필터로 개설된 모임이 없습니다 🧐</p>
          <p>다른 필터나 키워드를 검색해주세요</p>
          <br />
          <p>또는 나만의 산책 모임을 개설해 보세요 🥰</p>
          <StyeldCreateWalkMateLinkButton to={createWalkMateURL}>
            산책 모임 개설하기
          </StyeldCreateWalkMateLinkButton>
        </StyledArticlesNotFoundInfo>
      );
    }

    // article 이 있을 경우
    return page.data.articles.map((article: Article, index: number) => {
      // 마지막 article 을 찾아서 Card에 intersection observer ref 걸어주기
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
                src="/assets/loading-spinner-dog-1.gif"
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
            src="/assets/loading-spinner-dog-1.gif"
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

const StyledArticlesNotFoundInfo = styled.div`
  margin-top: 80px;
  text-align: center;

  .dog-paws-emoji {
    fill: pink;
  }

  a {
    margin: 20px auto;
  }
`;

const StyledWalkMatesListContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 55%;
`;
