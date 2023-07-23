export const FE_BASE_URL = 'http://localhost:5173';

// FEEDBACK: 변수명의 공통 부분을 postfix로 두면 검색할 때 어려움이 있을 수 있습니다.
// 예를 들어, myPageUrl을 검색할 때 Url인건 알지만 다른 이름은 모른다고 해보겠습니다.
// Url로 검색을 하면 myPageUrl 뿐만 아니라 ...Url..., Url... 등 많은 결과가 나오게 됩니다.
// 반면 UrlMyPage로 변수명을 설정하면 Url로 검색을 시작했을 때 Url...로 시작하는 변수명만 나오게 되고 금방 찾을 수 있습니다.
// 그래서 아래 변수명들을 모두 Url로 시작하게 변경하는게 좋을 거 같습니다.

// FEEDBACK: 상수화된 변수들은 대문자로 작성하는게 좋습니다.
// createWalkMateUrl -> CREATE_WALK_MATE_URL

export const createWalkMateURL = '/walk-mate/create'; // FEEDBACK: 변수명은 명사로 설정하는게 좋습니다.
export const myPageUrl = '/users/mypage';
export const walkMateAllUrl = '/walk-mate/all';
export const signUpUrl = '/users/sign-up';
export const signInUrl = '/users/sign-in';
export const getWalkMateDetailUrl = (articleId: number) =>
  `/walk-mate/${articleId}`;
export const secondHandUrl = '/second-hand-item';
export const petShopUrl = '/pet-shop';

// FEEDBACK: 종합하면 아래와 같은 결과가 됩니다.
export const URL_CREATE_WALK_MATE = '/walk-mate/create';
export const URL_WALK_MATE_DETAIL = (articleId: number) => `/walk-mate/${articleId}`; // FEEDBACK: 그리고 관련 있는 것끼리 묶어두면 살펴볼 때 좋습니다.

export const URL_MY_PAGE = '/users/mypage';
export const URL_WALK_MATE_ALL = '/walk-mate/all';
export const URL_SIGN_UP = '/users/sign-up';
export const URL_SIGN_IN = '/users/sign-in';
export const URL_SECOND_HAND = '/second-hand-item';
export const URL_PET_SHOP = '/pet-shop';
