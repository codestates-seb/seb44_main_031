import React from 'react';
import { styled } from 'styled-components';
import { BsCheckCircleFill } from 'react-icons/bs';
import { Article } from './WalkMateAll';
import BookmarkIcon from './BookmarkIcon';
import { useNavigate } from 'react-router-dom';
import CopyIcon from './CopyIcon';
import { FE_BASE_URL, getWalkMateDetailUrl } from '../../api/reactRouterUrl';
import { stringToLocaleString } from '../../utils/date-utils';
import {
  BODY_LENGTH,
  TITLE_LENGTH,
  sliceContentLengthEndWithDots,
} from './utils/formattingArticle';

type ArticleProps = {
  article: Article;
  selectedCard: number | null;
};

const WalkMatesCard = React.forwardRef<HTMLElement, ArticleProps>(
  ({ article, selectedCard }, ref: React.ForwardedRef<any>) => {
    const navigate = useNavigate();

    const articleDetailPageUrl = `${FE_BASE_URL}/walk-mate/${article.articleId}`;

    const handleCardClick = () => {
      navigate(getWalkMateDetailUrl(article.articleId));
    };
    console.log(article); // FEEDBACK: 불필요한 console.log는 지워주세요.

    return (
      <StyledCardContainer
        id={`card-${article.articleId}`}
        $isSelected={selectedCard === article.articleId}
        ref={ref}
        onClick={handleCardClick}
      >
        <div className="article-image-container">
          {/* <img src="/src/assets/Walkdog.png" alt="" className="article-image" /> */}
          <img src={article.imageUrls[0]} alt="" className="article-image" />
        </div>
        <StyledContentsContainer>
          <p className="content-date">
            {stringToLocaleString(article.startDate)}
          </p>
          <p className="content-title">
            {sliceContentLengthEndWithDots(article.title, TITLE_LENGTH)}
          </p>
          <p className="content-summary">
            {sliceContentLengthEndWithDots(article.body, BODY_LENGTH)}
          </p>
          <StyledAttendInfoContainer>
            <span className="attendees">
              모임 최대 인원 {article.attendant} 명
            </span>
            <span className="left-attendants">{article.lefts} 자리 남음</span>
          </StyledAttendInfoContainer>
          <StyledButtonsContainer>
            {article.isViewerJoining ? (
              <StyledAttendingContainer>
                <BsCheckCircleFill fill="green" />
                <span className="attending-meeting">참석 예정</span>
              </StyledAttendingContainer>
            ) : (
              <div />
            )}
            <StyledBottomButtonsContainer>
              <CopyIcon textToCopy={articleDetailPageUrl} />
              <BookmarkIcon />
            </StyledBottomButtonsContainer>
          </StyledButtonsContainer>
        </StyledContentsContainer>
      </StyledCardContainer>
    );
  }
);

interface StyledCardContainerProps {
  readonly $isSelected: boolean;
}

const StyledCardContainer = styled.div<StyledCardContainerProps>`
  display: flex;
  border: ${({ $isSelected }) =>
    $isSelected ? '1.5px solid var(--pink-400)' : '1px solid var(--black-600)'};
  border-radius: 20px;
  padding: 25px 20px;
  gap: 15px;

  font-size: 12px;
  cursor: pointer;

  &:hover {
    background-color: 'pink';
    transform: translateY(-6px);
    box-shadow: 0px 10px 10px rgba(0, 0, 0, 0.1),
      0px 5px 10px rgba(0, 0, 0, 0.1);
    transition: 0.2s;
  }

  &:active {
    background-color: 'pink';
    transform: translateY(0px);
  }

  .article-image-container {
    width: 165px;
  }

  .article-image {
    width: 100%;
    border-radius: 20px;
    max-height: 130px;
  }
`;

const StyledContentsContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;

  gap: 10px;

  .content-date {
    color: var(--black-700);
    font-weight: 600;
  }

  .content-title {
    color: var(--black-900);
    font-size: 14px;
    font-weight: 600;
  }

  .content-summary {
    color: var(--black-800);
  }
`;

const StyledAttendInfoContainer = styled.div`
  display: flex;
  gap: 10px;

  .attendees {
    color: var(--black-800);
  }

  .left-attendants {
    color: var(--pink-400);
  }
`;

const StyledButtonsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
`;

const StyledAttendingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 2px;

  color: var(--black-800);
  background-color: var(--black-100);
  border-radius: 15px;
  width: 80px;
  height: 25px;

  span {
    font-size: 12px;
    font-weight: 500;
  }
`;

const StyledBottomButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
`;

export default WalkMatesCard;
