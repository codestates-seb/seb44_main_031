import React, { useEffect, useRef, useContext } from 'react';
import {
  mapMarkerIconGreenPath,
  mapMarkerIconRedPath,
} from '../../constants/imageSrcPath';
import { Article, WalkMateAllContext } from './WalkMateAll';

interface WalkMatesMapProps {
  setSelectedCard: React.Dispatch<React.SetStateAction<number | null>>;
}

const WalkMatesMap = ({ setSelectedCard }: WalkMatesMapProps) => {
  const mapRef = useRef<HTMLDivElement>(null);

  const queryData = useContext(WalkMateAllContext);

  useEffect(() => {
    const latitude = queryData?.pages[0].data.userInfo.latitude;
    const longitude = queryData?.pages[0].data.userInfo.longitude;

    const markerIdMap = new Map<kakao.maps.Marker, number>();

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

    // 내 위치를 표시할 마커 생성, 인포 윈도우, 마우스 호버 이벤트 등록하기
    // 마커를 표시할 위치입니다
    const myPosition = new kakao.maps.LatLng(latitude, longitude);

    // 마커를 생성합니다
    const marker = new kakao.maps.Marker({
      position: myPosition,
    });

    // 마커를 지도에 표시합니다.
    marker.setMap(map);

    // 마커에 커서가 오버됐을 때 마커 위에 표시할 인포윈도우를 생성합니다
    const iwContent =
      '<div style="padding:5px; text-align:center; margin-left:10px;">내 등록 위치입니다</div>'; // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다

    // 인포윈도우를 생성합니다
    const infowindow = new kakao.maps.InfoWindow({
      content: iwContent,
    });

    // 마커에 마우스오버 이벤트를 등록합니다
    kakao.maps.event.addListener(marker, 'mouseover', function () {
      // 마커에 마우스오버 이벤트가 발생하면 인포윈도우를 마커위에 표시합니다
      infowindow.open(map, marker);
    });

    // 마커에 마우스아웃 이벤트를 등록합니다
    kakao.maps.event.addListener(marker, 'mouseout', function () {
      // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
      infowindow.close();
    });

    // 산책 모임들의 여러개의 마커 등록하기
    // 마커 이미지의 이미지 주소입니다
    const imageSrc = mapMarkerIconRedPath;

    // 여러개의 마커 생성하기
    queryData?.pages.map((page: any) => {
      return page.data.articles.map((article: Article) => {
        // 마커 이미지의 이미지 크기 입니다
        const imageSize = new kakao.maps.Size(24, 35);

        // 마커 이미지를 생성합니다
        const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

        // 마커를 생성합니다
        const marker = new kakao.maps.Marker({
          map: map, // 마커를 표시할 지도
          position: new kakao.maps.LatLng(article.latitude, article.longitude), // 마커를 표시할 위치
          title: article.location, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage, // 마커 이미지
        });

        markerIdMap.set(marker, article.articleId);

        // 마커에 표시할 인포윈도우를 생성합니다
        const infowindow = new kakao.maps.InfoWindow({
          content: `<div style="display:flex; flex-wrap:wrap; padding:10px; height:50px; width:260px; text-align:center;">${
            article.title.slice(0, 35) + '...'
          }</div>`, // 인포윈도우에 표시할 내용
          // removable: iwRemoveable, // x 버튼 기능 추가
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

        kakao.maps.event.addListener(marker, 'click', function () {
          // Get the corresponding ID from the markerIdMap
          const markerId = markerIdMap.get(marker);
          setSelectedCard(markerId as number);

          // 마커 위에 인포윈도우를 표시합니다
          marker.setImage(
            new kakao.maps.MarkerImage(
              mapMarkerIconGreenPath,
              new kakao.maps.Size(24, 35),
              { offset: new kakao.maps.Point(13, 35) }
            )
          );

          // 스크롤을 해당 카드로 이동시킵니다
          const pairedCard = window.document.getElementById(`card-${markerId}`);
          pairedCard?.scrollIntoView({
            behavior: 'smooth',
            block: 'center',
          });
        });
      });
    });

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
  }, [queryData?.pages, setSelectedCard]);

  return (
    <div
      id="map"
      style={{
        width: '450px',
        height: '600px',
        position: 'sticky',
        top: '160px',
        borderRadius: '25px',
      }}
      ref={mapRef}
    />
  );
};

export default WalkMatesMap;
