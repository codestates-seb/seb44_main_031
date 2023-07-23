import axios, { AxiosError, AxiosResponse } from 'axios';
import { SelectedFilter } from '../features/walk-mate-all/WalkMateAll';
import { toast } from 'react-toastify';
// import { toast } from 'react-toastify';
// import { useNavigate, useLocation } from 'react-router-dom';

// NOTE: 개발 상황에 따라 baseURL, Token 값을 설정해서 쓰면 됩니다 (영탁)
// TODO: 백엔드 배포 완료 후 실제 서버와 연결할때는, BASE_URL 에 해당 배포된 API URL 값을 지정해서 axiosInstance의 baseURL 로 설정해 주시면 됩니다. Token 도 현재는 주석처리 되어있는 localStorage 에서 실제 토큰을 가져오는 로직으로 대체해서 사용해 주시면 됩니다

// const Token =
//   'Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInVzZXJJZCI6NCwidXNlcm5hbWUiOiJkbGF3amRhbHMwMjE4M0BnbWFpbC5jb20iLCJzdWIiOiJkbGF3amRhbHMwMjE4M0BnbWFpbC5jb20iLCJpYXQiOjE2ODk3MzE2OTgsImV4cCI6MTY5MDA5MTY5OH0.Z1igUATtCdo_nOw2rzenKwcElXbAgPVCMjxPgOndbp4';
// const Token = localStorage.getItem('accessToken');
export const getTokenFromLocalStorage = () => {
  return localStorage.getItem('accessToken');
};

// JSON_SERVER
// export const BASE_URL = 'http://localhost:3001';
export const BASE_URL =
  'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080';

// URL PATH
export const signInUrl = '/users/sign-in';

// export const getCreateArticleUrl = 'articles-writer-info';
export const getCreateArticleUrl = 'articles/writer-info';
export const postCreateArticleUrl = 'articles';

// axios default instance
export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: getTokenFromLocalStorage(),
  },
});

// FEEDBACK: URL을 생성하는 함수라면 get...Url처럼 목적이 분명하게 드러나도록 작성하는게 좋습니다.
export const getArticlesUrlJsonServer = (
  page: number,
  size = 5,
  selectedFilter: SelectedFilter,
  searchQuery: string
) => {
  // FEEDBACK: console.log는 개발할 때만 사용하고, 배포할 때는 삭제하는게 좋습니다.
  console.log(
    `articles?_page=${page}&_limit=${size}`,
    selectedFilter,
    searchQuery
  );
  return `articles?_page=${page}&_limit=${size}`;
};

export const getArticlesUrl = (
  page: number,
  size: number,
  selectedFilter: SelectedFilter,
  searchQuery: string
) => {
  if (searchQuery === '') {
    return `articles?page=${page}&size=${size}&days=${selectedFilter.period.value}&sort=${selectedFilter.viewOrder.value}`;
  }
  return `articles?page=${page}&size=${size}&days=${selectedFilter.period.value}&sort=${selectedFilter.viewOrder.value}&keyword=${searchQuery}`;
};

// FEEDBACK: 불필요한 주석은 제거합니다.
// fetch data
export const fetchWalkMates = async (
  pageParam: number,
  size: number,
  selectedFilter: SelectedFilter,
  searchQuery: string
) => {
  const response = await axiosInstance.get(
    // FEEDBACK: 불필요한 주석은 제거합니다.
    // getArticlesUrlJsonServer(pageParam, size, selectedFilter, searchQuery)
    getArticlesUrl(pageParam, size, selectedFilter, searchQuery)
  );
  return response;
};

// FEEDBACK: 유틸 함수나 모듈을 export하는 파일에서 axios 인스턴스 설정은 어색하게 느껴집니다.
// 이런 환경 설정은 다른 파일에서 하는게 더 좋아보입니다.
axiosInstance.interceptors.response.use(
  (response: AxiosResponse<any, any>) => response,
  (error: AxiosError) => {
    // 토큰 인증이 안됐을 경우 현재 페이지 주소를 state 으로 담아서 로그인 페이지로 이동.
    if (
      error.response?.status === 401 ||
      (error.response?.data as any)?.status === 401 ||
      (error.response?.data as any)?.message === 'USER NOT FOUND'
    ) {
      toast.error(error.message);

      const currentPath = window.location.pathname;
      window.location.href = `${signInUrl}?path=${encodeURIComponent(
        currentPath
      )}`;
    }

    return Promise.reject(error);
  }
);

export const isAxiosError = (err: unknown): err is AxiosError => {
  return (err as AxiosError).isAxiosError === true;
};
