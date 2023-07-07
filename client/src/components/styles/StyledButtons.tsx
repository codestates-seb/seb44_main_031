import { styled } from 'styled-components';

export const StyledButtonPink3D = styled.button`
  background-color: var(--pink-400);
  border: 2px solid var(--pink-400);
  border-radius: 30px;
  box-shadow: var(--pink-100) 4px 4px 0 0;
  color: white;
  cursor: pointer;
  display: inline-block;
  font-weight: 600;
  font-size: 16px;
  padding: 0 18px;
  line-height: 40px;
  text-align: center;
  text-decoration: none;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;

  &:hover {
    background-color: rgb(255, 84, 141);
    border: 2px solid rgb(255, 84, 141);
  }

  &:active {
    box-shadow: var(--pink-300) 2px 2px 0 0;
    transform: translate(2px, 2px);
  }

  @media (min-width: 768px) {
    & {
      min-width: 120px;
      padding: 0 25px;
    }
  }
`;

// const StyeldButtonFlat = styled.button`
//   font-weight: bold;
//   padding: 5px;
//   color: white;
//   background-color: var(--pink-400);
//   height: 40px;
//   width: 100px;
//   cursor: pointer;

//   border: none;
//   border-radius: 15px;
// `;
