import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { RootState, useAppDispatch } from '../../store/store';
import {fetchUsers } from './myPageSlice';
import userProfileImg from '../../../public/assets/Profile.png';
import { styled } from 'styled-components';

// import { useNavigate } from 'react-router-dom';
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  background-color: #f1f2f3;
  font-size: 0.8rem;
`;
const UserContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f1f2f3;
  font-size: 0.8rem;
`;

const UserPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 400px;
  height: 300px;
  justify-content: center;
  align-items: center;
  background-color: #f1f2f3;
  font-size: 0.8rem;
`;

const UserTitle = styled.div`
  font-size: 20px;
`;

const UserCard = styled.div`
  text-align: center;
`;



const UserImg = styled.img`
  width: 180px;
  height: 180px;
  border-radius: 50%;
  border: solid 1px red;
`;

const UserName = styled.div`
  font-size: 20px;
  font-weight: bold;
`;

const DogPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 800px;
  justify-content: center;
  align-items: center;
  background-color: #f1f2f3;
  font-size: 0.8rem;
    ul{
        display: flex;
        flex-direction: column;
        margin:10px;
        li{
            margin : 10px;
            div{
                border-radius: 2%;
            }
        }
    }
`;
const PetImgName = styled.div`
    display: flex;
    flex-direction:column;
    margin-bottom: 10px;
`
const PetCard = styled.div`
  width: 450px;
  height: 250px;
  display: flex;
  border: 2px solid  #f4337d;
  justify-content: baseline;
  align-items: center;
  font-size: 15px;
  background-color: var(--pink-100);
  div{
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .lableAlign{
        width:200px;
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 10px; /* Add some space between the label and value */
      width: 200px; /* Set a fixed width for the labels */
      margin:5px;
      font-weight: 700;
      label{
        width: 80px;
        text-align: right;
      }
    }
  }
`;
const PetImg = styled.img`
  width: 150px;
  height: 150px;
  border-radius: 2%;
  margin: 20px;
`;

const Mypage = () => {
  const dispatch = useAppDispatch();
  const profile = useSelector((state: RootState) => state.mypage.profile);
  useEffect(() => {
    // 현재 URL 가져오기
    const currentURL = window.location.href;
    
    // URL에서 마지막 숫자 추출
    const regex = /\/(\d+)(?:\?.*)?$/;
    const match = currentURL.match(regex);
    const lastNumber = match ? Number(match[1]) : 1;
    
    // 유저 아이디로 API 호출
    if (lastNumber) {
      dispatch(fetchUsers(lastNumber));
    }
  }, [dispatch]);

  return (
    <Container>
      <UserContainer>
        <UserPart>
          <UserTitle>{profile.username}님의 페이지 입니다</UserTitle>
          <UserCard>
            <UserImg src={userProfileImg}></UserImg>

            <UserName>{profile.username}</UserName>
          </UserCard>         
        </UserPart>
      </UserContainer>
      <DogPart>
        <ul>
          {profile.pets.map((pet) => (
            <li key={pet.id}>
              <PetCard>
                <PetImgName>
                    <PetImg src={pet.imgUrl}></PetImg>
                    <h2>{pet.name}</h2>
                </PetImgName>
               <div>
                    <div className='lableAlign'>
                        <label htmlFor='mbti'>강아지 mbti :</label><div id='mbti'> {pet.mbti}</div>
                    </div>
                    <div className='lableAlign'>
                        <label htmlFor='breedName'>견종 :</label><div id='breedName'>{pet.breedName}</div>
                    </div>
                    <div className='lableAlign'>
                        <label htmlFor='neutralization'>중성화 여부 :</label><div id='neutralization'>{pet.neutralization ? ' O ' : ' X '}</div>
                    </div>
                    
                    <div className='lableAlign'>
                        <label htmlFor='gender'>성별 :</label><div id='gender'>{pet.gender ? '♀' : '♂'}</div>
                    </div>
                    <div className='lableAlign'>
                        <label htmlFor='birth'>생년월일 :</label> <div id='birth'>{pet.birth}</div>
                    </div>                              
                </div>
              </PetCard>
            </li>
          ))}
        </ul>
      </DogPart>
    </Container>
  );
};

export default Mypage;

