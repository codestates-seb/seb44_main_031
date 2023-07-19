import { styled } from 'styled-components';
import WalkMateCard from './WalkMateCard';

const articleId = '1';

const WalkMatesList = () => {
  return (
    <StyledWalkMatesListContainer>
      <WalkMateCard id={articleId} />
      <WalkMateCard id={'2'} />
      <WalkMateCard id={'3'} />
      <WalkMateCard id={'4'} />
      <WalkMateCard id={'5'} />
      <WalkMateCard id={'6'} />
    </StyledWalkMatesListContainer>
  );
};

const StyledWalkMatesListContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  /* width: 480px; */
  width: 60%;
`;

export default WalkMatesList;
