import { configureStore } from '@reduxjs/toolkit';
import signUpReducer from '../features/sign-up/signUpSlice';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import myPageReducer from '../features/my-page/myPageSlice';
import signInReducer from '../features/sign-in/signInSlice';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const store = configureStore({
  reducer: {
    signup: signUpReducer,
    signin: signInReducer,
    mypage: myPageReducer,
  },
});

export const useAppDispatch: () => typeof store.dispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<
  ReturnType<typeof store.getState>
> = useSelector;
export type RootState = ReturnType<typeof store.getState>;

export default store;
