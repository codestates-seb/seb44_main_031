import { styled } from 'styled-components';
import WalkMatesList from './WalkMatesList';
import WalkMatesMap from './WalkMatesMap';
import WalkMateFilters from './WalkMateFilters';
import WalkMatesHeader from './WalkMatesHeader';
import { createContext } from 'react';
import { IoIosArrowDropupCircle } from 'react-icons/io';

const WalkMateAllContext = createContext<any>(null);

const WalkMateAll = () => {
  const data = '1';

  const handleScrollIconClick = () => {
    window.scrollTo(0, 0);
  };

  return (
    <WalkMateAllContext.Provider value={data}>
      <StyledWalkMateAllContainer>
        <WalkMatesHeader />
        <WalkMateFilters />
        <StyeldMainContentContainer>
          <WalkMatesList />
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
