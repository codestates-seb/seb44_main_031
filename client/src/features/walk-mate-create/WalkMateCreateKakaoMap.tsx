import { useEffect, useRef, Dispatch, SetStateAction } from 'react';
import { distance } from '../../utils/distance';
import {
  inputValueType,
  isTouchedType,
  isValidType,
} from '../../hooks/useWalkMateForm';

declare global {
  interface Window {
    kakao: any;
  }
}

interface propType {
  inputValue: inputValueType;
  setInputValue: Dispatch<SetStateAction<inputValueType>>;
  setIsTouched: Dispatch<SetStateAction<isTouchedType>>;
  setIsValid: Dispatch<SetStateAction<isValidType>>;
}

const WalkMateCreateKakaoMap = ({
  inputValue,
  setInputValue,
  setIsTouched,
  setIsValid,
}: propType) => {
  const mapRef = useRef<HTMLDivElement>(null);
  // 유저가 등록해놓은 위치
  // 산책 모임 등록할 위치
  // const setLocation = props.setInputValues

  useEffect(() => {
    console.log('Map is rendered');
    const location = inputValue.location;
    const walkLocation = inputValue.walkLocation;

    const container = mapRef.current;
    const options = {
      //지도를 생성할 때 필요한 기본 옵션
      center: new window.kakao.maps.LatLng(walkLocation.lat, walkLocation.lng), //지도의 중심좌표.
      level: 7, //지도의 레벨(확대, 축소 정도)
    };

    const map = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

    // 주소-좌표 변환 객체를 생성합니다
    const geocoder = new kakao.maps.services.Geocoder();

    const markerPosition = new kakao.maps.LatLng(
      walkLocation.lat,
      walkLocation.lng
    );

    // 지도를 클릭한 위치에 표출할 마커입니다
    const marker = new kakao.maps.Marker({
      // 지도 중심좌표에 마커를 생성합니다
      // position: map.getCenter()
      position: markerPosition,
    });
    // 지도에 마커를 표시합니다
    marker.setMap(map);

    // 좌표로 법정동 상세 주소 정보를 요청하는 함수
    const searchDetailAddrFromCoords = (coords: any, callback: any) => {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    };

    // 지도 클릭했을때 마커 추가되야됨.
    kakao.maps.event.addListener(map, 'click', function (mouseEvent: any) {
      // 클릭한 위도, 경도 정보를 가져옵니다
      const latlng = mouseEvent.latLng;
      const message = `위도: ${latlng.getLat()}, 경도: ${latlng.getLng()}`;
      console.log(message);

      // state 업데이트
      setInputValue((prev) => {
        return {
          ...prev,
          walkLocation: { lat: latlng.getLat(), lng: latlng.getLng() },
        };
      });
      setIsTouched((prev) => {
        return { ...prev, location: true };
      });

      const dist = distance(
        location.lat,
        location.lng,
        latlng.getLat(),
        latlng.getLng()
      );
      if (dist > 3) {
        setIsValid((prev) => {
          return { ...prev, location: false };
        });
      }

      if (dist <= 3) {
        setIsValid((prev) => {
          return { ...prev, location: true };
        });
      }

      marker.setPosition(latlng);

      // 위도,경도를 도로명 or 지번주소로 변환
      searchDetailAddrFromCoords(
        mouseEvent.latLng,
        function (result: any, status: any) {
          if (status === kakao.maps.services.Status.OK) {
            const detailAddr = result[0].road_address
              ? result[0].road_address.address_name
              : result[0].address.address_name;

            setInputValue((prev) => {
              return { ...prev, walkAddress: detailAddr };
            });
          }
        }
      );
    });

    // 지도에 표시할 원을 생성합니다
    const circle = new kakao.maps.Circle({
      center: new kakao.maps.LatLng(location.lat, location.lng), // 원의 중심좌표 입니다
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

    // 지도에 표시할 원을 생성합니다
    const circleCenter = new kakao.maps.Circle({
      center: new kakao.maps.LatLng(location.lat, location.lng), // 원의 중심좌표 입니다
      radius: 10, // 미터 단위의 원의 지름입니다
      strokeWeight: 2, // 선의 두께입니다
      strokeColor: '#ff5180', // 선의 색깔입니다
      strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
      strokeStyle: 'solid', // 선의 스타일 입니다
      fillColor: '#ff5180', // 채우기 색깔입니다
      fillOpacity: 1, // 채우기 불투명도 입니다
    });

    // 지도에 원을 표시합니다
    circleCenter.setMap(map);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [inputValue.location]);

  return (
    <div id="map" style={{ width: '500px', height: '500px' }} ref={mapRef} />
  );
};

export default WalkMateCreateKakaoMap;
