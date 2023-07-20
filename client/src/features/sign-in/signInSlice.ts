/* eslint-disable @typescript-eslint/no-unused-vars */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import jwt_decode from 'jwt-decode';

interface LoginState {
  isLoggedIn: boolean;
  token: string;
  refreshToken: string;
  userId: string;
  loginRejectReason: string;
}

interface LoginData {
  email: string;
  password: string;
}

interface DecodedToken {
  userId: any;
}

const initialState: LoginState = {
  isLoggedIn: false,
  token: '',
  refreshToken: '',
  userId: '',
  loginRejectReason: '',
};
interface ServerResponse {
  accessToken: string;
  userId: string;
}
export const actionL = createAsyncThunk<ServerResponse, LoginData>(
  'user/join',
  async (data: LoginData) => {
    try {
      const response = await axios.post(
        'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/login',
        {
          email: data.email,
          password: data.password,
        }
      );
      console.log(response);
      const authorizationHeader = response.headers['authorization'];
      console.log(authorizationHeader);
      if (!authorizationHeader) {
        throw new Error('Authorization header not found in the response.');
      }

      const accessToken = authorizationHeader.slice();
      const userId = (jwt_decode(accessToken) as DecodedToken).userId;

      // Return the extracted token and user ID as an object
      return { accessToken, userId };
    } catch (error) {
      // 오류 발생 시 적절한 처리를 수행합니다.
      console.log('오류 발생:', error);
      throw error; // 오류를 다시 throw하여 reject 상태로 전달합니다.
    }
  }
);

export const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    updateLoginState: {
      reducer: (state, action: PayloadAction<LoginState>) => {
        state.isLoggedIn = action.payload.isLoggedIn;
        state.token = action.payload.token;
        state.refreshToken = action.payload.refreshToken;
        state.userId = action.payload.userId;
        state.loginRejectReason = action.payload.loginRejectReason;
      },
      prepare: () => {
        // How do you determine if the user is logged in?
        const isLoggedIn = localStorage.getItem('Token') ? true : false;
        const token = localStorage.getItem('Token') || '';
        const refreshToken = localStorage.getItem('refreshToken') || '';
        const userId = localStorage.getItem('userId') || '';
        const loginRejectReason = '';

        return {
          payload: {
            isLoggedIn,
            token,
            refreshToken,
            userId,
            loginRejectReason,
          },
        };
      },
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(actionL.pending, (state) => {
        state.isLoggedIn = false; // Reset login state
        state.token = ''; // Reset token
        state.userId = '';
        state.loginRejectReason = ''; // Reset reject reason
      })
      .addCase(actionL.fulfilled, (state, action) => {
        state.isLoggedIn = true; // Update login state
        state.token = action.payload.accessToken; // Set token
        state.userId = action.payload.userId;
      })
      .addCase(actionL.rejected, (state, action) => {
        console.log(action.error);
        state.isLoggedIn = false; // Reset login state
        state.token = ''; // Reset token
        state.userId = ''; // Reset id
        state.loginRejectReason = action.error.message as string; // Set reject reason
      });
  },
});

export default loginSlice.reducer;

export const { updateLoginState } = loginSlice.actions;
