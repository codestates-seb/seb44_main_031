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
        <LoginButton onClick={handleLogin}>Login</LoginButton>
        <SignUp onClick={handleSignUpClick}>회원가입</SignUp>
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
