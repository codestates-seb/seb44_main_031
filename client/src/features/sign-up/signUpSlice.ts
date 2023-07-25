import { createAsyncThunk } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

interface JoinData {
  username: string;
  email: string;
  emailAuth:string;
  password: string;
  latitude: number;
  longitude: number;
  address: string;
}

interface JoinResponse {
  success: boolean;
}

export const actionS = createAsyncThunk(
  'user/join',
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async (data: JoinData) => {
    try {
      const result = await axios.post<JoinResponse>(
        'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/auth/sign-up',
        {
          username: data.username,
          email: data.email,
          code:data.emailAuth,
          password: data.password,
          latitude: data.latitude,
          longitude: data.longitude,
          address: data.address,
        }
      );
      if (result.status === 200) {       
        return true;
      } else{
      
      }
    } catch (err: any) {
      return err.message;
    }
  }
);

export const signupSlice = createSlice({
  name: 'signupUser',
  initialState: {
    isJoined: false,
    isJoining: false,
    joinUser: false,
    joinRejectReason: '',
    joinErrorReasion: '',
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      // eslint-disable-next-line no-unused-vars
      .addCase(actionS.pending, (state) => {
        state.isJoining = true;
      })
      .addCase(actionS.fulfilled, (state, action) => {
        state.isJoined = true;
        state.isJoining = false;
        state.joinUser = action.payload as boolean;
        state.joinRejectReason = '';
      })
      .addCase(actionS.rejected, (state, action) => {
        state.isJoining = false;
        state.joinErrorReasion = action.payload as string;
      });
  },
});

export default signupSlice.reducer;
