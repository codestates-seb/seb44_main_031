import { styled } from 'styled-components';

const WalkMateDetailHeader: React.FC = () => {
  return (
    <Container>
      <WalkMateDetailHeaderBox>
        <div className="title">강아지 콩이랑 산책할 산책 메이트 구해요 !!</div>
        <DetailHeaderBox>
          <ProfileImage src="/src/assets/Profile.png" alt="프로필이미지" />
          <DetailTextBox>
            <div className="uptext">Hosted By</div>
            <div className="profilename">콩이 파파 </div>
          </DetailTextBox>
        </DetailHeaderBox>
      </WalkMateDetailHeaderBox>
    </Container>
  );
};
export default WalkMateDetailHeader;

const Container = styled.div`
  border-bottom: 1px solid var(--black-600);
`;
const WalkMateDetailHeaderBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 300px;
  background-color: #ffffff;
  height: 100px;

  .title {
    font-size: 25px;
    font-weight: bold;
  }
`;

const DetailHeaderBox = styled.div`
  display: flex;
  align-items: center;
`;

const ProfileImage = styled.img`
  width: 50px;
  height: 50px;
`;
const DetailTextBox = styled.div`
  margin-top: 10px;
  .uptext {
    font-weight: 300;
    font-size: 13px;
  }
  .profilename {
    margin-top: 2px;
    font-weight: bold;
  }
`;
