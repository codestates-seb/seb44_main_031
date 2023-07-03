import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    text-decoration: none;
    /* font-family: 'Noto Sans', 'Noto Sans KR', sans-serif; */
    line-height: 1;
    font-family: Noto Sans KR,sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    letter-spacing: -.5px;
    }
`;

export default GlobalStyle;
