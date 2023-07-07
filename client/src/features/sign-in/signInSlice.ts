import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import jwt_decode from 'jwt-decode';

interface LoginState {
  isLoggedIn: boolean;
  token: string;
  refreshToken: string;
  id: string;
  userId: string;
  loginRejectReason: string;
}

interface LoginData {
  email: string;
  password: string;
}

interface DecodedToken {
  username: string;
  memberId: string;
}

const initialState: LoginState = {
  isLoggedIn: false,
  token: '',
  refreshToken: '',
  id: '',
  userId: '',
  loginRejectReason: '',
};

export const actionL = createAsyncThunk(
  'user/join',
  async (data: LoginData) => {
    const response = await axios.post(
      'http://ec2-52-79-240-48.ap-northeast-2.compute.amazonaws.com:8080/api/users/sign-in',
      {
        email: data.email,
        password: data.password,
      }
    );
    const accessToken = response.headers.authorization.slice(); // Extract JWT token

    const userId = (jwt_decode(accessToken) as DecodedToken).username;
    const userMemberId = (jwt_decode(accessToken) as DecodedToken).memberId;

    // Return the extracted token and user ID as an object
    return { accessToken, userId, userMemberId };
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
        state.id = action.payload.id;
        state.userId = action.payload.userId;
        state.loginRejectReason = action.payload.loginRejectReason;
      },
      prepare: () => {
        // How do you determine if the user is logged in?
        const isLoggedIn = localStorage.getItem('Token') ? true : false;
        const token = localStorage.getItem('Token') || '';
        const refreshToken = localStorage.getItem('refreshToken') || '';
        const id = localStorage.getItem('Id') || '';
        const userId = localStorage.getItem('MemberId') || '';
        const loginRejectReason = '';

        return {
          payload: {
            isLoggedIn,
            token,
            refreshToken,
            id,
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
        state.id = ''; // Reset id
        state.userId = '';
        state.loginRejectReason = ''; // Reset reject reason
      })
      .addCase(actionL.fulfilled, (state, action) => {
        state.isLoggedIn = true; // Update login state
        state.token = action.payload.accessToken; // Set token
        state.id = action.payload.userId;
        state.userId = action.payload.userMemberId;
      })
      .addCase(actionL.rejected, (state, action) => {
        state.isLoggedIn = false; // Reset login state
        state.token = ''; // Reset token
        state.id = ''; // Reset id
        state.loginRejectReason = action.error.message as string; // Set reject reason
      });
  },
});

export default loginSlice.reducer;

export const { updateLoginState } = loginSlice.actions;
