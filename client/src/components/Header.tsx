import { styled } from 'styled-components';

const Header = () => {
  return (
    <HeaderContainer>
      <LogoContainer>
        <LogoImage src="/src/assets/petmily-logo-pink.png" alt="PetMily Logo" />
        <LogoText>PetMily</LogoText>
      </LogoContainer>
      <NavigationContainer>
        <NavLink>산책모임</NavLink>
        <NavLink>애견용품 중고거래</NavLink>
        <NavLink>우리동네 애견샵</NavLink>
        <NavLink>마이페이지</NavLink>
      </NavigationContainer>
      <AuthContainer>
        <LoginButton>Login</LoginButton>
        <LogoutButton>회원가입</LogoutButton>
      </AuthContainer>
    </HeaderContainer>
  );
};

const HeaderContainer = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100vw;
  height: 80px;
  background-color: white;
  padding: 0 80px;

  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
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

const NavLink = styled.a`
  font-size: 18px;
  font-weight: bold;
  color: var(--black-900);
  text-decoration: none;
  cursor: pointer;
  position: relative;

  &:before {
    content: '';
    position: absolute;
    bottom: -8px;
    left: 50%;
    transform: translateX(-50%);
    width: 0;
    height: 4px;
    border-radius: 30px;
    background-color: var(--pink-400);
    transition: width 0.3s ease-out;
  }

  &:hover:before {
    width: 100%;
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

const LogoutButton = styled.button`
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
