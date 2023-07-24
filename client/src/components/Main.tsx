import { styled, keyframes } from 'styled-components';
import { StyledButtonPink3D } from './styles/StyledButtons';

import { useNavigate } from 'react-router-dom';

const Main = () => {
  const navigate = useNavigate();

  const handlePostView = () => {
    // 로그인 여부 확인
    const userId = localStorage.getItem('userId');

    if (userId) {
      // 로그인 상태라면 '/walk-mate/all' 페이지로 이동
      navigate('/walk-mate/all');
    } else {
      // 로그인 상태가 아니라면 '/users/sign-in' 페이지로 이동
      navigate('/users/sign-in');
    }
  };
  return (
    <MainContainer>
      <FirstSection>
        <TitleContainer>
          <Title>당신 근처의 소중한 이웃들과 함께</Title>
          <Subtitle>소중한 반려견들과 함께하는 즐거운 산책 시스템</Subtitle>
          <AnimatedText>
            <Logo src="/assets/petmily-logo-pink.png"/>
            PetMily
            </AnimatedText>
        </TitleContainer>
        <AnimatedTextContainer>
          <MainDog src={'/assets/maindog.png'} alt="강아지사진" />
          
        </AnimatedTextContainer>
      </FirstSection>
      <SecondSection>
        <SecondTitleContainer>
        <SecondTitle>혼자 가기 외로운 산책</SecondTitle>
          <SecondSubtitle>동네 사람들과 같이 산책해요!</SecondSubtitle>
          <StyledButtonPink3D onClick={handlePostView}>게시글 보기</StyledButtonPink3D>
        </SecondTitleContainer>
        <AnimatedTextContainer>
          <Mac src={'/assets/macbook.png'} alt="맥북" />
        </AnimatedTextContainer>
      </SecondSection>
      <ThirdSection>
        <ThirdTitleContainer>
          <ThirdTitle>이웃과 함께 하는 동네생활 </ThirdTitle>
          <ThirdSubtitle>우리 동네의 다양한 이야기를 이웃과 함께 나누어요.</ThirdSubtitle>
          <FlexContainer>
            <ViewdogWrapper>
              <Viewdog src={'/assets/viewdog.png'} alt="강아지사진" />
              <FlexTextContainer>
                <SmallTitle>동네 산책</SmallTitle>
                <SmallSubtitle>혼자 놀기심심해요 같이놀아요.</SmallSubtitle>
              </FlexTextContainer>
            </ViewdogWrapper>
            <ViewdogWrapper>
              <Chat src={'/assets/chat.png'} alt="채팅사진" />
              <FlexTextContainer>
                <SmallTitle> 동네 모임</SmallTitle>
                <SmallSubtitle>이웃과 온 오프라인으로 만나요.</SmallSubtitle>
              </FlexTextContainer>
            </ViewdogWrapper>
            <ViewdogWrapper>
              <Deal src={'/assets/deal.png'} alt="거래" />
              <FlexTextContainer>
                <SmallTitle>중고 거래</SmallTitle>
                <SmallSubtitle>필요한 물품들을 거래해요.</SmallSubtitle>
              </FlexTextContainer>
            </ViewdogWrapper>
          </FlexContainer>
      
        </ThirdTitleContainer>
          <View3 src={'/assets/iphone.png'} alt="강아지사진" />
      </ThirdSection>
    </MainContainer>
  );
};

export default Main;

const MainContainer = styled.div`
  scroll-snap-type: y mandatory;
  height: 100vh;
  scroll-behavior: smooth;
  overflow-x: hidden;
`;
const Logo = styled.img`
  width: 40px;
  height: 40px;

  margin-right: 10px;
`;
const FlexContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
`;

const FlexTextContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 10px;
`;

const FirstSection = styled.section`
  scroll-snap-align: start;
  height: 100vh;
  background-color: var(--beige-100);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow-x: hidden;
`;
const SecondSection = styled.section`
  scroll-snap-align: start;
  height: 100vh;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow-x: hidden;
`;
const ThirdSection = styled.section`
  scroll-snap-align: start;
  height: 100vh;
  background-color: #fff8fd;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow-x: hidden;
`;

const slideIn = keyframes`
  0% {
    opacity: 0;
    transform: translateX(-100%);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
`;
const fadeIn = keyframes`
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
`;

const TitleContainer = styled.div`
  text-align: center;
  opacity: 0;
  animation: ${slideIn} 1s ease-in-out forwards;
  animation-delay: 0.5s;
`;

const Title = styled.h1`

  font-size: 40px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10px; 
`;

const Subtitle = styled.h2`
  font-size: 24px;
  font-weight: normal;
  color: #666666;
  margin-bottom: 30px;
`;

const SecondTitleContainer = styled.div`
  text-align: center;
  opacity: 0;
  animation: ${slideIn} 1s ease-in-out forwards;
  animation-delay: 0.5s;
`;
const SecondTitle = styled.h1`

  font-size: 40px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10px; 
`;

const SecondSubtitle = styled.h2`
  font-size: 24px;
  font-weight: normal;
  color: #666666;
  margin-bottom: 30px;
`;

const ThirdTitleContainer = styled.div`
  text-align: center;
`;

const ThirdTitle = styled.h1`

  font-size: 40px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10px; 
`;

const ThirdSubtitle = styled.h2`
  font-size: 25px;
  font-weight: normal;
  color: #666666;
  margin-bottom: 30px;
`;
const SmallTitle = styled.h3`
  font-size: 18px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20px;
`;

const SmallSubtitle = styled.p`
  font-size: 14px;
  color: #666666;
  margin-bottom: 20px;
`;


const AnimatedTextContainer = styled.div`
  display: flex;
  align-items: center;
  opacity: 0;
  animation: ${fadeIn} 1s ease-in-out forwards;
  animation-delay: 1s;
`;

const MainDog = styled.img`
  width: 700px;
  height: auto;
  margin-right: 10px;
`;

const View3 = styled.img`
margin-left: 80px;
  width: 500px;
  height: 800px;
  border-radius: 20px;
`;
const ViewdogWrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #f6d7e2;
  margin: 10px auto;
`;
const Viewdog = styled.img`
margin-top: 120px;
  width: 50px;
  height: 60px;
  border-radius: 20px;
  margin-bottom: 30px;
`;
const Chat = styled.img`
margin-top: 120px;
  width: 50px;
  height: 60px;
  border-radius: 20px;
  margin-bottom: 30px;
`;
const Deal = styled.img`
margin-top: 120px;
  width: 50px;
  height: 60px;
  border-radius: 20px;
  margin-bottom: 30px;
`;

const Mac = styled.img`
margin-top: 50px;
 width: 600px;
  height: 400px;
`;
const AnimatedText = styled.h3`
  font-size: 24px;
  margin-top: 0px;
  font-weight: bold;
  color: #333333;
`;
