import { Route, Routes } from 'react-router-dom';
import GlobalStyle from './components/styles/GlobalStyled';
import SignUp from './features/sign-up/SignUp';

import WalkMateDetail from './features/walk-mate-detail/WalkMateDetail';
function App() {
  return (
    <div>
      <GlobalStyle />
      <Routes>
        <Route
          path="/"
          //
          element={<WalkMateDetail />}
        />
        <Route
          path="/sign-up"
          // element={<p>초기구현 테스트 완료 - 나중에 지워주세요</p>}
          element={<SignUp />}
        />
      </Routes>
    </div>
  );
}

export default App;
