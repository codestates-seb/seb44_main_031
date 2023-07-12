import axios from 'axios';

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
