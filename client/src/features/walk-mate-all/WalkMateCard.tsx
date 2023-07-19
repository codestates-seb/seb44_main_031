import { styled } from 'styled-components';
import { CiBookmark } from 'react-icons/ci';
import { FiShare } from 'react-icons/fi';
import { BsCheckCircleFill } from 'react-icons/bs';
import { Link } from 'react-router-dom';

const WalkMateCard = ({ id }: { id: string }) => {
  return (
    <StyledCardContainer id={id} to={`/walk-mate/${id}`} $isSelected={false}>
      <div className="article-image-container">
        <img src="/src/assets/Walkdog.png" alt="" className="article-image" />
      </div>
      <StyledContentsContainer>
        <p className="content-date">SUN, JUL 2 13:00 KST</p>
        <p className="content-title">
          북촌 한옥 마을 돌면서 강아시 산책하실분 구해요~
        </p>
        <p className="content-summary">
          오후 1시에 미니스탑 편의점에서 만나서 애기들 산책하실...
        </p>
        <StyledAttendInfoContainer>
          <span className="attendees">2 참석자</span>
          <span className="left-attendants">2 자리 남음</span>
        </StyledAttendInfoContainer>
        <StyledButtonsContainer>
          <StyledAttendingContainer>
            <BsCheckCircleFill fill="green" />
            <span className="attending-meeting">참석 예정</span>
          </StyledAttendingContainer>
          <StyledBottomButtonsContainer>
            <FiShare className="share-icon" stroke-width="1" />
            <CiBookmark className="bookmark-off-icon" stroke-width="0" />
          </StyledBottomButtonsContainer>
        </StyledButtonsContainer>
      </StyledContentsContainer>
    </StyledCardContainer>
  );
};

interface StyledCardContainerProps {
  readonly $isSelected: boolean;
}

const StyledCardContainer = styled(Link)<StyledCardContainerProps>`
  display: flex;
  border: ${({ $isSelected }) =>
    $isSelected ? '1.5px solid var(--pink-400)' : '1px solid var(--black-600)'};
  border-radius: 20px;
  padding: 25px 20px;
  gap: 10px;

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
    height: 100px;
  }

  .article-image {
    width: 100%;
    border-radius: 30px;
  }
`;

const StyledContentsContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;

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
  width: 100%;
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
  /* width: 40px; */
  width: 80px;
  height: 20px;

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

  .share-icon {
    width: 16px;
    height: 16px;
    color: var(--black-800);
  }
  .bookmark-off-icon {
    width: 16px;
    height: 16px;
    color: var(--black-800);
  }
`;

export default WalkMateCard;
