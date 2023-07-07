import React from 'react';
import { styled } from 'styled-components';
import UserCard from './UserCard';

const WalkMateDetailBody: React.FC = () => {
  return (
    <BodyContainer>
      <WalkMateBodyContainer>
        <WalkDogImage src="src/assets/Walkdog.png" alt="강아지사진" />
        <TextBox>
          <div className="TextBoxTitle">간단한 소개! (글제목)</div>
          <div className="TextBoxBody">
            우리 콩이가 소형견 이다보니 대형견을 보면 무서워 해요 ㅠㅠ 그래서
            사교성 좋은 강아지들이랑 산책하고 싶어요! (대형견도 사교성 좋은
            강아지면 괜찮을것 같아요!) 매너좋은 반려견 , 견주분들 이셨 으면
            좋겠습니다.!
          </div>
        </TextBox>
        <UserCard />

        <ButtonBox>
          <button>참가하기</button>
          <button>수정하기</button>
        </ButtonBox>
      </WalkMateBodyContainer>
    </BodyContainer>
  );
};

export default WalkMateDetailBody;

const WalkMateBodyContainer = styled.div`
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 600px;
  margin: 0 auto;
`;
const WalkDogImage = styled.img`
  margin-top: 40px;
  border-radius: 30px;
  width: 500px;
  margin-bottom: 40px;
`;
const TextBox = styled.div`
  width: 500px;
  height: 300px;
  padding: 10px;
  border-radius: 10px;

  .TextBoxTitle {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
    color: #333333;
  }

  .TextBoxBody {
    line-height: 1.6;
    font-size: 16px;
    color: #666666;
  }
`;
const ButtonBox = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-left: auto;
  gap: 10px;
  button {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    background-color: var(--pink-400);
    color: white;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
  }
`;
const BodyContainer = styled.div`
  display: flex;
`;
