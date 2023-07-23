import { Link, useLocation, useNavigate } from 'react-router-dom';
import { styled } from 'styled-components';
import { useCallback, useState } from 'react';
import { actionL } from './signInSlice';
import { useAppDispatch } from '../../store/store';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f1f2f3;
  font-size: 0.8rem;
  ${StyledButtonPink3D}{
    width: 100px;
    padding:2px;
  }
`;
const Contents = styled.div`
  display: flex;
  flex-direction: column;
  width: 278px;
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
const LoginForm = styled.form`
  display: flex;
  flex-direction: column;
  background-color: white;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.05), 0 20px 48px rgba(0, 0, 0, 0.05),
    0 1px 4px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 24px;
  margin-bottom: 24px;
  .submit{
    display: flex;
    justify-content: center;
    ${StyledButtonPink3D}{
      width: 100px;
      padding: 1px;
      margin-top:10px;
    }
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
const Message = styled.div`
  padding: 16px;
  margin-bottom: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;

  div.areyou {
    margin-top: 12px;
  }
`;

const SignIn = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const onChangeEmail = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setEmail(e.target.value);
    },
    []
  );

  const onChangePassword = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setPassword(e.target.value);
    },
    []
  );
  const location = useLocation();

  // FEEDBACK: 불필요한 여백은 제거해주세요.


  const onSubmitJoin = useCallback(
    async (e: React.FormEvent) => {
      e.preventDefault();

      console.log([email, password]); // FEEDBACK: 불필요한 console.log는 지워주세요.
      try {
        const resultAction: any = await dispatch(actionL({ email, password }));
        console.log(resultAction);
        const { accessToken, userId } = resultAction.payload;
        // Save the token and user ID
        localStorage.setItem('accessToken', accessToken); // FEEDBACK: localStorage 추상화 피드백을 참고해주세요.
        localStorage.setItem('userId', userId);
        const params = new URLSearchParams(location.search);
        const path = params.get('path');
        console.log(path);
        if(path===null){ // FEEDBACK: if (path === null) 처럼 여백을 주세요.
          navigate('/walk-mate/all');
        }else{ // FEEDBACK: } else { 처럼 여백을 주세요.
          navigate(`${path}`); // FEEDBACK: path는 이미 string인데 `${path}` 처럼 감싸준 이유가 있을까요?
        }
      } catch (error: any) {
        console.log('로그인 에러:', error);
        console.log(
          '로그인에 실패했습니다. 이메일과 비밀번호를 다시 확인해주세요.'
        );
      }
    },
    [dispatch, email, navigate, password]
  );

  return (
    <Container>
      <Contents>
        <Logo>
          <img src="/src/assets/petmily-logo-pink.png" alt="logo petmily" />
        </Logo>

        <LoginForm onSubmit={onSubmitJoin}>
          <InputEmail>
            <label>Email</label>
            <input
              type="email"
              id="loginEmail"
              placeholder="Email"
              value={email}
              onChange={onChangeEmail}
            />
          </InputEmail>
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
          <div className='submit'>
            <StyledButtonPink3D type="submit">로그인</StyledButtonPink3D>
          </div>
        </LoginForm>
        <Message>
          <div>
            Don’t have an account? <Link to="">Sign up</Link>
          </div>

          <div className="areyou">
            Are you an employer? <Link to="">Sign up on Talent </Link>
          </div>
        </Message>
      </Contents>
    </Container>
  );
};

export default SignIn;
