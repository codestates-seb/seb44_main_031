import { createAsyncThunk } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

interface JoinData {
  displayName: string;
  email: string;
  emailAuth: string;
  password: string;
}

interface JoinResponse {
  success: boolean;
}

export const actionS = createAsyncThunk(
  'user/join',
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async (data: JoinData) => {
    const result = await axios.post<JoinResponse>(
      'http://ec2-52-79-240-48.ap-northeast-2.compute.amazonaws.com:8080/api/users/sign-up',
      {
        displayName: data.displayName,
        email: data.email,
        code: data.emailAuth,
        password: data.password,
      }
    );

    if (result.data.success === true) {
      console.log('박지훈');
      const success = result.data.success;
      return { success };
    }
    console.log(result);
  }
);
interface initialState {
  isJoined: boolean;
  isJoining: boolean;
  joinUser: boolean;
  joinRejectReason: string;
}
export const signupSlice = createSlice({
  name: 'signupUser',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      // eslint-disable-next-line no-unused-vars
      .addCase(actionS.pending, (state, action) => {
        state.isJoining = true;
      })
      .addCase(actionS.fulfilled, (state, action) => {
        state.isJoined = true;
        state.isJoining = false;
        state.joinUser = action.payload.data;
        state.joinRejectReason = '';
      })
      .addCase(actionS.rejected, (state, action) => {
        state.isJoining = false;
        state.joinErrorReasion = action.error;
      });
  },
});

export default signupSlice.reducer;
