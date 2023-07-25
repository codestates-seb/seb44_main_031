import { styled } from 'styled-components';
import { useEffect, useCallback, useState } from 'react';
import { useDaumPostcodePopup } from 'react-daum-postcode';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { actionS } from './signUpSlice';
// import { useDispatch } from 'react-redux';
import { useAppDispatch } from '../../store/store';

const ec2URL = 'ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080';

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
const Logo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
  #logo {
    width: 32px;
    height: 37px;
  }
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
  ${StyledButtonPink3D} {
    width: 120px;
    height: 30px;
    padding:3px;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .submit{
    display:flex;
    justify-content: center;
   margin-top: 20px;
  }
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
  margin: 6px 0 6px;
  justify-content: space-between;
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
  align-items: center;
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
  > input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
`;
const SignUp: React.FC = () => {
  const [email, setEmail] = useState('');
  const [emailAuth, setEmailAuth] = useState('');
  const [password, setPassword] = useState('');
  const [username, setUsername] = useState('');
  const [address, setAddress] = useState('');
  const [validId, setValidId] = useState('');
  const [validCode, setValidCode] = useState('');
  const [latitude, setLatitude] = useState(0);
  const [longitude, setLongitude] = useState(0);
  const [usernameError, setUsernameError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const open = useDaumPostcodePopup(
    'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
  );
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
      setPasswordError(null);

      // Password validation
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{10,16}$/;
      if (!passwordRegex.test(password)) {
        setPasswordError(
          '비밀번호는 영문, 숫자, 특수문자(!@#$%^&*)를 포함하여 10~16글자로 입력해주세요.'
        );
      }else if(passwordRegex.test(password)===true){
        setPasswordError(
          '유효한 비밀번호입니다'
        );
      }
    },
    [password]
  );
  const onChangeDisplay = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setUsername(e.target.value);
      setUsernameError(null);

      // Username validation
      const usernameRegex = /^[a-zA-Z0-9가-힣]{2,8}$/;
      if (!usernameRegex.test(username)) {
        setUsernameError(
          '닉네임은 2~8글자의 한글, 영어, 숫자로만 입력해주세요.'
        );
      }else if(usernameRegex.test(username)===true){
        setUsernameError(
          '유효한 닉네임입니다.'
        );
      }
    },
    [username]
  );
  //배포후 에러 처리 해보기
  const onSubmitJoin = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (validId === '') {
        alert('중복확인을 하지 않았습니다');
        return;
      } else if (validId !== username) {
        alert('중복확인한 아이디와 일치하지 않습니다');
        return;
      } else if (validCode !== emailAuth) {
        alert(`인증번호 인증을 완료하지 않았습니다.`);
      } else if (address === '') {
        alert('서비스 이용 주소를 추가 하세요');
      }
      dispatch(
         actionS({ username, email, password,emailAuth, latitude, longitude, address })
      ).then((response) => {     
          if (response.payload===true) {
            console.log(response.payload)
            alert('회원가입 성공');
            navigate('/users/sign-in');
          }else{
            alert('회원가입을 다시시도해주세요.')
          }     
      });
    },
    [
      validId,
      username,
      validCode,
      emailAuth,
      address,
      email,
      password,
      latitude,
      longitude,
      dispatch,
      navigate,
    ]
  );
  //배포후 에러 처리 해보기
  const goEmail = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://${ec2URL}/auth/sign-up/send-verification-email?email=${email}`
        )
        .then(() => {
          // 이메일 인증에 대한 로직을 추가해주세요
          alert('요청을 보냈습니다');
        })
        .catch((error) => {
          console.error(error);
          alert('다시 요청해주세요');
        });
    },
    [email]
  );
  const checkEmail = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(
          `http://${ec2URL}/auth/verify-email?code=${emailAuth}&email=${email}`
        )
        .then(() => {
          // 이메일 인증에 대한 로직을 추가해주세요
          setValidCode(emailAuth);
          alert('인증에 성공하셨습니다');
        })
        .catch((error) => {
          console.error(error);
          alert('인증번호가 틀렸습니다.');
        });
    },
    [email, emailAuth]
  );
  //배포후 에러 처리 해보기
  const goId = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      axios
        .get(`http://${ec2URL}/auth/check-username?username=${username}`)
        .then(() => {
          // 아이디 확인
          setValidId(username);
          alert('사용 가능한 닉네임 입니다');
        })
        .catch((error) => {
          console.error(error);
        });
    },
    [username]
  );
  //배포후 에러 처리 해보기
  const handleComplete = (data: any) => {
    setAddress(data.address);
  };
  const handleClick = () => {
    open({ onComplete: handleComplete });
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
    });
  }, []);

  /// mapContainer = container
  //map == map
  const goAddress = useCallback(() => {
    if (address === '') {
      return;
    }
    const kakao = (window as any)['kakao'];
    kakao.maps.load(() => {
      const geocoder = new kakao.maps.services.Geocoder();
      // 주소로 좌표를 검색합니다..
      //현재 새주소만 인정되는 문제가 있음
      geocoder.addressSearch(address, function (result: any[], status: any) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          // const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          setLatitude(result[0].y);
          setLongitude(result[0].x);
        }
      });
    });
  }, [address, latitude, longitude]);
  useEffect(() => {
    goAddress();
  }, [goAddress]);

  return (
    <Container>
      <Contents>
        <form onSubmit={onSubmitJoin}>
          <SignUpForm>
            <Logo>
              <img src="/assets/petmily-logo-pink.png" alt="logo petmily" />
            </Logo>
            <InputUsername>
              <div>닉네임</div>
              <IdCheckForm>
                <input
                  type="text"
                  id="signupusername"
                  placeholder="닉네임 만들기"
                  value={username}
                  onChange={onChangeDisplay}
                  required
                ></input>
                <StyledButtonPink3D onClick={goId}>
                  중복 확인 발급
                </StyledButtonPink3D>
               
              </IdCheckForm>
              {usernameError && <p>{usernameError}</p>}
            </InputUsername>
            <InputEmail>
              <div>E-mail</div>

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
                  인증번호 발급
                </StyledButtonPink3D>
              </EmailAuthForm>
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
                  required
                ></input>
                <StyledButtonPink3D onClick={checkEmail}>
                  인증번호 확인
                </StyledButtonPink3D>
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
                required
              ></input>             
            </InputPW>
            {passwordError && <p>{passwordError}</p>}
            <InputAddress>
              {/* <div value={address} onChange={goAddress}>
                {address}aa
              </div> */}
              <input
                type="address"
                id="address"
                placeholder="내주소 등록하기"
                value={address}
                readOnly
                required
              ></input>
              <StyledButtonPink3D type="button" onClick={handleClick}>
                Open
              </StyledButtonPink3D>
            </InputAddress>

            <div className='submit'>
              <StyledButtonPink3D>회원가입</StyledButtonPink3D>
            </div>
          </SignUpForm>
        </form>
        <div id="map" className="map/"></div>
      </Contents>
    </Container>
  );
};

export default SignUp;
