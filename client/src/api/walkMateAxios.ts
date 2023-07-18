import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';

// NOTE: 개발 상황에 따라 baseURL, Token 값을 설정해서 쓰면 됩니다 (영탁)
// TODO: 백엔드 배포 완료 후 실제 서버와 연결할때는, BASE_URL 에 해당 배포된 API URL 값을 지정해서 axiosInstance의 baseURL 로 설정해 주시면 됩니다. Token 도 현재는 주석처리 되어있는 localStorage 에서 실제 토큰을 가져오는 로직으로 대체해서 사용해 주시면 됩니다

const JSON_SERVER = 'http://localhost:3001';
// const BASE_URL = '';
// const Token = 'Bearer ' + localStorage.getItem('token');

const Token = '';

export const getCreateArticleUrl = 'articles-info';
export const postCreateArticleUrl = 'articles';
export const signInUrl = '/users/sign-in';

export const axiosInstance = axios.create({
  baseURL: JSON_SERVER,
  headers: {
    Authorization: Token,
  },
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    // 토큰 인증이 안됐을 경우 현재 페이지 주소를 state 으로 담아서 로그인 페이지로 이동.
    const navigate = useNavigate();
    const location = useLocation();

    if (error.response.status === 401) {
      navigate(signInUrl, {
        state: { path: location.pathname },
      });
    }
    return Promise.reject(error);
  }
);
