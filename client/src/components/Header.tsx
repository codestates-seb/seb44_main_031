import { styled } from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { NavLink } from 'react-router-dom';
import {
  walkMateAllUrl,
  myPageUrl,
  signUpUrl,
  signInUrl,
  secondHandUrl,
  petShopUrl,
} from '../api/reactRouterUrl';
import Logout from '../features/sign-in/LogOut';

// FEEDBACK: localStorage.getItem('accessToken')은 다른 곳에서도 충분히 쓰일 가능성이 있습니다.
// 왜냐하면 localStorage 자체가 전역적으로 쓰이는 것이기 때문입니다.
// 그렇다면 'accessToken'이 여러군데에서 쓰일텐데 이렇게 하드코딩으로 쓰는 것은 좋은 방법이 아닙니다.
// 따라서 const KEY_ACCESS_TOKEN = 'accessToken'; 처럼 상수화해서 관리하거나
// 아니면 localStorage를 추상화해서 쓰는 방법이 더 좋습니다.
// 아래 코드는 예시 입니다.

// localStorage.ts
const KEY_ACCESS_TOKEN = 'accessToken';

/**
 * LocalStorage를 추상화한 객체입니다.
 */
export const LocalStorageAuth = {
  getAccessToken: () => {
    return localStorage.getItem(KEY_ACCESS_TOKEN);
  },
  setAccessToken: (accessToken: string) => {
    localStorage.setItem(KEY_ACCESS_TOKEN, accessToken);
  },
  removeAccessToken: () => {
    localStorage.removeItem(KEY_ACCESS_TOKEN);
  },
  // ...
};

const Header = () => {
  const navigate = useNavigate();

  const handleLogoClick = () => {
    navigate('/');
    window.location.reload();
  };
  const handleSignUpClick = () => {
    navigate(signUpUrl);
  };
  const handleLogin = () => {
    navigate(signInUrl);
  };
  return (
    <HeaderContainer>
      <LogoContainer onClick={handleLogoClick}>
        <LogoImage src="/src/assets/petmily-logo-pink.png" alt="PetMily Logo" />
        <LogoText>PetMily</LogoText>
      </LogoContainer>
      <NavigationContainer>
        <StyledNavLink to={walkMateAllUrl}>산책모임</StyledNavLink>
        <StyledNavLink to={secondHandUrl}>애견용품 중고거래</StyledNavLink>
        <StyledNavLink to={petShopUrl}>우리동네 애견샵</StyledNavLink>
        <StyledNavLink to={myPageUrl}>마이페이지</StyledNavLink>
      </NavigationContainer>
      <AuthContainer>
        {/* Logout은 버튼인데 Button 이라는 이름이 없고 LoginButton은 Logout과 달리 Button이 붙어있네요. 이름은 통일성 있게 관리해주세요. */}
        {localStorage.getItem('accessToken') ? (
          <Logout />
        ) : (
          <>
            <LoginButton onClick={handleLogin}>Login</LoginButton>
            <SignUp onClick={handleSignUpClick}>회원가입</SignUp>
          </>
        )}
      </AuthContainer>
    </HeaderContainer>
  );
};

const HeaderContainer = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;

  height: 80px;
  background-color: white;
  padding: 0 80px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  left: 0;
  z-index: 999;
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  :hover {
    cursor: pointer;
  }
`;

const LogoImage = styled.img`
  width: 40px;
  height: 40px;
  margin-right: 10px;
`;

const LogoText = styled.h1`
  font-size: 24px;
  font-weight: bold;
  color: var(--black-900);
`;

const NavigationContainer = styled.div`
  display: flex;
  gap: 80px;
`;

const StyledNavLink = styled(NavLink)`
  font-size: 18px;
  font-weight: bold;
  color: var(--black-900);
  text-decoration: none;
  cursor: pointer;
  position: relative;

  &:not(.active)::before {
    content: '';
    position: absolute;
    bottom: -8px;
    left: 50%;
    transform: translateX(-50%);
    width: 0;
    height: 3px;
    border-radius: 30px;
    background-color: var(--pink-400);
    transition: width 0.3s ease-out;
  }

  &:hover:before {
    width: 100%;
  }

  &.active::after {
    content: '';
    position: absolute;
    bottom: -8px; /* Adjust this value to control the distance of the underline from the text */
    left: 0;
    width: 100%; /* This will make the underline span the entire width of the NavLink */
    height: 3px; /* Adjust this value to control the thickness of the underline */
    border-radius: 30px;
    background-color: var(--pink-400); /* Set the color of the underline */
  }
`;

const AuthContainer = styled.div`
  display: flex;
  gap: 10px;
  .logout {
    width: 20px;
  }
`;

const LoginButton = styled.button`
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: var(--pink-400);
  border-radius: 20px;
  padding: 8px 16px;
  border: none;
  cursor: pointer;
  transition: color 0.3s ease-in-out;
  &:hover {
    color: var(--black-900);
  }
`;

const SignUp = styled.button`
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: var(--pink-400);
  border-radius: 20px;
  padding: 8px 16px;
  border: none;
  cursor: pointer;
  transition: color 0.3s ease-in-out;
  &:hover {
    color: var(--black-900);
  }
`;

export default Header;
