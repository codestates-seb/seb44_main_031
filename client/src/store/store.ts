import { configureStore } from '@reduxjs/toolkit';
import counterReducer from './initSlice';
import signUpReducer from '../features/sign-up/signUpSlice';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const store = configureStore({
  reducer: {
    counter: counterReducer,
    signup: signUpReducer,
  },
});
// export type useAppDispatch = typeof store.dispatch;

export const useAppDispatch: () => typeof store.dispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<
  ReturnType<typeof store.getState>
> = useSelector;

export default store;
