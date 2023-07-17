import { useEffect, useRef } from 'react';
import {
  mapMarkerIconGreenPath,
  mapMarkerIconRedPath,
} from '../../constants/imageSrcPath';

const latitude = 37.58251737488069;
const longitude = 126.98517705739235;

// 여러개의 마커 생성하기 위한 위치
const positions = [
  {
    location: '서울특별시 종로구 가회동 31-48',
    latlng: new kakao.maps.LatLng(37.582660916157515, 126.98361081274618),
    content: '북촌 육경에서 강아지들 산책하실분~',
  },
  {
    location: '서울특별시 종로구 자하문로15길 18',
    latlng: new kakao.maps.LatLng(37.581425248408, 126.97065669055479),
    content: '통인시장에서 만나서 애기들 산책해요',
  },
  {
    location: '서울특별시 종로구 이화동 대학로8길 1',
    latlng: new kakao.maps.LatLng(37.58127764729223, 127.00261713775853),
    content: '마로니에 공원 근처에서 오후에 강아지 산책하실 이웃 있나요?',
  },
  {
    location: '서울특별시 종로구 종로 99',
    latlng: new kakao.maps.LatLng(37.57134955914499, 126.98842605662585),
    content: '탑골공원에서 강아지 같이 산책해요~',
  },
];

const WalkMatesMap = () => {
  const mapRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const container = mapRef.current;

    const options = {
      //지도를 생성할 때 필요한 기본 옵션
      center: new window.kakao.maps.LatLng(latitude, longitude), //지도의 중심좌표.
      level: 7, //지도의 레벨(확대, 축소 정도)
    };

    const map = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

    // 지도에 표시할 원을 생성합니다 (본인 등록 위치의 3km 이내만 선택가능 하다는 ui 표시)
    const circle = new kakao.maps.Circle({
      center: new kakao.maps.LatLng(latitude, longitude), // 원의 중심좌표 입니다
      radius: 3000, // 미터 단위의 원의 지름입니다
      strokeWeight: 2, // 선의 두께입니다
      strokeColor: '#ff5180', // 선의 색깔입니다
      strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
      strokeStyle: 'solid', // 선의 스타일 입니다
      fillColor: 'none', // 채우기 색깔입니다
      fillOpacity: 0.7, // 채우기 불투명도 입니다
    });

    // 지도에 원을 표시합니다
    circle.setMap(map);

    // 마커 이미지의 이미지 주소입니다
    const imageSrc = mapMarkerIconRedPath;

    // 여러개의 마커 생성하기
    for (let i = 0; i < positions.length; i++) {
      // 마커 이미지의 이미지 크기 입니다
      const imageSize = new kakao.maps.Size(24, 35);

      // 마커 이미지를 생성합니다
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

      // 마커를 생성합니다
      const marker = new kakao.maps.Marker({
        map: map, // 마커를 표시할 지도
        position: positions[i].latlng, // 마커를 표시할 위치
        title: positions[i].location, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
        image: markerImage, // 마커 이미지
      });

      // const iwRemoveable = true;
      // 마커에 표시할 인포윈도우를 생성합니다
      const infowindow = new kakao.maps.InfoWindow({
        content: `<div style="display:flex; flex-wrap:wrap; padding:10px; height:50px; width:260px; text-align:center;">${positions[i].content}</div>`, // 인포윈도우에 표시할 내용
        // removable: iwRemoveable,
      });

      // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
      // 이벤트 리스너로는 클로저를 만들어 등록합니다
      // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
      kakao.maps.event.addListener(
        marker,
        'mouseover',
        makeOverListener(map, marker, infowindow)
      );
      kakao.maps.event.addListener(
        marker,
        'mouseout',
        makeOutListener(infowindow)
      );

      // 테스트
      kakao.maps.event.addListener(marker, 'click', function () {
        // 마커 위에 인포윈도우를 표시합니다
        marker.setImage(
          new kakao.maps.MarkerImage(
            mapMarkerIconGreenPath,
            new kakao.maps.Size(24, 35),
            { offset: new kakao.maps.Point(13, 35) }
          )
        );

        // 스크롤을 해당 카드로 이동시킵니다
        window.scrollTo(0, 100);
      });
    }

    // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
    function makeOverListener(map: any, marker: any, infowindow: any) {
      return function () {
        infowindow.open(map, marker);
      };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다
    function makeOutListener(infowindow: any) {
      return function () {
        infowindow.close();
      };
    }
  }, []);

  return (
    <div
      id="map"
      style={{
        width: '450px',
        height: '600px',
        position: 'sticky',
        top: '100px',
      }}
      ref={mapRef}
    />
  );
};

export default WalkMatesMap;
