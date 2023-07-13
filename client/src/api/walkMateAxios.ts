import axios from 'axios';

// 개발 상황에 따라 baseURL, Token 값을 설정해서 쓰면 됨. (영탁)

const JSON_SERVER = 'http://localhost:3001';
// const BASE_URL = '';
// const Token = 'Bearer ' + localStorage.getItem('token');
const Token = '';

export const axiosInstance = axios.create({
  baseURL: JSON_SERVER,
  headers: {
    Authorization: Token,
  },
});
