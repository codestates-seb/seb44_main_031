import { styled } from 'styled-components';
const UserCard = () => {
  return (
    <UserCardContainer>
      <UserCardRow>
        <UserCardComponent>
          <ProfileCard src="/src/assets/Profile.png" alt="프로필이미지" />
          <Username>콩이파파</Username>
          <Role>Host</Role>
          <Tooltip>콩이:3살 중성화 o</Tooltip>
        </UserCardComponent>
        <UserCardComponent>
          <MemberProfileCard src="/src/assets/Undefined.png" alt="멤버이미지" />
          <Username>달콩이</Username>
          <Role>Member</Role>
          <Tooltip>달콩이:2살 중성화 x</Tooltip>
        </UserCardComponent>
        <UserCardComponent>
          <MemberProfileCard src="/src/assets/Undefined.png" alt="멤버이미지" />
          <Username>탱자탱자</Username>
          <Role>Member</Role>
          <Tooltip>탱자:1살 중성화 o</Tooltip>
        </UserCardComponent>
      </UserCardRow>
    </UserCardContainer>
  );
};

export default UserCard;

const UserCardContainer = styled.div`
  width: 600px;
  height: 250px;
  border-radius: 30px;
  background-color: var(--pink-200);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const UserCardRow = styled.div`
  display: flex;
`;

const UserCardComponent = styled.div`
  position: relative;
  display: inline-block;
  width: 130px;
  height: 180px;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  border-radius: 10px;
  margin-right: 10px;
  box-shadow: 0px 8px 10px rgba(0, 0, 0, 0.2), 0px 3px 6px rgba(0, 0, 0, 0.15);
  transform: translateY(0);
  transition: transform 0.3s ease-in-out;

  &:hover {
    transform: translateY(-10px);
    box-shadow: 0px 12px 15px rgba(0, 0, 0, 0.3),
      0px 5px 10px rgba(0, 0, 0, 0.2);
  }
`;
const Tooltip = styled.div`
  position: absolute;
  width: 150px;
  left: 50%;
  bottom: 190px;
  transform: translateX(-50%);
  background-color: #646fd4;
  padding: 10px;
  border-radius: 5px;
  color: #fff;
  text-align: center;
  display: none;

  &::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-width: 8px;
    border-style: solid;
    border-color: transparent transparent #646fd4 transparent;
  }

  ${UserCardComponent}:hover & {
    display: block;
  }
`;

const ProfileCard = styled.img`
  width: 80px;
  margin-bottom: 10px;
`;

const Username = styled.div`
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
`;

const Role = styled.div`
  margin-top: 5px;
  font-size: 14px;
  color: gray;
`;
const MemberProfileCard = styled.img`
  width: 60px;
  height: 80px;
  margin-bottom: 10px;
`;
