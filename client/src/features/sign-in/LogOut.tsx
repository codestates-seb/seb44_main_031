import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import axios from 'axios';
import { toast } from 'react-toastify';
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
      .then(() => {
        // 요청이 성공적으로 처리되었을 때 실행할 코드
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userId');
        navigate('/');
        toast.success('로그아웃 완료되었습니다.')
      })
      .catch((error) => {
        // 요청 처리 중에 에러가 발생했을 때 실행할 코드
        console.log(error);
      });
    
  }, [navigate]);

  return (
    <StyledButtonPink3D style={{width:'120px'}} onClick={handleLogout} className='logout'>
   로그아웃
    </StyledButtonPink3D>
  );
};

export default Logout;