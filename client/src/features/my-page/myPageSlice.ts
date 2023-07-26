import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
export const AWS_URL_PATH =
  'http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080';

interface Profile {
  username: string;
  address: string;
  imgUrl: string;
  latitude: number;
  longitude: number;
  pets: [
    {
      id: number;
      name: string;
      mbti: string;
      birth: string;
      gender: boolean;
      neutralization: boolean;
      imgUrl: string;
      breed: number;
      breedName: string;
    }
  ];
}

interface UserState {
  status: 'good' | 'loading' | 'failed';
  error: string | null;
  profile: Profile;
  mypostList: MypostList[];
  mypetWalk:MyPetWalk[];
}

const initialState: UserState = {
  status: 'good',
  error: null,
  profile: {
    username: 'string',
    address: 'string',
    imgUrl: 'string',
    latitude: 35.113752878216,
    longitude: 128.96567277736,
    pets: [
      {
        id: 0,
        name: 'string',
        birth: 'string',
        gender: false,
        mbti: 'intp',
        neutralization: true,
        imgUrl: '',
        breed: 1,
        breedName: 'string',
      },
    ],
  },
  mypostList: [],
  mypetWalk:[],
};
export interface MyPetWalk{
  articleId:number,
  title: string,
  startDate: string,
  createdAt: string
}
interface MypostList{
  articleId:number,
  title: string,
  startDate: string,
  createdAt: string
}
interface FetchMypostListResponse {
  success: true;
  result: MypostList[];
}
interface FetchUsersResponse {
  success: true;
  result: {
    username: string;
    address: string;
    imgUrl: string;
    latitude: number;
    longitude: number;
    pets: [
      {
        id: number;
        name: string;
        mbti: string;
        birth: string;
        gender: boolean;
        neutralization: boolean;
        imgUrl: string;
        breed: number;
        breedName: string;

      }
    ];
  };
}

interface FetchUsernameChangeResponse {
  username: string;
  password: string;
}
interface FetchAddressChangeResopnse {
  address: string;
  latitude: number;
  longitude: number;
}
// userId: string;
interface FetchPssswordChangeResponse {
  password: string;
  newPassword: string;
  newPasswordCheck: string;
}
export const fetchUsers = createAsyncThunk<Profile, number>(
  'users/mypage/get',
  async (userId) => {
 
    const response = await axios.get<FetchUsersResponse>(
      `${AWS_URL_PATH}/users/${userId}`
    );
    return response.data.result;
  }
);
export const fetchMypostList = createAsyncThunk<
  MypostList[],
  void,
  {
    rejectValue: string;
  }
>('users/get-articles-written-by', async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get<FetchMypostListResponse>(
      `${AWS_URL_PATH}/users/get-articles-written-by`,
      {
        headers: {
          Authorization: localStorage.getItem('accessToken'),
        },
      }
    );
    return response.data.result;
  } catch (error) {
    return rejectWithValue('개최한 모임이 없습니다');
  }
});
export const fetchPetWalkList = createAsyncThunk<
  MyPetWalk[],
  string,
  {
    rejectValue: string;
  }
>('users/get-articles-attended', async (petId) => {
  try {
    const response = await axios
    .get(
      `http://ec2-3-36-94-225.ap-northeast-2.compute.amazonaws.com:8080/users/get-articles-attended/${petId}`,
      {
        headers: {
          Authorization: localStorage.getItem('accessToken'),
        },
      }
    )
    console.log(response);
    return response.data.result;
  } catch (error) {
    console.log(`에러는 ${error}`)
  }
});

export const fetchUsersname = createAsyncThunk<
  Profile,
  FetchUsernameChangeResponse
>('users/username', async ({ username, password }) => {
  const response = await axios.patch<FetchUsersResponse>(
    `${AWS_URL_PATH}/users/username`,
    {
      newNickname: username,
      password: password,
    },
    {
      headers: {
        Authorization: localStorage.getItem('accessToken'),
      },
    }
  );
  return response.data.result;
});
export const fetchPassword = createAsyncThunk<
  Profile,
  FetchPssswordChangeResponse
>('users/password', async ({ password, newPassword, newPasswordCheck }) => {
  const response = await axios.patch<FetchUsersResponse>(
    `${AWS_URL_PATH}/users/password`,
    {
      password: password,
      newPassword: newPassword,
      newPasswordConfirm: newPasswordCheck,
    },
    {
      headers: {
        Authorization: localStorage.getItem('accessToken'),
      },
    }
  );
  return response.data.result;
});
export const fetchAddress = createAsyncThunk<
  Profile,
  FetchAddressChangeResopnse
>('users/address', async ({ address, latitude, longitude }) => {
  const response = await axios.patch<FetchUsersResponse>(
    `${AWS_URL_PATH}/users/address`,
    {
      address: address,
      latitude: latitude,
      longitude: longitude,
    },
    {
      headers: {
        Authorization: localStorage.getItem('accessToken'),
      },
    }
  );
  return response.data.result;
});
export const myPageSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.profile = action.payload;
        state.status = 'good';
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message as string;
        const currentPath = window.location.pathname;
      window.location.href = `sign-in?path=${encodeURIComponent(
        currentPath
      )}`;
      })
      .addCase(fetchUsersname.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchUsersname.fulfilled, (state, action) => {
        state.profile = action.payload;
        state.status = 'good';
      })
      .addCase(fetchUsersname.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message as string;
      })
      .addCase(fetchAddress.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchAddress.fulfilled, (state, action) => {
        state.profile = action.payload;
        state.status = 'good';
      })
      .addCase(fetchAddress.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message as string;
      })
      .addCase(fetchPassword.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchPassword.fulfilled, (state, action) => {
        state.profile = action.payload;
        state.status = 'good';
      })
      .addCase(fetchPassword.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message as string;
      })
      .addCase(fetchMypostList.pending, (state) => {
        state.status='loading';
        // You can handle loading state if necessary
      })
      .addCase(fetchMypostList.fulfilled, (state, action) => {
        // You can update the state with the fetched data here
        // For example, if you have a mypostList property in UserState:
        state.mypostList = action.payload;
        state.status = 'good';
      })
      .addCase(fetchMypostList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string; // The rejectWithValue value will be stored here
      })
      .addCase(fetchPetWalkList.pending, (state) => {
        state.status='loading';
        // You can handle loading state if necessary
      })
      .addCase(fetchPetWalkList.fulfilled, (state, action) => {
        // You can update the state with the fetched data here
        // For example, if you have a mypostList property in UserState:
        state.mypetWalk = action.payload;
        state.status = 'good';
      })
      .addCase(fetchPetWalkList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string; // The rejectWithValue value will be stored here
      });
  },
});

export default myPageSlice.reducer;
