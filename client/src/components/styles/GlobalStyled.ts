import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    text-decoration: none;
    line-height: 1;
    font-family: Noto Sans KR,sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    letter-spacing: -.5px;
    }

    :root {
      /* color */
      --white: rgba(255, 255, 255, 1);
      --black: rgba(0, 0, 0, 1);

      --pink-100: rgba(252, 246, 251, 1);
      --pink-200: rgba(250, 203, 217, 1);
      --pink-300: rgba(255, 170, 198, 1);
      --pink-400: rgba(255, 64, 129, 1);

      --skyblue-100: rgba(189, 237, 239, 1);
      --skyblue-200: rgba(155, 206, 233, 1);

      --green-100: rgba(182, 231, 195, 1);
      --green-200: rgba(94, 203, 124, 1);

      --beige-100: rgba(250, 244, 229, 1);

      --black-100: rgba(240, 240, 240, 1);
      --black-200: rgba(240, 240, 240, 1);
      --black-300: rgba(232, 232, 232, 1);
      --black-400: rgba(225, 225, 225, 1);
      --black-500: rgba(217, 217, 217, 1);
      --black-600: rgba(174, 174, 174, 1);
      --black-700: rgba(130, 130, 130, 1);
      --black-800: rgba(87, 87, 87, 1);
      --black-900: rgba(43, 43, 43, 1);
    }
`;

export default GlobalStyle;
