import { styled } from 'styled-components';

const Footer = () => {
  return <FooterContainer>Footer</FooterContainer>;
};

const FooterContainer = styled.footer`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 80px;
  background-color: var(--pink-100);
`;

export default Footer;
