import { styled } from 'styled-components';
import { Link } from 'react-router-dom';
import { createWalkMateURL } from '../../api/reactRouterUrl';

const WalkMatesHeader = () => {
  return (
    <StyledTitleContainer>
      <h1 className="page-title">🐶 우리 동네 반려견 산책 모임 찾기 🌿</h1>
      <StyeldCreateWalkMateLinkButton to={createWalkMateURL}>
        산책 모임 개설하기
      </StyeldCreateWalkMateLinkButton>
    </StyledTitleContainer>
  );
};

const StyledTitleContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: center;
  width: 100%;

  .page-title {
    text-align: center;
    font-size: 24px;
  }
`;

export const StyeldCreateWalkMateLinkButton = styled(Link)`
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
`;

export default WalkMatesHeader;
