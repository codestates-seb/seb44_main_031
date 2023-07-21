import { useCallback, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState, useAppDispatch } from '../../store/store';
import { fetchUsersname, fetchUsers, fetchPassword } from './myPageSlice';
import userProfileImg from '/src/assets/Profile.png';
import { styled } from 'styled-components';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import Map from './Map';
import { BsFillGearFill, BsPlusCircleDotted } from 'react-icons/bs';
import AddPetModal from './AddPetModal';
import ModifyPetModal from './ModifyPetModal';
import UsernameChangeModal from './UsernameChangeModal';
import axios, { isAxiosError, AxiosError } from 'axios';
import ModifyPasswordModal from './ModifyPasswordModal';
import { useNavigate } from 'react-router-dom';
interface PetData {
  petId: number;
  name: string;
  birth: string;
  gender: boolean;
  neutralization: boolean;
  breed: number;
  mbti: string;
  image: File | null;
}
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

const UserPartButtons = styled.div`
  display: flex;
  width: 280px;
  justify-content: center;
  margin: 10px;
  StyledButtonPink3D {
    width: 100px;
  }
`;

const UserImg = styled.img`
  width: 180px;
  height: 180px;
  border-radius: 50%;
  border: solid 1px red;
`;

const UserImgRe = styled.div`
  float: right;
  margin-top: 140px;
  width: 5px;
  height: 5px;
  input {
    border-radius: 50%;
    border: solid 1px red;
    display: none;
  }
  .Gear {
    font-size: 24px;
  }
`;
const UserName = styled.div`
  font-size: 20px;
  font-weight: bold;
`;
const InputUsername = styled.div`
  display: flex;
  flex-direction: column;
  margin: 6px 0 6px;

  > label {
    text-align: left;
    margin: 2px 0 2px;
    padding: 0 2px;
    font-size: 1rem;
    font-weight: bold;
  }

  > input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
  ${StyledButtonPink3D} {
    height: 30px;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
  }
`;

const InputPassword = styled.div`
  display: flex;
  flex-direction: column;
  margin: 6px 0 6px;

  > div.input-password-label {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;

    > div {
      text-align: left;
      margin: 2px 0 2px;
      padding: 0 2px;
      font-size: 1rem;
      font-weight: 800;
    }
  }

  > input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
`;
const IdCheckForm = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;
const MapPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 400px;
  height: 300px;
  justify-content: center;
  align-items: center;
  background-color: #f1f2f3;
  font-size: 0.8rem;
`;

const DogPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 800px;
  justify-content: center;
  align-items: center;
  background-color: #f1f2f3;
  font-size: 0.8rem;
  .petAddForm {
    display: flex;
    width: 100%;
  }
`;

const PetCard = styled.div`
  width: 800px;
  height: 150px;
  display: flex;
  border: 1px solid red;
  justify-content: space-between;
  align-items: center;
`;

const PetImg = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
`;

const PetImgRe = styled.div`
  float: right;
  margin-top: 100px;
  margin-left: -80px;
  width: 5px;
  height: 5px;
  input {
    border-radius: 50%;
    border: solid 1px red;
    display: none;
  }
  .Gear {
    font-size: 20px;
  }
`;

const PetSetting = styled.div`
  display: flex;
  flex-direction: column;
`;
const PetAdd = styled.div`
  margin-top: 20px;
  width: 800px;
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: dotted 3px gray;
  .addPet {
    font-size: 30px;
  }
  cursor: pointer;
  &:hover {
    transform: translateY(-1px);
  }
`;

const Mypage = () => {
  const dispatch = useAppDispatch();
  const profile = useSelector((state: RootState) => state.mypage.profile);
  const [petData, setPetData] = useState<PetData>({
    petId: 0,
    name: '',
    birth: '',
    gender: true,
    neutralization: false,
    breed: 1,
    mbti: '',
    image: null,
  });
  const navigate = useNavigate();

  const [isOpenUsernameChangeModal, setOpenUsernameChangeModal] =
    useState<boolean>(false);
  const onClickUsernameChangeToggleModal = useCallback(() => {
    setOpenUsernameChangeModal(!isOpenUsernameChangeModal);
  }, [isOpenUsernameChangeModal]);
  const handleUsernameChangeClick = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.stopPropagation();
    onClickUsernameChangeToggleModal();
  };

  const handleModifyAddress = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    navigate('/users/test');
  };
  const [isOpenModifyPasswordModal, setOpenModifyPasswordModal] =
    useState<boolean>(false);
  const onClickPasswordChangeToggleModal = useCallback(() => {
    setOpenModifyPasswordModal(!isOpenModifyPasswordModal);
  }, [isOpenModifyPasswordModal]);
  const handleModifyPasswordClick = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.stopPropagation();
    onClickPasswordChangeToggleModal();
  };

  const [username, setUsername] = useState('');
  const [validId, setValidId] = useState('');
  const [password, setPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [newPasswordCheck, setNewPasswordCheck] = useState<string>('');
  const onChangeDisplay = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setUsername(e.target.value);
    },
    []
  );
  const onChangePassword = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setPassword(e.target.value);
    },
    []
  );
  const onChangeNewPassword = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setNewPassword(e.target.value);
    },
    []
  );
  const onChangeNewPasswordCheck = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setNewPasswordCheck(e.target.value);
    },
    []
  );
  const goId = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/check-username?username=${username}`
        )
        .then((response) => {
          // 아이디 확인
          console.log(response);
          setValidId(username);
          alert('사용가능한 닉네임입니다');
        })
        .catch((error: unknown | Error | AxiosError) => {
          console.log(error);
          if (isAxiosError(error)) {
            if (error.response) {
              const errorMessage: string = error.response.data.message;
              const status: number = error.response.status;

              // Show the error message as a pop-up
              alert(`${status}: ${errorMessage}`);
            }
          } else {
            // Handle other types of errors (e.g., network error)
            alert(error);
          }
        });
    },
    [username]
  );

  const onSubmitUsernameChange = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (validId === '') {
        alert('중복확인을 하지 않았습니다');
        return;
      } else if (validId !== username) {
        alert('중복확인한 아이디와 일치하지 않습니다');
        return;
      }
      console.log([username]);
      dispatch(fetchUsersname({ username, password })).then(
        (resultAction: any) => {
          const { success } = resultAction.payload;
          if (success === true) {
            alert('닉네임 변경완료');
          } else {
            alert('다시 시도해 주세요');
          }
        }
      );
      // .catch((err) => console.log(err.message));
    },
    [validId, username, password, dispatch]
  );
  const onSubmitPasswordChange = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (password === '') {
        alert('입력을 완료해주세요');
        return;
      } else if (newPassword !== newPasswordCheck) {
        alert('새비밀번호 확인이 틀렸습니다');
        return;
      }
      dispatch(fetchPassword({ password, newPassword, newPasswordCheck })).then(
        (resultAction: any) => {
          const { success } = resultAction.payload;
          if (success === true) {
            alert('닉네임 변경완료');
          } else {
            alert('다시 시도해 주세요');
          }
        }
      );
      // .catch((err) => console.log(err.message));
    },
    [password, newPassword, newPasswordCheck, dispatch]
  );
  const [isOpenAddPetModal, setOpenAddPetModal] = useState<boolean>(false);
  const handleAddPetClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
    onClickToggleAddPetModal();
  };
  const onClickToggleAddPetModal = useCallback(() => {
    setOpenAddPetModal(!isOpenAddPetModal);
  }, [isOpenAddPetModal]);

  const [isOpenModifyPetModal, setOpenModifyPetModal] =
    useState<boolean>(false);

  const handleModifyPetClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    onClickToggleModifyPetModal();
    const petId = parseInt(event.currentTarget.dataset.petid, 10);
    console.log(`${petId}아아ㅏ아아아아`);
    const petData = profile.pets.find((pet) => pet.id === petId);
    if (petData) {
      setPetData(petData);
    } // Set the pet data as the default values for the input fields
  };
  const onClickToggleModifyPetModal = useCallback(() => {
    setOpenModifyPetModal(!isOpenModifyPetModal);
  }, [isOpenModifyPetModal]);

  useEffect(() => {
    console.log(localStorage.getItem('userId'), 'qkerkwkerkw');
    dispatch(fetchUsers(Number(localStorage.getItem('userId'))));
  }, [dispatch]);
  const handleImageChange = (e: React.FormEvent<HTMLInputElement>) => {
    if (e.currentTarget.files !== null) {
      setPetData((prevData) => ({
        ...prevData,
        image: e.currentTarget?.files[0], // 이미지를 input에서 선택한 파일로 업데이트
      }));
      console.log(e.currentTarget.files[0]);
      console.log('이미지바뀜');
    }
    // valid: 꼭 한개 이상의 파일이 담겨있어야함: input value 값이 '' 빈문자열이 아니어야함.
  };
  const handleInputChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = event.target as HTMLInputElement; // 타입 지정

    let inputValue: string | boolean | File | null = value;

    if (type === 'checkbox') {
      inputValue = (event.target as HTMLInputElement).checked; // 타입 캐스팅
      if (name === 'gender' || name === 'neutralization') {
        inputValue = inputValue === true; // 서버에서 문자열로 반환되는 경우 'true'를 boolean 값으로 변환
      }
    }
    setPetData((prevData) => ({
      ...prevData,
      [name]: inputValue,
    }));
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.persist;
    const formData = new FormData();
    formData.append('image', petData.image || '');
    const requestData = {
      name: petData.name,
      birth: petData.birth,
      mbti: petData.mbti,
      gender: petData.gender,
      neutralization: petData.neutralization,
      breedId: Number(petData.breed),
    };
    const jsonBlob = new Blob([JSON.stringify(requestData)], {
      type: 'application/json',
    });
    formData.append('request', jsonBlob);

    // 악시오스로 수정
    axios
      .post(
        'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/pets/register',
        formData,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then((response) => {
        // 요청이 성공적으로 처리되었을 때 실행할 코드
        console.log(response.data);
        petData.petId = response.data.result.id;
        console.log(petData.petId);
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.error(error);
      });
  };

  const handleModifySubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.persist;
    const formData = new FormData();
    formData.append('image', petData.image || '');
    const requestData = {
      name: petData.name,
      birth: petData.birth,
      mbti: petData.mbti,
      gender: petData.gender,
      neutralization: petData.neutralization,
      breedId: Number(petData.breed),
    };
    const jsonBlob = new Blob([JSON.stringify(requestData)], {
      type: 'application/json',
    });
    formData.append('request', jsonBlob);
    // 서버로 formData 전송
    axios
      .patch(
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/pets/${petData.id}`,
        formData,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then((response) => {
        // 요청이 성공적으로 처리되었을 때 실행할 코드
        console.log(petData.petId);
        console.log(response.data);
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.log(petData.petId);
        console.log(petData);

        console.error(error);
      });
  };
  return (
    <Container>
      <UserContainer>
        <UserPart>
          <UserTitle>{profile.username}님의 페이지 입니다</UserTitle>
          <UserCard>
            <UserImg src={userProfileImg}></UserImg>
            <UserImgRe>
              <label htmlFor="userProfile">
                <BsFillGearFill className="Gear" />
              </label>
              <input
                type="file"
                name="image"
                id="userProfile"
                accept="image/*"
              />
            </UserImgRe>
            <UserName>{profile.username}</UserName>
          </UserCard>
          <UserPartButtons>
            <StyledButtonPink3D onClick={handleUsernameChangeClick}>
              닉네임 수정
            </StyledButtonPink3D>
            <StyledButtonPink3D onClick={handleModifyPasswordClick}>
              비밀 번호 변경
            </StyledButtonPink3D>
          </UserPartButtons>
          {isOpenUsernameChangeModal && (
            <UsernameChangeModal
              onClickUsernameChangeToggleModal={
                onClickUsernameChangeToggleModal
              }
            >
              <form onSubmit={onSubmitUsernameChange}>
                <InputUsername>
                  <div>닉네임 바꾸기</div>
                  <IdCheckForm>
                    <input
                      type="name"
                      id="signupusername"
                      placeholder="닉네임 만들기"
                      value={username}
                      onChange={onChangeDisplay}
                      required
                    ></input>
                    <StyledButtonPink3D onClick={goId}>
                      중복 확인 하기
                    </StyledButtonPink3D>
                  </IdCheckForm>
                </InputUsername>
                <InputPassword>
                  <div className="input-password-label">
                    <div>Password</div>
                  </div>
                  <input
                    type="password"
                    id="loginPassword"
                    placeholder="Password"
                    value={password}
                    onChange={onChangePassword}
                  />
                </InputPassword>
                <StyledButtonPink3D>변경하기</StyledButtonPink3D>
              </form>
            </UsernameChangeModal>
          )}
          {isOpenModifyPasswordModal && (
            <ModifyPasswordModal
              onClickPasswordChangeToggleModal={
                onClickPasswordChangeToggleModal
              }
            >
              <form onSubmit={onSubmitPasswordChange}>
                <InputPassword>
                  <div className="input-password-label">
                    <div>Password</div>
                  </div>
                  <input
                    type="password"
                    id="PostPassword"
                    placeholder="Password"
                    value={password}
                    onChange={onChangePassword}
                  />
                </InputPassword>
                <InputPassword>
                  <div className="input-password-label">
                    <div>새 Password</div>
                  </div>
                  <input
                    type="password"
                    id="newPassword"
                    placeholder="Password"
                    value={newPassword}
                    onChange={onChangeNewPassword}
                  />
                </InputPassword>
                <InputPassword>
                  <div className="input-password-label">
                    <div>새 Password 확인</div>
                  </div>
                  <input
                    type="password"
                    id="newPasswordCheck"
                    placeholder="Password"
                    value={newPasswordCheck}
                    onChange={onChangeNewPasswordCheck}
                  />
                </InputPassword>
                <StyledButtonPink3D>변경하기</StyledButtonPink3D>
              </form>
            </ModifyPasswordModal>
          )}
        </UserPart>
        <MapPart>
          <div>
            <div>{profile.address}</div>
            <StyledButtonPink3D onClick={handleModifyAddress}>
              변경
            </StyledButtonPink3D>
          </div>
          <Map />
        </MapPart>
      </UserContainer>
      <DogPart>
        <ul>
          {profile.pets.map((pet) => (
            <li key={pet.id}>
              <PetCard>
                <PetImg src={pet.imgUrl}></PetImg>
                <PetImgRe>
                  <label htmlFor="userProfile">
                    <BsFillGearFill className="Gear" />
                  </label>
                  <input
                    type="file"
                    name="image"
                    id="userProfile"
                    accept="image/*"
                  />
                </PetImgRe>
                <div>
                  <p>이름 : {pet.name}</p>
                  <p>강아지 mbti : {pet.mbti}</p>
                  <p>견종 : {pet.breed}</p>
                  <p>중성화 여부 : {pet.neutralization}</p>
                  <p>성별 : {pet.gender}</p>
                  <p>생년월일 : {pet.birth}</p>
                </div>
                <PetSetting>
                  <div>
                    <StyledButtonPink3D
                      data-petid={pet.id}
                      onClick={handleModifyPetClick}
                    >
                      변경
                    </StyledButtonPink3D>
                    <StyledButtonPink3D>삭제</StyledButtonPink3D>
                  </div>
                </PetSetting>
              </PetCard>
            </li>
          ))}
        </ul>
        {isOpenModifyPetModal && (
          <ModifyPetModal
            onClickToggleModifyPetModal={onClickToggleModifyPetModal}
          >
            <form
              onSubmit={handleModifySubmit}
              style={{
                width: '800px',
                height: '200px',
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
              }}
              encType="multipart/form-data"
            >
              <PetImg></PetImg>
              <PetImgRe>
                <label htmlFor="file">
                  <BsFillGearFill className="Gear" />
                </label>
                <input
                  type="file"
                  name="file"
                  id="file"
                  accept="image/*"
                  onChange={handleImageChange}
                />
              </PetImgRe>
              <AddInputContainer>
                <div>
                  <InputContainer>
                    <InputWrapper>
                      <InputLabel>이름 :</InputLabel>
                      <InputField
                        type="text"
                        name="name"
                        id="name"
                        maxLength={8}
                        value={petData.name}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>강아지 mbti :</InputLabel>
                      <InputField
                        type="text"
                        name="mbti"
                        id="mbti"
                        value={petData.mbti}
                        onChange={handleInputChange}
                        list="mbtiList"
                      />
                      <datalist id="mbtiList">
                        <option value="ISTJ" />
                        <option value="ISFJ" />
                        <option value="INFJ" />
                        <option value="INTJ" />
                        <option value="ISTP" />
                        <option value="ISFP" />
                        <option value="INFP" />
                        <option value="INTP" />
                        <option value="ESTP" />
                        <option value="ESFP" />
                        <option value="ENFP" />
                        <option value="ENTP" />
                        <option value="ESTJ" />
                        <option value="ESFJ" />
                        <option value="ENFJ" />
                        <option value="ENTJ" />
                      </datalist>
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>견종 :</InputLabel>
                      <InputField
                        type="text"
                        name="breed"
                        id="breed"
                        maxLength={8}
                        value={petData.breed}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                  </InputContainer>
                </div>
                <div>
                  <InputContainer>
                    <InputWrapper>
                      <InputLabel>중성화 여부 :</InputLabel>
                      <input
                        type="checkbox"
                        name="neutralization"
                        id="neutralization"
                        checked={petData.neutralization}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>성별 :</InputLabel>
                      <input
                        type="radio"
                        name="gender"
                        value="male"
                        id="male"
                        checked={petData.gender === true}
                        onChange={handleInputChange}
                      />
                      <label htmlFor="male">남자</label>
                      <input
                        type="radio"
                        name="gender"
                        value="female"
                        id="female"
                        checked={petData.gender === false}
                        onChange={handleInputChange}
                      />
                      <label htmlFor="female">여자</label>
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>생년월일 :</InputLabel>
                      <InputField
                        type="date"
                        name="birth"
                        id="birth"
                        value={petData.birth}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                  </InputContainer>
                </div>
              </AddInputContainer>

              <PetSetting>
                <div>
                  <StyledButtonPink3D type="submit">
                    등록하기
                  </StyledButtonPink3D>
                </div>
              </PetSetting>
            </form>
          </ModifyPetModal>
        )}
        {isOpenAddPetModal && (
          <AddPetModal onClickToggleAddPetModal={onClickToggleAddPetModal}>
            <form
              onSubmit={handleSubmit}
              style={{
                width: '800px',
                height: '200px',
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
              }}
              encType="multipart/form-data"
            >
              <PetImg></PetImg>
              <PetImgRe>
                <label htmlFor="file">
                  <BsFillGearFill className="Gear" />
                </label>
                <input
                  type="file"
                  name="file"
                  id="file"
                  accept="image/*"
                  // multiple
                  // value={petData.image}
                  onChange={handleImageChange}
                />
              </PetImgRe>
              <AddInputContainer>
                <div>
                  <InputContainer>
                    <InputWrapper>
                      <InputLabel>이름 :</InputLabel>
                      <InputField
                        type="text"
                        name="name"
                        id="name"
                        maxLength={8}
                        value={petData.name}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>강아지 mbti :</InputLabel>
                      <InputField
                        type="text"
                        name="mbti"
                        id="mbti"
                        value={petData.mbti}
                        onChange={handleInputChange}
                        list="mbtiList"
                      />
                      <datalist id="mbtiList">
                        <option value="ISTJ" />
                        <option value="ISFJ" />
                        <option value="INFJ" />
                        <option value="INTJ" />
                        <option value="ISTP" />
                        <option value="ISFP" />
                        <option value="INFP" />
                        <option value="INTP" />
                        <option value="ESTP" />
                        <option value="ESFP" />
                        <option value="ENFP" />
                        <option value="ENTP" />
                        <option value="ESTJ" />
                        <option value="ESFJ" />
                        <option value="ENFJ" />
                        <option value="ENTJ" />
                      </datalist>
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>견종 :</InputLabel>
                      <InputField
                        type="text"
                        name="breed"
                        id="breed"
                        maxLength={8}
                        value={petData.breed}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                  </InputContainer>
                </div>
                <div>
                  <InputContainer>
                    <InputWrapper>
                      <InputLabel>중성화 여부 :</InputLabel>
                      <input
                        type="checkbox"
                        name="neutralization"
                        id="neutralization"
                        checked={petData.neutralization}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>성별 :</InputLabel>
                      <input
                        type="radio"
                        name="gender"
                        value="male"
                        id="male"
                        checked={petData.gender === true}
                        onChange={handleInputChange}
                      />
                      <label htmlFor="male">남자</label>
                      <input
                        type="radio"
                        name="gender"
                        value="female"
                        id="female"
                        checked={petData.gender === false}
                        onChange={handleInputChange}
                      />
                      <label htmlFor="female">여자</label>
                    </InputWrapper>
                    <InputWrapper>
                      <InputLabel>생년월일 :</InputLabel>
                      <InputField
                        type="date"
                        name="birth"
                        id="birth"
                        value={petData.birth}
                        onChange={handleInputChange}
                      />
                    </InputWrapper>
                  </InputContainer>
                </div>
              </AddInputContainer>

              <PetSetting>
                <div>
                  <StyledButtonPink3D type="submit">
                    등록하기
                  </StyledButtonPink3D>
                </div>
              </PetSetting>
            </form>
          </AddPetModal>
        )}
        <PetAdd onClick={handleAddPetClick}>
          <BsPlusCircleDotted className="addPet" />
        </PetAdd>
      </DogPart>
    </Container>
  );
};

export default Mypage;

const InputContainer = styled.div`
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
`;

const InputWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  #mbtiList {
    width: 200px;
  }
`;

const InputLabel = styled.label`
  width: 70px;
  text-align: right;
  margin-right: 10px;
`;

const InputField = styled.input`
  padding: 5px;
`;

const AddInputContainer = styled.div`
  display: flex;
`;
