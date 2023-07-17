import { styled } from 'styled-components';
import { Link } from 'react-router-dom';
import { createWalkMateURL } from '../../api/reactRouterUrl';

const WalkMatesHeader = () => {
  return (
    <StyledTitleContainer>
      <h1 className="page-title">🐶 우리 동네 산책 모임 찾기 🌿</h1>
      <Link to={createWalkMateURL} className="location-link">
        <span>산책 모임 개설하기</span>
      </Link>
    </StyledTitleContainer>
  );
};

const StyledTitleContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  /* justify-content: space-between; */
  align-items: center;
  width: 100%;

  .page-title {
    // title 정가운대로 오도록 수정해야함
    text-align: center;
    font-size: 24px;
  }

  .location-link {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 4px;

    color: white;
    font-size: 14px;
    font-weight: 600;
    text-align: center;

    margin-left: auto;
    margin-right: 10px;
    background-color: var(--pink-400);
    width: 130px;
    height: 40px;
    border-radius: 12px;
  }
`;

export default WalkMatesHeader;
