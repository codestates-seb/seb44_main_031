import { styled } from 'styled-components';
import { IoPaw, IoMailOutline } from 'react-icons/io5';

const Footer = () => {
  return (
    <FooterContainer>
      <FooterText>
        <IoPaw className="io-paw" /> Explore and connect with fellow pet owners
        in your area!
      </FooterText>
      <FooterText>
        🐶 Petmily provides a service for pet owners to find pet walk mates in
        your community.
      </FooterText>
      <FooterText>
        Contact us at <IoMailOutline /> support@PetMily.com for any inquiries.
      </FooterText>
    </FooterContainer>
  );
};

export default Footer;

// FEEDBACK: /* Pink background... */ 라고 주석은 되어있는데 실제론 white로 되어있어서 혼동됩니다. 그리고 이렇게 되어 있으면 자연스레 '복붙'이 떠오르는 건 왜일까요...
const FooterContainer = styled.footer`
  background-color: white; /* Pink background color for the footer */
  padding: 20px;
  color: black; /* White text color */
  text-align: center;
  box-shadow: 2px 0px 10px rgba(0, 0, 0, 0.1);

  .io-paw {
    fill: var(--pink-400);
  }
`;

const FooterText = styled.p`
  font-size: 16px;
  margin-bottom: 10px;
`;
