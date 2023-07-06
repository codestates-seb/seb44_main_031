import { Route, Routes } from 'react-router-dom';
import GlobalStyle from './components/styles/GlobalStyled';
import WalkMateDetail from './features/walk-Mate-Detail/WalkMateDetail';
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
      </Routes>
    </div>
  );
}

export default App;
