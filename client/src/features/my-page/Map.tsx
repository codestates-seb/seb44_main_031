import { styled } from 'styled-components';
import { useEffect, useState } from 'react';
import { useAppDispatch, RootState } from '../../store/store';
import { fetchUsers } from './myPageSlice';
import { useSelector } from 'react-redux';

const Container = styled.div`
  /* Styles omitted for brevity */
`;

const Contents = styled.div`
  /* Styles omitted for brevity */
`;

const InputAddress = styled.div`
  /* Styles omitted for brevity */
  #map {
    z-index: 1;
    position: relative;
  }
`;

const Map = () => {
  const [address, setAddress] = useState('');
  const [latitude, setLatitude] = useState(0);
  const [longitude, setLongitude] = useState(0);
  const dispatch = useAppDispatch();
  const profile = useSelector((state: RootState) => state.mypage.profile);

  useEffect(() => {
    setAddress(profile.address);
    setLatitude(profile.latitude);
    setLongitude(profile.longitude);
    console.log(address); // FEEDBACK: 불필요한 console.log는 지워주세요.
    console.log(latitude);
    console.log(longitude);
  }, [address, latitude, longitude, profile]);

  useEffect(() => {
    // FEEDBACK: localStorage를 추상화 하는건 어떨까요. (다른 피드백 참고)
    // Number로 형변환 해주는 로직도 추상화된 localStorage 내부에 있는게 좋겠습니다.
    dispatch(fetchUsers(Number(localStorage.getItem('userId'))));
  }, [dispatch]);

  // FEEDBACK: Form.tsx와 중복되는 코드 입니다. 중복을 제거해주세요.
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
      'https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=2e9c72e22b8b9402a65bbc568e1d75b1',
    );

    my_script.then(() => {
      const kakao = (window as any)['kakao'];
      kakao.maps.load(() => {
        const mapContainer = document.getElementById('map');
        const options = {
          center: new kakao.maps.LatLng(latitude, longitude),
          level: 3,
        };
        const map = new kakao.maps.Map(mapContainer, options);
        const markerPosition = new kakao.maps.LatLng(latitude, longitude);
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(map);
      });
    });
  }, [latitude, longitude]);

  return (
    <Container>
      <Contents>
        <InputAddress>
          <div
            id="map"
            style={{
              width: '200px',
              height: '200px',
              position: 'sticky',
              zIndex: '1',
            }}
            className="map/"
          ></div>
        </InputAddress>
      </Contents>
    </Container>
  );
};

export default Map;
