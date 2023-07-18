import React from 'react';

import WalkMateDetailHeader from './WalkMateDetailHeader';
import WalkMateDetailBody from './WalkMateDetailBody';
import { styled } from 'styled-components';
import WalkMateDetailSide from './WalkMateDetailSide';

const WalkMateDetail = () => {
  return (
    <WalkMateDetailContainer>
      <WalkMateDetailHeader />
      <Container>
        <WalkMateDetailBody />
        <WalkMateDetailSide />
      </Container>
    </WalkMateDetailContainer>
  );
};

export default WalkMateDetail;

const WalkMateDetailContainer = styled.div``;
const Container = styled.div`
  display: flex;
  justify-content: center;
  gap: 80px;
`;
