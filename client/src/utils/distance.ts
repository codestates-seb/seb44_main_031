// 두 좌표(위도, 경도) 사이의 거리를 km 단위로 계산해주는 알고리즘 함수 (영탁)

export function distance(
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
) {
  const R = 6371; // 지구 반지름 (단위: km)
  const dLat = deg2rad(lat2 - lat1);
  const dLon = deg2rad(lon2 - lon1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(deg2rad(lat1)) *
      Math.cos(deg2rad(lat2)) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const distance = R * c; // 두 지점 간의 거리 (단위: km)
  return distance;
}

export function deg2rad(deg: number) {
  return deg * (Math.PI / 180);
}

// 예시: 서울과 부산 간의 거리를 계산합니다.
// const seoulLat = 37.5665;
// const seoulLon = 126.978;
// const busanLat = 35.1796;
// const busanLon = 129.0756;
// const dist = distance(seoulLat, seoulLon, busanLat, busanLon);
// console.log(dist);
// 출력: 325.4961911409085
