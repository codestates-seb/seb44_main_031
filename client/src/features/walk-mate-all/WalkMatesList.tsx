import { styled } from 'styled-components';
import WalkMateCard from './WalkMateCard';

const WalkMatesList = () => {
  return (
    <StyledWalkMatesListContainer>
      <WalkMateCard />
      <WalkMateCard />
      <WalkMateCard />
      <WalkMateCard />
      <WalkMateCard />
      <WalkMateCard />
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
