import { styled } from 'styled-components';
import { useEffect, useCallback, useState } from 'react';
import { useDaumPostcodePopup } from 'react-daum-postcode';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { actionS } from './signUpSlice';
// import { useDispatch } from 'react-redux';
import { useAppDispatch } from '../../store/store';
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f1f2f3;
  font-size: 0.8rem;
  form {
    width: 30em;
  }
  #map {
    height: 600px;
  }
`;
const Contents = styled.div`
  display: flex;
  flex-direction: column;
  width: 30em;
`;
const SignUpForm = styled.div`
  display: flex;
  flex-direction: column;
  background-color: white;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.05), 0 20px 48px rgba(0, 0, 0, 0.05),
    0 1px 4px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 24px;
  margin-bottom: 24px;
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
`;

const InputEmail = styled.div`
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
`;

const InputPW = styled.div`
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
const InputAddress = styled.div`
  display: flex;
  flex-direction: column;
  margin: 6px 0 6px;

  > div {
    text-align: left;
    margin: 2px 0 2px;
    padding: 0 2px;
    font-size: 1rem;
    font-weight: 800;
  }

  > input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
`;
const EmailAuthForm = styled.div`
  display: flex;
  justify-content: space-between;
  align-itme: center;
`;
const IdCheckForm = styled.div`
  display: flex;
  justify-content: space-between;
  align-itme: center;
`;
const SignUp = () => {
  const [email, setEmail] = useState('');
  const [emailAuth, setEmailAuth] = useState('');
  const [password, setPassword] = useState('');
  const [username, setUsername] = useState('');
  const [address, setAddress] = useState('');
  const [validId, setValidId] = useState('');
  const open = useDaumPostcodePopup(
    'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
  );
  console.log(1);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const onChangeEamil = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setEmail(e.target.value);
    },
    []
  );
  const onChangeEamilAuth = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setEmailAuth(e.target.value);
    },
    []
  );
  const onChangePassword = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setPassword(e.target.value);
    },
    []
  );
  const onChangeDisplay = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setUsername(e.target.value);
    },
    []
  );
  // const goEmail =
  const onSubmitJoin = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (validId !== username) {
        alert('중복확인한 아이디와 일치하지 않습니다');
        return;
      }
      console.log([username, email, emailAuth, password]);
      dispatch(actionS({ username, email, emailAuth, password })).then(
        (resultAction: any) => {
          const { success } = resultAction.payload;
          if (success === true) {
            alert('회원가입 성공');
            navigate('/users/sign-in');
          } else {
            alert('비밀번호 아이디 이메일 인증을 모두 수행하쇼');
          }
        }
      );
      // .catch((err) => console.log(err.message));
    },
    [validId, username, email, emailAuth, password, dispatch, navigate]
  );
  const goEmail = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://ec2-52-79-240-48.ap-northeast-2.compute.amazonaws.com:8080/api/users/emails?email=${email}`
        )
        .then((response) => {
          // 이메일 인증에 대한 로직을 추가해주세요
          console.log(response);
        })
        .catch((error) => {
          console.error(error);
          alert('다시 요청해주세요');
        });
    },
    [email]
  );
  const goId = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://ec2-52-79-240-48.ap-northeast-2.compute.amazonaws.com:8080/api/users/username?username=${username}`
        )
        .then((response) => {
          // 아이디 확인
          console.log(response);
          setValidId(username);
        })
        .catch((error) => {
          console.error(error);
        });
    },
    [username]
  );
  const handleComplete = (data: any) => {
    console.log(1);

    console.log(data);
    console.log(data.roadAddress); // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
    setAddress(data.address);
    //여기서 goaddress할필요가 없음 유즈이펙트땜
  };
  const handleClick = () => {
    open({ onComplete: handleComplete });
    console.log(0);
  };
  const new_script = (src: string) => {
    return new Promise<void>((resolve, reject) => {
      const script = document.createElement('script');
      script.src = src;
      script.addEventListener('load', () => {
        resolve();
      });
      script.addEventListener('error', (e) => {
        reject(e);
      });
      document.head.appendChild(script);
    });
  };
  useEffect(() => {
    const my_script = new_script(
      'https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=2e9c72e22b8b9402a65bbc568e1d75b1'
    );
    //스크립트 읽기 완료 후 카카오맵 설정
    my_script.then(() => {
      console.log('script loaded!!!');
      const kakao = (window as any)['kakao'];
      kakao.maps.load(() => {
        const mapContainer = document.getElementById('map');
        const options = {
          center: new kakao.maps.LatLng(37.56000302825312, 126.97540593203321), //좌표설정
          level: 3,
        };
        const map = new kakao.maps.Map(mapContainer, options); //맵생성
        //마커설정
        const markerPosition = new kakao.maps.LatLng(
          37.56000302825312,
          126.97540593203321
        );
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(map);
      });
    });
  }, []);

  /// mapContainer = container
  //map == map
  const goAddress = useCallback(() => {
    if (address === '') {
      return;
    }
    console.log('들어왔다');
    console.log(address);
    console.log(2);
    const kakao = (window as any)['kakao'];
    kakao.maps.load(() => {
      const mapContainer = document.getElementById('map');
      const options = {
        center: new kakao.maps.LatLng(37.55, 126.97540593203321), //좌표설정
        level: 3,
      };
      const map = new kakao.maps.Map(mapContainer, options); //맵생성
      //마커설정
      const geocoder = new kakao.maps.services.Geocoder();
      // 주소로 좌표를 검색합니다..
      //현재 새주소만 인정되는 문제가 있음
      geocoder.addressSearch(address, function (result: any[], status: any) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 결과값으로 받은 위치를 마커로 표시합니다
          const marker = new kakao.maps.Marker({
            map: map,
            position: coords,
          });

          // 인포윈도우로 장소에 대한 설명을 표시합니다
          const infowindow = new kakao.maps.InfoWindow({
            content:
              '<div style="width:150px;color:red;text-align:center;padding:6px 0;">내가 썼지롱</div>',
          });
          infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
        }
      });
    });
  }, [address]);
  useEffect(() => {
    goAddress();
  }, [goAddress]);

  return (
    <Container>
      <Contents>
        <form onSubmit={onSubmitJoin}>
          <SignUpForm>
            <InputUsername>
              <div>닉네임</div>
              <IdCheckForm>
                <input
                  type="name"
                  id="signupusername"
                  placeholder="닉네임 만들기"
                  value={username}
                  onChange={onChangeDisplay}
                ></input>
                <button onClick={goId}>중복 확인 발급</button>
              </IdCheckForm>
            </InputUsername>
            <InputEmail>
              <div>E-mail</div>
              <input
                type="email"
                id="loginEamil"
                placeholder="이메일"
                value={email}
                onChange={onChangeEamil}
              ></input>
              {/* <div onClick={goEmail}>이메일 인증</div> */}
            </InputEmail>
            <InputEmail>
              <div>인증번호</div>
              <EmailAuthForm>
                <input
                  type="text"
                  id="emailAuth"
                  placeholder="인증번호 입력"
                  value={emailAuth}
                  onChange={onChangeEamilAuth}
                ></input>
                <button onClick={goEmail}>인증번호 발급</button>
              </EmailAuthForm>

              {/* <div onClick={goEmail}>보내기</div> */}
            </InputEmail>
            <InputPW>
              <div>비밀번호</div>
              <input
                type="password"
                id="loginPassword"
                placeholder="비밀번호"
                value={password}
                onChange={onChangePassword}
              ></input>
            </InputPW>
            <InputAddress>
              {/* <div value={address} onChange={goAddress}>
                {address}aa
              </div> */}
              <input
                type="address"
                id="address"
                placeholder="숭례문 도로명"
                value={address}
                readOnly
              ></input>
              <button type="button" onClick={handleClick}>
                Open
              </button>
            </InputAddress>

            <div>
              <button>회원가입</button>
            </div>
          </SignUpForm>
        </form>
        <div id="map" className="map/"></div>
      </Contents>
    </Container>
  );
};

export default SignUp;
