import { configureStore } from '@reduxjs/toolkit';
import counterReducer from './initSlice';
import signUpReducer from '../features/sign-up/signUpSlice';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const store = configureStore({
  reducer: {
    counter: counterReducer,
    signup: signUpReducer,
  },
});
export type useAppDispatch = typeof store.dispatch;
export default store;
