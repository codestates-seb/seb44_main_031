import { useState, useEffect } from 'react';
import axios from 'axios';
import { styled } from 'styled-components';
import { useParams } from 'react-router-dom';
import { API_URL,AUTH_TOKEN } from '../../api/APIurl';
interface Attendee {
  id: number;
  nickname: string;
  imgUrl: string | null;
  pets: Pet[];
}

interface Pet {
  name: string;
  gender: boolean;
}

interface AttendeeInfo {
  name: string;
  imgUrl: string;
  id: number;
}

interface PetItemProps {
  readonly $isSelected: boolean;
}
interface ArticleData {
  startDate: string;
  endDate: string;
  attendant: number;
  
}

const UserCard = () => {
  const [attendees, setAttendees] = useState<Attendee[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedPets, setSelectedPets] = useState<number[]>([]);
  const [attendeeInfo, setAttendeeInfo] = useState<AttendeeInfo[]>([]);
  const { articleId } = useParams<{ articleId: string }>();
  const [articleData, setArticleData] = useState<ArticleData | null>(null);
  
  //산책 상세페이지 get 요청 
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${API_URL}/articles/${articleId}`, {
          headers: {
            Authorization: AUTH_TOKEN,
          },
        });

        setAttendees(response.data.attendees);
        setArticleData(response.data.article);
        console.log(response.data.article)
      } catch (error) {
        console.error('attendees 가져오는중 오류 발생:', error);
      }
    };

    fetchData();
  }, []);
// 참가하기 버튼 눌렀을때  
  useEffect(() => {
    const fetchAttendeeInfo = async () => {
      try {
        const response = await axios.get<AttendeeInfo[]>(
          `${API_URL}/articles/attendee-info/${articleId}`,
          {
            headers: {
              Authorization: AUTH_TOKEN,
            },
          }
        );
        setAttendeeInfo(response.data.result); 
      
      
      } catch (error) {
        console.error('attendee-info 가져오는 중 오류 발생:', error);
      }
    };

    fetchAttendeeInfo();
  }, []);

  const openModal = () => {
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handlePetSelect = (petId: number) => {
    if (selectedPets.includes(petId)) {
      setSelectedPets(selectedPets.filter((pet) => pet !== petId));
    } else {
      setSelectedPets([...selectedPets, petId]);
    }
  };
// 강아지 데리고 갈 목록들 post
const handleRegister = async () => {
  // 만약 articleData가 null이라면 API로부터 데이터를 가져오지 못한 상태이므로 등록을 막고 에러 처리를 해줄 수 있습니다.
  if (!articleData) {
    console.error('articleData가 유효하지 않습니다.');
    return;
  }

  const { startDate, endDate, attendant } = articleData; // API로부터 받아온 데이터에서 startDate, endDate, attendant를 가져옴

  // 선택된 강아지들의 ID를 배열 형태로 저장합니다.
  const selectedPetIds = selectedPets.map((petId) => petId);

  // 위에서 정의한 데이터 포맷을 만들어줍니다.
  const postData = {
    petIds: selectedPetIds,
    articleId: parseInt(articleId),
    startDate,
    endDate,
    attendant,
  };

  try {
    // POST 요청을 보냅니다.
    await axios.post(`${API_URL}/articles/attend`, postData, {
      headers: {
        Authorization: AUTH_TOKEN,
      },
    });

    console.log('등록이 완료되었습니다.');
    closeModal();
  } catch (error) {
    console.error('등록 중 오류 발생:', error);
  }
};

  const getPetImageUrl = (gender: boolean) => {
    if (gender) {
      return '/src/assets/petmily-logo-pink.png';
    } else {
      return '/src/assets/petmily-logo-white.png';
    }
  };
  console.log('attendeeInfo:', attendeeInfo);
  return (
    <>
      <UserCardContainer>
        <UserCardRow>
          {attendees.map((attendee, index) => (
            <UserCardComponent key={index}>
              <ProfileCard src={attendee.imgUrl ?? undefined} alt="프로필이미지" />
              <Username>{attendee.nickname}</Username>
              <Role>{index === 0 ? 'Host' : 'Member'}</Role>
              <Tooltip>
                {attendee.pets.map((pet, index) => (
                  <PetInfo key={index}>
                    <PetImage
                      src={getPetImageUrl(pet.gender)}
                      alt="강아지 이미지"
                    />
                    <PetName>{pet.name}</PetName>
                  </PetInfo>
                ))}
              </Tooltip>
            </UserCardComponent>
          ))}
        </UserCardRow>
      </UserCardContainer>
      <ButtonBox>
        <button onClick={openModal}>참가하기</button>
      </ButtonBox>

      {showModal && (
        <ModalContainer>
          <ModalContent>
            <h2>산책에 데려갈 강아지 선택</h2>
            <PetList>
              {attendeeInfo !== null && attendeeInfo.map((pets, index) => (
                <PetItem
                  key={index}
                  onClick={() => handlePetSelect(pets.id)}
                  $isSelected={selectedPets.includes(pets.id)}
                >
                  <PetImage src={pets.imgUrl} alt="강아지 이미지" />
                  <PetName>{pets.name}</PetName>
                  {selectedPets.includes(pets.id) && (
                    <PetCheckIcon>&#10003;</PetCheckIcon>
                  )}
                </PetItem>
              ))}
            </PetList>
            <ButtonBox>
              <button onClick={closeModal}>닫기</button>
              <button
                onClick={handleRegister}
                disabled={selectedPets.length === 0}
              >
                등록하기
              </button>
            </ButtonBox>
          </ModalContent>
          <ModalOverlay onClick={closeModal} />
        </ModalContainer>
      )}
    </>
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
  align-items: flex-start;
`;

const UserCardRow = styled.div`
  display: flex;
  margin-left: 10px;
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
  width: 120px;
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

const PetInfo = styled.div`
  display: flex;
  align-items: center;
  margin-top: 5px;
`;

const PetImage = styled.img`
  width: 25px;
  height: 25px;
  border-radius: 50%;
  margin-bottom: 10px;
`;

const PetName = styled.div`
  font-size: 15px;
  margin-left: 10px;
  text-align: center;
`;

const ButtonBox = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
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

const ModalContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 40px;
  border-radius: 10px;
  z-index: 10;
  max-width: 400px;
`;

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 5;
`;

const PetList = styled.div`
  margin-top: 10px;
  gap: 10px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
`;

const PetItem = styled.div<PetItemProps>`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  cursor: pointer;
  width: 100px;
  height: 50px;
  padding: 8px;
  border-radius: 5px;
  transition: background-color 0.3s;
  background-color: ${(props) =>
    props.$isSelected ? 'var(--pink-300)' : 'transparent'};
  color: ${(props) => (props.$isSelected ? 'white' : 'inherit')};

  &:hover {
    background-color: var(--pink-300);
  }
`;

const PetCheckIcon = styled.span`
  margin-left: 1px;
`;