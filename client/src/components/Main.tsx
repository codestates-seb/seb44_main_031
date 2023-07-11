import { styled, keyframes } from 'styled-components';

import mainDogImage from '/src/assets/maindog.png'; // 이미지 파일 경로를 정확히 설정해주세요.

const Main = () => {
  return (
    <MainContainer>
      <FirstSection>
        <TitleContainer>
          <Title>당신 근처의 소중한 이웃들과 함께</Title>
          <Subtitle>소중한 반려견들과 함께하는 즐거운 산책 시스템</Subtitle>
        </TitleContainer>
        <AnimatedTextContainer>
          <MainDog src={mainDogImage} alt="강아지사진" />
          <AnimatedText>PetMily</AnimatedText>
        </AnimatedTextContainer>
      </FirstSection>
      <SecondSection>
        <TitleContainer>
          <Title>당신 근처의 소중한 이웃들과 함께</Title>
          <Subtitle>소중한 반려견들과 함께하는 즐거운 산책 시스템</Subtitle>
        </TitleContainer>
        <AnimatedTextContainer>
          <MainDog src={mainDogImage} alt="강아지사진" />
          <AnimatedText>PetMily</AnimatedText>
        </AnimatedTextContainer>
      </SecondSection>
    </MainContainer>
  );
};

export default Main;

const MainContainer = styled.div`
  scroll-snap-type: y mandatory;
  height: 100vh;
  overflow-y: scroll;
  scroll-behavior: smooth;
  overflow-x: hidden;
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
  background-color: #c47070;
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
  margin-bottom: 10px; /* 사진과의 간격을 조정 */
`;

const Subtitle = styled.h2`
  font-size: 24px;
  font-weight: normal;
  color: #666666;
`;

const fadeIn = keyframes`
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
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

const AnimatedText = styled.h3`
  font-size: 24px;
  font-weight: bold;
  color: #333333;
`;
