// NOTE: 컴포넌트에서 axios로 http 요청을 보낼때 할 때 사용할 커스텀 훅입니다. 추후 추가 구현 예정입니다. (이영탁)
// TODO:
// 1. 페이지 첫렌더에 useEffect GET 요청을 보내는 상황을 고려해 작성할것
// 2. 버튼을 클릭했을때 eventHandler 함수에서 GET, POST, PATCH, DELETE 요청을 보내는 상황을 고려해 작성할것
// 3. data, loading, error 에 대한 state 값을 가지고 있을것.

// import { useState, useEffect } from 'react';

// type configObj = {
//   axiosInstance: ,
//   method: ,
//   url: ,
//   requestConfig:
// };

// const useAxios = (configObj) => {
//   const { axiosInstance, method, url, requestConfig = {} } = configObj;

//   const [response, setResponse] = useState([]);
//   const [error, setError] = useState('');
//   const [loading, setLoading] = useState(true);

//   useEffect(()=> {

//   }, [])

//   return [response, error, loading];
// };
