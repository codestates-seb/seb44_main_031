import { useCallback, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState, useAppDispatch } from '../../store/store';
import { fetchUsersname, fetchUsers, fetchPassword, fetchMypostList ,fetchPetWalkList} from './myPageSlice';
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
import UserWithdrawModal from './UserWithdraw';
import { PiDogDuotone } from "react-icons/pi";
import MypostModal from './MyPost';
import { stringToLocaleString } from '../../utils/date-utils';
import MyPetWalkModal from './MyPetWalk';
interface PetData {
  petId: number;
  name: string;
  birth: string;
  gender: boolean;
  neutralization: boolean;
  breedId: number;
  mbti: string;
  image : File | null;
  imgUrl:string;
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  background-color: white;
  font-size: 0.8rem;
   input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
  ${StyledButtonPink3D}{
    width: 100px;
    padding: 2px;
  }
`;
const UserContainer = styled.div`
margin-top: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-size: 0.8rem;
  margin-bottom: 40px;  
`;
const UserPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 400px;
  height: 300px;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-size: 0.8rem;
  margin-top: 10px;
`;
const UserTitle = styled.div`
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
`;
const UserCard = styled.div`
  text-align: center;  
`;
const UserPartButtons = styled.div`
  display: flex;
  width: 400px;
  justify-content: center;
  gap:3px;
  margin: 10px;
`;
const UserImg = styled.img`
  width: 150px;
  height: 150px;
  border-radius: 50%;
margin-bottom: 25px;
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
  justify-content: space-between;
  align-items: center;
  background-color: white;
  font-size: 0.8rem;
`;

const DogPart = styled.div`
  display: flex;
  flex-direction: column;
  width: 800px;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-size: 0.8rem;

  .petAddForm {
    display: flex;
    width: 100%;
  }

  .petgap {
    display: flex;
    flex-direction: column;
    gap: 20px;
   
  }
  .petwalkList{
  cursor: pointer;
  display:flex;
  flex-direction: column;
  justify-content: space-around;
  height:100px;
  padding:17px;
  border-radius: 5px;
  text-align: left;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); 
  transition: background-color 0.2s ease; 
  &:hover {
    background-color: #dcdcdc;
  }
  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px var(--pink-300);
  }
}
`;
const SmallButton = styled.button`
  border: none;
  border-radius: 5px;
  background-color: var(--pink-400);
  color: white;
  font-size: 12px;
  padding: 8px 12px;
  text-align: center;
  text-transform: uppercase; 
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); 
  transition: background-color 0.2s ease; 
  cursor: pointer;


  &:hover {
    background-color: var(--pink-300);
    
  }


  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px var(--pink-300);
  }



`;
const MapInfo = styled.div`
  height:60px;
  display:flex;
  justify-content: space-between;
  flex-direction: column;
  align-items: center;
`

const PetCard = styled.div`
  width: 720px;
  height: 200px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-radius: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
`;

const PetImg = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
`;

const PetImgRe = styled.div`
  form{
    width: 200px;
    height: 200px;
  }
  input {
    display: none;
  }
  .Dog {
    font-size: 20px;
  }
  .show{
    margin-top:5px;
    color: gray;
  }
`;
const PetProfile = styled.div`
width: 200px;
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
.show{
    margin-top:15px;
    color:gray;
  }
`
const PetSetting = styled.div`
  display: flex;
  flex-direction: column;
  gap:3px;
  .petsetting{
    margin-left: 60px;
    margin-bottom:120px;
    ${SmallButton}{
      margin:2px;
    }
  }
`;
const PetAdd = styled.div`
  margin-top: 20px;
  width: 600px;
  height: 200px;
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
const EmailAuthForm = styled.div`
  display: flex;  
  justify-content: space-between;
  align-items: center;
  margin:10px;
`;
 const ListCard = styled.div`
  display:flex;
  flex-direction:column;
  gap: 3px;
  div{
    border:1px solid gray;
    border-radius: 5px;
    padding:13px;
    font-size:18px;
    width: 500px;
    cursor: pointer;

  &:hover {
    background-color: var(--pink-300);
    
  }


  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px var(--pink-300);
  }
  }
 `
 
const Mypage = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const mypostList = useSelector((state: RootState) => state.mypage.mypostList);
  const mypetList = useSelector((state: RootState) => state.mypage.mypetWalk);
  const profile = useSelector((state: RootState) => state.mypage.profile);
  const [petData, setPetData] = useState<PetData>({
    petId: 0,
    name: '',
    birth: '',
    gender: true,
    neutralization: false,
    breedId: 2,
    mbti: '',
    image: null,
    imgUrl:'',
    });
  
    const sliceContentLengthEndWithDots = (
      content: string,
      desiredLength: number
    ) => {
      if (content.length <= 66) return content;
    
      return content.slice(0, desiredLength - 3) + '...';
    };
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

  const [isOpenMypostModal, setOpenMypostModal] =
    useState<boolean>(false);
  const onClickToggleMypostModal = useCallback(() => {
    setOpenMypostModal(!isOpenMypostModal);
  }, [isOpenMypostModal]);
  const handleMypostClick = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.stopPropagation();
    onClickToggleMypostModal();
  };

  const handleModifyAddress = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    navigate('/users/mapChange');
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
  const [email, setEmail] = useState('');
  const [emailCheck, setEmailCheck] = useState<boolean>(false);
  const onChangeEamil = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setEmail(e.target.value);
    },
    []
  );
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
        .then(() => {      
          setValidId(username);
          alert('사용가능한 닉네임입니다');
        })
        .catch((error: unknown | Error | AxiosError) => {
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
  const goEmail = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/delete/send-verification-email?email=${email}`,
          {
            headers: {
              Authorization: localStorage.getItem('accessToken'),
            },
          }
        )
        .then(() => {
          // 이메일 인증에 대한 로직을 추가해주세요
          setEmailCheck(true);
          alert('인증을 완료 하셨습니다');
        })
        .catch((error) => {
          console.log(error); 
          alert('다시 요청해주세요');
        });
    },
    [email]
  );
  const onSubmitUserWithdraw= useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();   
      if (emailCheck === false) {
        alert('이메일 인증을 진행해주세요');
        return;
      }
      axios
        .delete(
          `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/delete`,
          {
            headers: {
              Authorization: localStorage.getItem('accessToken'),
            },
          }
        )
        .then(() => {
          // 이메일 인증에 대한 로직을 추가해주세요
          alert('탈퇴 되었습니다');
          setEmailCheck(false);
          localStorage.removeItem('accessToken');
          localStorage.removeItem('userId');
          navigate('/');
        })
        .catch((error) => {
          alert('참여중인 게시글이 있으므로 탈퇴가 불가합니다');
          console.log(error)
        });
    },
    [validId, emailCheck,username, password, dispatch]
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
      
      dispatch(fetchUsersname({ username, password })).then(
        () => {
            alert('닉네임 변경완료');
            window.location.reload();
        }
      );
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
        () => {
    
            alert('비밀번호 변경완료');
          
          window.location.reload();
        }
      );
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

  const [isOpenUserWithdrawModal, setOpenUserWithdrawModal] =
    useState<boolean>(false);
  const onClickUserWithdrawModal = useCallback(() => {
    setOpenUserWithdrawModal(!isOpenUserWithdrawModal);
    }, [isOpenUserWithdrawModal]);
    const handleUserWithdrawClick = (
      event: React.MouseEvent<HTMLButtonElement>
    ) => {
      event.stopPropagation();
      onClickUserWithdrawModal();
    };
  const handleModifyPetClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    onClickToggleModifyPetModal();
    const petId = parseInt(event.currentTarget.dataset.petid, 10);
    const petData = profile.pets.find((pet) => pet.id === petId);
    if (petData) {
      //@ts-ignore
      setPetData(petData);
    } // Set the pet data as the default values for the input fields
  };
  const [isOpenModifyPetModal, setOpenModifyPetModal] =
  useState<boolean>(false);
  const onClickToggleModifyPetModal = useCallback(() => {
    setOpenModifyPetModal(!isOpenModifyPetModal);
  }, [isOpenModifyPetModal]);
  
  
  const [isOpenPetWalkModal, setOpenPetWalkModal] =
    useState<boolean>(false);
  const onClickToggleMypetWalkModal = useCallback(() => {
    setOpenPetWalkModal(!isOpenPetWalkModal);
    }, [isOpenPetWalkModal]);
  const handlePetWalkClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    onClickToggleMypetWalkModal();
    const petId = event.currentTarget.dataset.petid; // Get the petId from the clicked button
    if (!petId) {
      return;
    }
    dispatch(fetchPetWalkList(petId));
    };

  useEffect(() => {
    
    dispatch(fetchUsers(Number(localStorage.getItem('userId'))));
    dispatch(fetchMypostList())
  }, [dispatch]);
  const handleImageChange = (e: React.FormEvent<HTMLInputElement>) => {
    const files = e.currentTarget.files;
    if (files !== null && files.length > 0) {
      setPetData((prevData) => {
        return { ...prevData, image: files[0] };
      });
    }

  };
  const handlePetImgChange = (e: React.FormEvent<HTMLInputElement>) => {
    const files = e.currentTarget.files;
    if (files !== null && files.length > 0) {
      setPetData((prevData) => {
        
        return { ...prevData, image: files[0] };
      });
      
    }
  };
  const handlePetImageSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.persist();
    const petId = event.currentTarget.getAttribute('data-petid'); // 수정된 부분
    const formData = new FormData();
    formData.append('image', petData.image || '');    
    axios
      .patch(
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/pets/image/${petId}`,
        formData,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error(error);
      });
  };
  const handleUserImgChange = (e: React.FormEvent<HTMLInputElement>) => {
    const files = e.currentTarget.files;
    if (files !== null && files.length > 0) {
      setPetData((prevData) => {
        return { ...prevData, image: files[0] };
      });
    }

  };
  const handleUserImageSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.persist();
    const formData = new FormData();
    formData.append('image', petData.image || '');    
    axios
      .patch(
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/users/image`,
        formData,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then(() => {
        
      window.location.reload();

      })
      .catch((error) => {
        console.error(error);
        alert('이미지가 너무크거나 형식에맞지 않습니다 jpg,png,jpeg')
      });
  };
  const handleInputChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = event.target as HTMLInputElement; // 타입 지정

    let inputValue: string | boolean | File | null = value;

    if (type === 'checkbox') {
      inputValue = (event.target as HTMLInputElement).checked; // 타입 캐스팅

      if (name === 'neutralization') {
        inputValue = inputValue === true; // 서버에서 문자열로 반환되는 경우 'true'를 boolean 값으로 변환
      }
    } else if (type === 'radio') {
      // 선택된 라디오 버튼 값으로 변환
      if (value === 'male') {
        inputValue = true; // 서버에서 문자열로 반환되는 경우 'true'를 boolean 값으로 변환
      } else if (value === 'female') {
        inputValue = false;
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
      breedId: Number(petData.breedId),

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
    
        petData.petId = response.data.result.id;

       
        setOpenAddPetModal(false);
        window.location.reload();
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.error(error);
      });
  };
  const handleDeletePetClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const petId = event.currentTarget.dataset.petid; // Get the petId from the clicked button
    if (!petId) {
      return;
    }
    axios
      .delete(
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/pets/${petId}`,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then(() => {
        // 요청이 성공적으로 처리되었을 때 실행할 코드
        window.location.reload();
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.log(error);
        alert('산책계획이거나 산책중인 강아지라 삭제할수 없습니다');
      });
  };

  const handleModifySubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.persist;
    // 서버로 formData 전송
    let modifiedBreedId;
  if (petData.breedId === null || petData.breedId === undefined || isNaN(petData.breedId)) {
    modifiedBreedId = 1;
  } else {
    modifiedBreedId = Number(petData.breedId);
  }
    axios
      .patch(
        //@ts-ignore
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/pets/information/${petData.id}`,
        {
          name: petData.name,
          birth: petData.birth,
          mbti: petData.mbti,
          gender: petData.gender,
          neutralization: petData.neutralization,
          breedId: modifiedBreedId,
        },
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <Container>
      {!localStorage.getItem('accessToken') && (
        <div style={{width: '100%', height:'800px',display:'flex',justifyContent:'center',alignItems:'center',}}>
          <h1>로그인 하고오개</h1>
          <img src="/assets/Profile.png" alt="asd" style={{width: '200px'} }/>
        </div>
      )}
      <UserContainer>
        <UserPart>
          <UserTitle>{profile.username}님의 페이지 입니다</UserTitle>
          <UserCard>
            <UserImg src={profile.imgUrl}></UserImg>
            <UserImgRe>
            <form     
              onSubmit={handleUserImageSubmit}
              style={{
                width: '80px',
                height: '20px',
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
              }}
              encType="multipart/form-data"                     
            > 
              <label htmlFor="userProfile" style={{cursor:'pointer'}}>
                <BsFillGearFill className="Gear" />
              </label>
              <input
                type="file"
                name="image"
                id="userProfile"
                accept="image/*"
                onChange={handleUserImgChange}
              />
              <SmallButton>적용</SmallButton>
            </form>
            </UserImgRe>
            <UserName>{profile.username}</UserName>
          </UserCard>
          <UserPartButtons>
            <SmallButton onClick={handleMypostClick}>
              내 게시물 보기
            </SmallButton>
            <SmallButton onClick={handleUsernameChangeClick}>
              닉네임 수정
            </SmallButton>
            <SmallButton onClick={handleModifyPasswordClick}>
              비밀 번호 변경
            </SmallButton>
            <SmallButton onClick={handleUserWithdrawClick}>
              회원탈퇴
            </SmallButton>
          </UserPartButtons>
          {isOpenMypostModal && (
        <MypostModal
        onClickToggleMypostModal={
          onClickToggleMypostModal
        }>
          <h1>내 게시글보기</h1>
          <ListCard >
            {mypostList.map((post) => (
              <div key={post.articleId} onClick={()=>{navigate(`/walk-mate/${post.articleId}`)}}>
                <p>{sliceContentLengthEndWithDots(post.title,50)}</p>
                <br></br>
                <p>시작시간:{stringToLocaleString(post.createdAt)}</p> 
                <p>종료예정시간{stringToLocaleString(post.startDate)}</p>
              </div>
            ))}
          </ListCard>
        </MypostModal>
      )}
          {isOpenUsernameChangeModal && (
            <UsernameChangeModal
              onClickUsernameChangeToggleModal={
                onClickUsernameChangeToggleModal
              }
            >
              <form onSubmit={onSubmitUsernameChange}>
                <InputUsername>
                  <h2>닉네임 바꾸기</h2>
                  <IdCheckForm>
                  <div className="input-password-label">
                    <div>새 Username</div>
                  </div>
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
          {isOpenUserWithdrawModal && (
            <UserWithdrawModal
            onClickUserWithdrawModal={
              onClickUserWithdrawModal
              }
            >
              <form onSubmit={onSubmitUserWithdraw}>
                <h2>회원 탈퇴하기</h2>
              <EmailAuthForm>
                <input
                  type="email"
                  id="loginEamil"
                  placeholder="이메일"
                  value={email}
                  onChange={onChangeEamil}
                  required
                ></input>
                <StyledButtonPink3D onClick={goEmail}>
                  이메일 인증
                </StyledButtonPink3D>
              </EmailAuthForm>
                <StyledButtonPink3D>탈퇴하기</StyledButtonPink3D>
              </form>
            </UserWithdrawModal>
          )}
        </UserPart>
        <MapPart>
        <Map />
          <MapInfo>
            <h2>{`${profile.address}`.split(' ').slice(0, 3).join(' ')}</h2>
            <SmallButton onClick={handleModifyAddress}>
              주소 변경하기
            </SmallButton>
          </MapInfo>
          
        </MapPart>
      </UserContainer>
      <DogPart>
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
            >
              <PetProfile>
              
                  <PetImg src={petData.imgUrl}></PetImg>
                </PetProfile>
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

                      <select
                        name="breedId"
                        id="breedId"
                        value={petData.breedId}
                        onChange={handleInputChange}
                      >
                        <option value="1">토이 푸들</option>
                        <option value="2">미니어쳐 푸들</option>
                        <option value="3">스탠다드 푸들</option>
                        <option value="4">말티즈</option>
                        <option value="5">골든 리트리버</option>
                        <option value="6">시츄</option>
                        <option value="7">시바 이누</option>
                        <option value="8">포메라니안</option>
                        <option value="9">웰시코기</option>
                        <option value="10">비글</option>
                        <option value="11">사모예드</option>
                        <option value="12">닥스훈트</option>
                        <option value="13">슈나우저</option>
                        <option value="14">보더 콜리</option>
                        <option value="15">허스키</option>
                        <option value="16">코카 스파니엘</option>
                        <option value="17">요크셔 테리어</option>
                        <option value="18">그레이하운드</option>
                        <option value="19">스피츠</option>
                        <option value="20">치와와</option>
                        <option value="21">믹스견</option>
                        <option value="22">골든 리트리버</option>
                        <option value="23">진돗개</option>
                      </select>

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
              <div className='profile'>
              <PetImg></PetImg>
              <PetImgRe>
                <label htmlFor="file" style={{cursor:'pointer'}}>
                  <PiDogDuotone className="Gear" />
                  <p className='show'>"강아지를 눌러 프로필 선택을 하셔야 합니다"</p>

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
              </div>
                       
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

                      <select
                        name="breedId"
                        id="breedId"
                        value={petData.breedId}
                        onChange={handleInputChange}
                      >
                        <option value="1">토이 푸들</option>
                        <option value="2">미니어쳐 푸들</option>
                        <option value="3">스탠다드 푸들</option>
                        <option value="4">말티즈</option>
                        <option value="5">골든 리트리버</option>
                        <option value="6">시츄</option>
                        <option value="7">시바 이누</option>
                        <option value="8">포메라니안</option>
                        <option value="9">웰시코기</option>
                        <option value="10">비글</option>
                        <option value="11">사모예드</option>
                        <option value="12">닥스훈트</option>
                        <option value="13">슈나우저</option>
                        <option value="14">보더 콜리</option>
                        <option value="15">허스키</option>
                        <option value="16">코카 스파니엘</option>
                        <option value="17">요크셔 테리어</option>
                        <option value="18">그레이하운드</option>
                        <option value="19">스피츠</option>
                        <option value="20">치와와</option>
                        <option value="21">믹스견</option>
                        <option value="22">골든 리트리버</option>
                        <option value="23">진돗개</option>
                      </select>
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
        {isOpenPetWalkModal &&(
          <MyPetWalkModal onClickToggleMypetWalkModal={onClickToggleMypetWalkModal}>
            <h2>참가한 산책모임</h2>
            <div className='petwalkList' onClick={()=>{navigate(`/walk-mate/${mypetList.articleId}`)}}>
              <p style={{fontSize:"15px"}}>{sliceContentLengthEndWithDots(mypetList.title,20)}</p>
              <p>시작시간:{stringToLocaleString(mypetList.createdAt)}</p> 
              <p>종료예정시간{stringToLocaleString(mypetList.startDate)}</p>
            </div>            
          </MyPetWalkModal>
        )}
        <ul className='petgap'>
        
          {profile.pets.map((pet) => (
            <li key={pet.id}>
              <PetCard>
                <PetProfile>
                  <PetImg src={pet.imgUrl}></PetImg>
                  <PetImgRe>
                    <form
                      onSubmit={handlePetImageSubmit}
                      style={{
                        width: '150px',
                        height: '20px',
                        display: 'flex',
                        justifyContent: 'space-around',
                        alignItems: 'center',
                      }}
                      encType="multipart/form-data"
                      data-petid={pet.id}
                    >                    
                      <label htmlFor="dogProfile" style={{cursor:'pointer'}}>                      
                        <PiDogDuotone className="Dog" />프로필선택                    
                      </label>
                      <input
                        type="file"
                        name="image"
                        id="dogProfile"
                        accept="image/*"
                        onChange={handlePetImgChange}
                      />
                      <SmallButton type='submit'> 적용 </SmallButton>
                    </form>
                   
                  </PetImgRe>
                  <p className='show'>"프로필 선택 후에 "선택 적용"을 눌러야 프로필 사진 변경이 적용됩니다"</p>
                </PetProfile>
                
                <div>
                  <p>이름 : {pet.name}</p>
                  <p>강아지 mbti : {pet.mbti}</p>
                  <p>견종 : {pet.breedName}</p>
                  <p>중성화 여부 : {pet.neutralization ? ' O ' : ' X '}</p>
                  <p>성별 : {pet.gender ? '♀' : '♂'}</p>
                  <p>생년월일 : {pet.birth}</p>
                </div>
                <PetSetting>
                  <div className='petsetting'>
                    <SmallButton
                      data-petid={pet.id}
                      onClick={handleModifyPetClick}
                    >
                      변경
                    </SmallButton>
                    <SmallButton
                      data-petid={pet.id}
                      onClick={handleDeletePetClick}
                    >
                      삭제
                    </SmallButton>
                    <SmallButton
                     data-petid={pet.id}
                     onClick={handlePetWalkClick}
                     >
                      산책
                    </SmallButton>
                  </div>
                </PetSetting>
                
              </PetCard>
            </li>
          ))}
        </ul>
       
        
        <PetAdd onClick={handleAddPetClick}>
          <BsPlusCircleDotted className="addPet" />
          <h3>강아지 등록하기</h3>
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