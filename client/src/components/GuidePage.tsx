import styled from 'styled-components';

const GuidePage = () => {
  return (
    <GuidePageContainer>
      <Title>현재 서비스 준비중입니다</Title>
      <Message>빠른 시일 내에 찾아뵙겠습니다</Message>
    </GuidePageContainer>
  );
};

export default GuidePage;

const GuidePageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f9f0f3; /* Pink background color */
`;

const Title = styled.h1`
  font-size: 24px;
  color: #ff1493; /* Pink text color */
  margin-bottom: 20px;
`;

const Message = styled.p`
  font-size: 18px;
  color: #fff; /* White text color */
  background-color: #ff1493; /* Pink background color for the message */
  padding: 10px 20px;
  border-radius: 8px;
`;
