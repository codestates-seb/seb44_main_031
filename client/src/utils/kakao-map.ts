// NOTE: 카카오 지도 API 에서지도에 표시할 원을 생성하는 카카오맵 함수 (이영탁)
// strokeStyle 의 타입을 지정하는 부분에서 발생하는 타입스크립트 에러를 해결하지 못햇음.
// Circel.d.ts 에 정의되어 있는 CircleOptions 를 어떻게 import 해올지 모르겠음.
// TODO: strokeStyle 타입 지정하는 부분의 타입스크립트 에러 해결하고 함수화 완성하기.

// import { CircleOptions } from 'kakao.maps';

type strokeStyle =
  | 'solid'
  | 'shortdash'
  | 'shortdot'
  | 'shortdashdot'
  | 'shortdashdotdot'
  | 'dot'
  | 'dash'
  | 'dashed'
  | 'dashdot'
  | 'longdash'
  | 'longdashdot'
  | 'longdashdotdot';

export interface CircleOptions {
  latitude: number;
  longitude: number;
  radius: number; // 미터 단위의 원의 지름입니다
  strokeWeight: number; // 선의 두께입니다
  strokeColor: string; // 선의 색깔입니다
  strokeOpacity: number; // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
  strokeStyle: strokeStyle; // 선의 스타일 입니다
  fillColor: string; // 채우기 색깔입니다
  fillOpacity: number; // 채우기 불투명도 입니다
}

export const makeCircleOnTheMap = (circleOptions: CircleOptions, map: any) => {
  const circle = new kakao.maps.Circle({
    center: new kakao.maps.LatLng(
      circleOptions.latitude,
      circleOptions.longitude
    ), // 원의 중심좌표 입니다
    radius: circleOptions.radius, // 미터 단위의 원의 지름입니다
    strokeWeight: circleOptions.strokeWeight, // 선의 두께입니다
    strokeColor: circleOptions.strokeColor, // 선의 색깔입니다
    strokeOpacity: circleOptions.strokeOpacity, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    strokeStyle: circleOptions.strokeStyle, // 선의 스타일 입니다
    fillColor: circleOptions.fillColor, // 채우기 색깔입니다
    fillOpacity: circleOptions.strokeOpacity, // 채우기 불투명도 입니다
  });
  circle.setMap(map);
};
