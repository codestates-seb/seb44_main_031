import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  articles: {
    articleId: 1,
    imgUrl:
      'https://upload.wikimedia.org/wikipedia/commons/a/a4/Bukchon_Hanok_Village_%EB%B6%81%EC%B4%8C_%ED%95%9C%EC%98%A5%EB%A7%88%EC%9D%84_October_1_2020_15.jpg',
    date: '2023-07-20T16:23',
    title: '북촌 육경에서 강아지들 산책하실분~',
    body: '저녁 6시에 미니스탑 편의점에서 만나서 애기들 산책하실분 구합니다',
    location: '서울특별시 종로구 가회동 31-48',
    latitude: 37.582660916157515,
    longitude: 126.98361081274618,
    attendant: 3,
    lefts: 1,
    isSelectedToJoinByViewer: true, //조회하는 유저: 토큰에 담긴 유저 정보
    isMarkerClicked: false,
  },
  userInfo: {
    address: '서울특별시 종로구 가회동 북촌로 64',
    latitude: 37.58251737488069,
    longitude: 126.98517705739235,
  },
};

export const walkMateAllSlice = createSlice({
  name: 'walkMateAll',
  initialState,
  reducers: {
    increment: (state) => {
      // Redux Toolkit allows us to write "mutating" logic in reducers. It
      // doesn't actually mutate the state because it uses the Immer library,
      // which detects changes to a "draft state" and produces a brand new
      // immutable state based off those changes
      state;
    },
    decrement: (state) => {
      state;
    },
    incrementByAmount: (state, action) => {
      state = action.payload;
    },
  },
});

// Action creators are generated for each case reducer function
export const { increment, decrement, incrementByAmount } =
  walkMateAllSlice.actions;

export default walkMateAllSlice.reducer;
