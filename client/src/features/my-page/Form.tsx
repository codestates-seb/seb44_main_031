import { styled } from 'styled-components';
import { useEffect, useCallback, useState } from 'react';
import { useDaumPostcodePopup } from 'react-daum-postcode';
import useDidMountEffect from './useDidMountEffect';
import { fetchAddress } from './myPageSlice';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../../store/store';
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f1f2f3;
  font-size: 0.8rem;
  form {
    width: 30em;
  }
  #map {
    height: 600px;
  }
`;
const Contents = styled.div`
  display: flex;
  flex-direction: column;
  width: 30em;
`;

const InputAddress = styled.div`
  display: flex;
  flex-direction: column;
  margin: 6px 0 6px;

  > div {
    text-align: left;
    margin: 2px 0 2px;
    padding: 0 2px;
    font-size: 1rem;
    font-weight: 800;
  }

  > input {
    margin: 2px 0 2px;
    border: 1px solid #babfc4;
    border-radius: 3px;
    padding: 0.6em 0.7em;
    color: #0c0d0e;
  }
`;

const Form = () => {
  const [address, setAddress] = useState('');
  const [latitude, setLatitude] = useState(0);
  const [longitude, setLongitude] = useState(0);
  const [map, setMap] = useState<any>();
  const [marker, setMarker] = useState<any>();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const open = useDaumPostcodePopup(
    'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
  );

  const handleComplete = (data: any) => {
    setAddress(data.address);
    //여기서 goaddress할필요가 없음 유즈이펙트땜
  };
  const handleClick = () => {
    open({ onComplete: handleComplete });
  };
  const new_script = (src: string) => {
    return new Promise<void>((resolve, reject) => {
      const script = document.createElement('script');
      script.src = src;
      script.addEventListener('load', () => {
        resolve();
      });
      script.addEventListener('error', (e) => {
        reject(e);
      });
      document.head.appendChild(script);
    });
  };
  useEffect(() => {
    const my_script = new_script(
      'https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=2e9c72e22b8b9402a65bbc568e1d75b1'
    );
    //스크립트 읽기 완료 후 카카오맵 설정
    my_script.then(() => {
      const kakao = (window as any)['kakao'];
      kakao.maps.load(() => {
        const mapContainer = document.getElementById('map');
        const options = {
          center: new kakao.maps.LatLng(37.56000302825312, 126.97540593203321), //좌표설정
          level: 3,
        };
        const map = new kakao.maps.Map(mapContainer, options); //맵생성
        //마커설정
        const markerPosition = new kakao.maps.LatLng(
          37.56000302825312,
          126.97540593203321
        );
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(map);
      });
    });
  }, []);

  /// mapContainer = container
  //map == map
  const goAddress = useCallback(() => {
    if (address === '') {
      return;
    }
    const kakao = (window as any)['kakao'];
    kakao.maps.load(() => {
      const mapContainer = document.getElementById('map');
      const options = {
        center: new kakao.maps.LatLng(37.55, 126.97540593203321), //좌표설정
        level: 3,
      };
      const map = new kakao.maps.Map(mapContainer, options); //맵생성
      //마커설정
      const geocoder = new kakao.maps.services.Geocoder();
      // 주소로 좌표를 검색합니다..
      //현재 새주소만 인정되는 문제가 있음
      geocoder.addressSearch(address, function (result: any[], status: any) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          setLatitude(result[0].y);
          setLongitude(result[0].x);
          // 결과값으로 받은 위치를 마커로 표시합니다
          const marker = new kakao.maps.Marker({
            map: map,
            position: coords,
          });
          // 인포윈도우로 장소에 대한 설명을 표시합니다
          const infowindow = new kakao.maps.InfoWindow({
            content:
              `<div style="width:150px;black:yellow;text-align:center;padding:6px 0;">선택한 위치</div>`,
          });
          infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
          marker.setMap(map);
          setMap(map);
          setMarker(marker);
        }
      });
    });
  }, [address, latitude, longitude]);
  useEffect(() => {
    goAddress();
  }, [goAddress]);

  const handleMapClick = useCallback(
    (mouseEvent: any) => {
      const kakao = window.kakao;
      const geocoder = new kakao.maps.services.Geocoder();

      geocoder.coord2Address(
        mouseEvent.latLng.getLng(),
        mouseEvent.latLng.getLat(),
        (result: any, status: any) => {
          if (status === kakao.maps.services.Status.OK) {
            const address = result[0].road_address
              ? result[0].road_address.address_name
              : result[0].address.address_name;
            setAddress(address);
            marker.setMap(null);
            marker.setPosition(mouseEvent.latLng);
            marker.setMap(map);
            setLatitude(mouseEvent.latLng.getLat());
            setLongitude(mouseEvent.latLng.getLng());
          }
        }
      );
    },
    [latitude, map, marker]
  );

  useDidMountEffect(() => {
    if (map && marker) {
      const kakao = (window as any)['kakao'];
      kakao.maps.event.addListener(map, 'click', handleMapClick);
    }
  }, [map, marker, handleMapClick]);

  const handleAddressModifyClick = useCallback(
    async (e: React.FormEvent) => {
      e.preventDefault();
      dispatch(fetchAddress({ address, latitude, longitude })).then(
        () => {
         
          
            alert('주소를 변경했습니다');
          
          navigate('/users/mypage');
          window.location.reload();
        }
      );
    },
    [dispatch, address, latitude, longitude, navigate]
  );
  return (
    <Container>
      <Contents>
        <InputAddress>
          <label htmlFor="address">
            <p>1. 우선 바꾸고 싶은 주소를 검색합니다</p>
            <p>2.세부주소를 클릭으로 변경합니다.</p>
            <p>3. 완료를 누르면 주소변경이 완료 됩니다.</p>
          </label>
          <input
            type="address"
            id="address"
            placeholder="숭례문 도로명"
            value={address}
            readOnly
          ></input>

          <button type="button" onClick={handleClick}>
            주소검색하기
          </button>
          <button type="button" onClick={handleAddressModifyClick}>
            주소변경완료
          </button>
          <div id="map" className="map/"></div>
        </InputAddress>
      </Contents>
    </Container>
  );
};

export default Form;
