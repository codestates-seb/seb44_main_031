import { Route, Routes } from 'react-router-dom';
import GlobalStyle from './components/styles/GlobalStyled';
import WalkMateCreate from './features/walk-mate-create/WalkMateCreate';
import WalkMateAll from './features/walk-mate-all/WalkMateAll';
import Header from './components/Header';
import Footer from './components/Footer';
import SignUp from './features/sign-up/SignUp';
import SignIn from './features/sign-in/SignIn';
import WalkMateDetail from './features/walk-mate-detail/WalkMateDetail';

function App() {
  return (
    <div>
      <GlobalStyle />
      <Header />
      <Routes>
        <Route path="/" element={<p>초기화면 구현 테스트</p>} />
        <Route path="walk-mate">
          <Route path="all" element={<WalkMateAll />} />
          <Route path="create" element={<WalkMateCreate />} />
          <Route path=":articleId" element={<WalkMateDetail />} />
        </Route>
        <Route path="users">
          <Route path="sign-up" element={<SignUp />} />
          <Route path="sign-in" element={<SignIn />} />
        </Route>
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
