// // redirectMiddleware.ts

// import { Middleware } from '@reduxjs/toolkit';
// import { updateLoginState } from './signInSlice';

// const redirectMiddleware: Middleware = (store) => (next) => (action) => {
//   if (action.type.endsWith('/rejected') && action.error && action.error.message === 'Request failed with status code 403') {
//     // 403 에러가 발생했을 때, updateLoginState 액션을 디스패치하여 로그인 상태를 false로 설정합니다.
//     store.dispatch(
//       updateLoginState({
//         isLoggedIn: false,
//         token: '',
//         refreshToken: '',
//         userId: '',
//         loginRejectReason: '권한이 없습니다.',
//       })
//     );

//     // 로그인 페이지로 리다이렉트합니다.
//     // 만약 React Router를 사용한다면, 'login'을 로그인 페이지의 적절한 경로로 변경해야 합니다.
//     window.location.href = '/login';
//   }

//   return next(action);
// };

// export default redirectMiddleware;
