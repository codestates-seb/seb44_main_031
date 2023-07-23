import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
const Logout = () => {
  const navigate = useNavigate();

  // Logout function to clear local storage and redirect to the home page
  const handleLogout = useCallback(() => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userId');
    navigate('/');
  }, [navigate]);

  return (
    <StyledButtonPink3D onClick={handleLogout} className='logout' >
   로그아웃
    </StyledButtonPink3D>
  );
};

export default Logout;