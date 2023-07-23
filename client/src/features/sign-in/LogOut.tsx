import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import axios from 'axios';
const Logout = () => {
  const navigate = useNavigate();

  // Logout function to clear local storage and redirect to the home page
  const handleLogout = useCallback(() => {
    axios
      .delete(
        `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/logout`,
        {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        }
      )
      .then((response) => {
        // 요청이 성공적으로 처리되었을 때 실행할 코드
        console.log(response);
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userId');
        navigate('/');
        window.location.reload();
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.log(error);
      });
    
  }, [navigate]);

  return (
    <StyledButtonPink3D onClick={handleLogout} className='logout' >
   로그아웃
    </StyledButtonPink3D>
  );
};

export default Logout;