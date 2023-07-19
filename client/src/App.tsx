import { Route, Routes } from 'react-router-dom';
import GlobalStyle from './components/styles/GlobalStyled';
import WalkMateCreate from './features/walk-mate-create/WalkMateCreate';
import WalkMateAll from './features/walk-mate-all/WalkMateAll';
import Header from './components/Header';
import Footer from './components/Footer';
import SignUp from './features/sign-up/SignUp';
import SignIn from './features/sign-in/SignIn';
import WalkMateDetail from './features/walk-mate-detail/WalkMateDetail';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AUTO_CLOSE_MS_TIME } from './constants/toastUi';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
// import axios from 'axios';

const queryClient = new QueryClient();

function App() {
  // axios.get(
  //   'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/articles/writer-info',
  //   {
  //     headers: {
  //       Authorization:
  //         'Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInVzZXJJZCI6MSwidXNlcm5hbWUiOiJwYWxrajI1ODA0NzdAbmF2ZXIuY29tIiwic3ViIjoicGFsa2oyNTgwNDc3QG5hdmVyLmNvbSIsImlhdCI6MTY4OTY3NTczNCwiZXhwIjoxNjkwMDM1NzM0fQ.E40oqA8eIADSjL3U3PNYqX60cA_ytJLzz44OkpYHKcE',
  //     },
  //   }
  // );

  return (
    <QueryClientProvider client={queryClient}>
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
        <ToastContainer autoClose={AUTO_CLOSE_MS_TIME} />
      </div>
    </QueryClientProvider>
  );
}

export default App;
