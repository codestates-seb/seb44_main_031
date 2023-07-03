import { configureStore } from '@reduxjs/toolkit';
import counterReducer from './initSlice';

const store = configureStore({
  reducer: {
    counter: counterReducer,
  },
});

export default store;
