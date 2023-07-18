import { useState, useEffect } from 'react';
import axios from 'axios';
import { styled } from 'styled-components';

interface Attendee {
  userId: number;
  username: string;
  userimUrl: string;
  pets: Pet[];
}

interface Pet {
  petImUrl: string;
  petName: string;
  gender: string;
}
interface AttendeeInfo {
  petname: string;
  petimgUrl: string;
  petId: number;
}
interface PetItemProps {
  readonly $isSelected: boolean;
}
const UserCard = () => {
  const [attendees, setAttendees] = useState<Attendee[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedPets, setSelectedPets] = useState<number[]>([]);
  const [attendeeInfo, setAttendeeInfo] = useState<AttendeeInfo[]>([]);

  //산책 상세페이지 get 요청
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:3001/articles');

        const attendeesData = response.data.attendees;
        if (attendeesData && attendeesData.length > 0) {
          setAttendees(attendeesData);
        } else {
          console.error('받을 데이터가 존재하지 않습니다.', response.data);
        }
      } catch (error) {
        console.error('attendees 가져오는중 오류 발생:', error);
      }
    };

    fetchData();
  }, []);
  //참가하기 버튼을 눌렀을때 get 요청
  useEffect(() => {
    const fetchAttendeeInfo = async () => {
      try {
        const response = await axios.get('http://localhost:3001/attendee-info');

        const attendeeInfoData = response.data;
        if (attendeeInfoData && attendeeInfoData.length > 0) {
          setAttendeeInfo(attendeeInfoData);
        } else {
          console.error('받을 데이터가 존재하지 않습니다.', response.data);
        }
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

  const handleRegister = async () => {
    try {
      await axios.post('http://localhost:3001/articles/attend', {
        selectedPets,
      });
      console.log('등록이 완료되었습니다.');

      closeModal();
    } catch (error) {
      console.error('등록 중 오류 발생:', error);
    }
  };
  // api에서 gender에 따라 남여 이미지 다르게
  const getPetImageUrl = (gender: string) => {
    if (gender === 'boy') {
      return '/src/assets/petmily-logo-white.png';
    } else if (gender === 'girl') {
      return '/src/assets/petmily-logo-pink.png';
    }

    return '/src/assets/petmily-logo-white.png';
  };

  return (
    <>
      <UserCardContainer>
        <UserCardRow>
          {attendees.map((attendee, index) => (
            <UserCardComponent key={index}>
              <ProfileCard src={attendee.userimUrl} alt="프로필이미지" />
              <Username>{attendee.username}</Username>
              <Role>{index === 0 ? 'Host' : 'Member'}</Role>
              <Tooltip>
                {attendee.pets.map((pet, index) => (
                  <PetInfo key={index}>
                    <PetImage
                      src={getPetImageUrl(pet.gender)}
                      alt="강아지 이미지"
                    />
                    <PetName>{pet.petName}</PetName>
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
              {attendeeInfo.map((pets, index) => (
                <PetItem
                  key={index}
                  onClick={() => handlePetSelect(pets.petId)}
                  $isSelected={selectedPets.includes(pets.petId)}
                >
                  <PetImage src={pets.petimgUrl} alt="강아지 이미지" />
                  <PetName>{pets.petname}</PetName>
                  {selectedPets.includes(pets.petId) && (
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
