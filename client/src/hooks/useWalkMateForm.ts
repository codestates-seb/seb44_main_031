import { useState, useEffect } from 'react';
import {
  dateToStringDate,
  dateToStringTime,
  nowDateAfterSomeMinutes,
} from '../utils/date-utils';
import { axiosInstance } from '../api/walkMateAxios';
import { useNavigate } from 'react-router-dom';

export interface inputValueType {
  image: string;
  title: string;
  body: string;
  date: string;
  initDate: string;
  time: string;
  attendant: number;
  location: { lat: number; lng: number };
  walkLocation: { lat: number; lng: number };
  walkAddress: string;
  pets: { id: number; name: string; imgUrl: string }[];
  selectedPets: number[];
}

export interface isValidType {
  image: boolean;
  title: boolean;
  body: boolean;
  date: boolean;
  time: boolean;
  attendant: boolean;
  location: boolean;
}

export interface isTouchedType {
  image: boolean;
  title: boolean;
  body: boolean;
  date: boolean;
  time: boolean;
  attendant: boolean;
  location: boolean;
  pets: boolean;
}

const useWalkMateForm = () => {
  // input 값
  // onChange Handler: input 값 binding, validation check
  // onBlur Handler: blur 될때 validation 일어나야되 (필요없는거 같은데)
  // onSubmit Handler: submit 될때 validatoin check, 해당 input으로 focus 스크롤이동, 통과되면 POST 요청보내
  // error 메세지, isTouched 가 ture && !isValid 일때 에러 메세지 띄워주면되
  // 성공적으로 submit 하면 초기 state 값으로 reset 해야되 (어차피 페이지 navigate 되니까 상관없을듯)
  const navigate = useNavigate();

  const [inputValue, setInputValue] = useState<inputValueType>({
    image: '',
    title: '',
    body: '',
    date: '',
    initDate: '',
    time: '',
    attendant: 4,
    // location: { lat: 37.5796, lng: 126.977 },
    // walkLocation: { lat: 37.5796, lng: 126.977 },
    location: { lat: 33.450701, lng: 126.570667 },
    walkLocation: { lat: 33.450701, lng: 126.570667 },
    walkAddress: '',
    pets: [{ id: 1, name: '', imgUrl: '' }],
    selectedPets: [],
  });

  const [isValid, setIsValid] = useState<isValidType>({
    image: false,
    title: false,
    body: false,
    date: true,
    time: true,
    attendant: true,
    location: true,
  });

  const [isTouched, setIsTouched] = useState({
    image: false,
    title: false,
    body: false,
    date: false,
    time: false,
    attendant: false,
    location: false,
    pets: false,
  });

  const [isPageLoading, setIsPageLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Date initial value
    const thirtyMinLater = nowDateAfterSomeMinutes(30);
    const stringFormatDate = dateToStringDate(thirtyMinLater);
    const stringFormatTime = dateToStringTime(thirtyMinLater);

    setInputValue((prev) => {
      return { ...prev, initDate: stringFormatDate, date: stringFormatDate };
    });
    setInputValue((prev) => {
      return { ...prev, time: stringFormatTime };
    });

    // GET 요청보내서 사용자 위치 정보, 강아지정보, 로그인여부 받아와야함.
    const fetchUserData = async () => {
      try {
        setIsPageLoading(true);
        const response = await axiosInstance.get('articles-info');
        const data = response.data;
        setInputValue((prev) => {
          return {
            ...prev,
            pets: data.result.pets,
            location: { lat: data.result.latitude, lng: data.result.longitude },
            walkLocation: {
              lat: data.result.latitude,
              lng: data.result.longitude,
            },
          };
        });
      } catch (err: any) {
        // 토큰 인증이 안됐을 경우 로그인 페이지로 이동
        if (err.status === 401) {
          navigate('/users/sign-in', { state: { path: '/walk-mate/create' } });
        }
        setError(err.message);
        console.log(err.message);
      } finally {
        setIsPageLoading(false);
      }
    };
    fetchUserData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleImageChange = (e: React.FormEvent<HTMLInputElement>) => {
    setInputValue({ ...inputValue, image: e.currentTarget.value });
    setIsTouched({ ...isTouched, image: true });

    // valid: 꼭 한개 이상의 파일이 담겨있어야되 = 초기 value 값이 아니어야되 ''
    const isImageValid = e.currentTarget.value !== '';
    if (!isImageValid) {
      setIsValid({ ...isValid, image: false });
    }
    if (isImageValid) {
      setIsValid({ ...isValid, image: true });
    }
  };

  const handleTitleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    setInputValue({ ...inputValue, title: e.currentTarget.value });
    setIsTouched({ ...isTouched, title: true });

    const isTitleValid =
      e.currentTarget.value.trim().length >= 15 &&
      e.currentTarget.value.trim().length <= 100;
    if (!isTitleValid) {
      setIsValid({ ...isValid, title: false });
    }
    if (isTitleValid) {
      setIsValid({ ...isValid, title: true });
    }
  };

  const handleBodyChange: React.ChangeEventHandler<HTMLTextAreaElement> = (
    e
  ) => {
    setInputValue({ ...inputValue, body: e.currentTarget.value });
    setIsTouched({ ...isTouched, body: true });

    const isBodyValid =
      e.currentTarget.value.trim().length >= 30 &&
      e.currentTarget.value.trim().length <= 250;
    if (!isBodyValid) {
      setIsValid({ ...isValid, body: false });
    }
    if (isBodyValid) {
      setIsValid({ ...isValid, body: true });
    }
  };

  const handleDateChange = (e: React.FormEvent<HTMLInputElement>) => {
    setInputValue({ ...inputValue, date: e.currentTarget.value });
    setIsTouched({ ...isTouched, date: true });

    // valid: 작성하는 시점에 지정한 날짜가 꼭 작성하는 시점으로부터 now+ 30min 의 날짜어야되.
    // input 에 min 속성으로 이미 지정되있음. 근데 엣지케이스 있음.
    // 엣지케이스 ex) 11시 20분에 화면에 들어왔어, 7월7일 23시50분으로 설정했어.
    // 근데 작성하다 보니 11시 40분이 되서 최소 날짜가 7월8일 00시10분 이후어야되.
    // 그렇기 때문에 여기(HandleChnage)나,onSubmit 할때 한번 더 유효성 검사해줘야되.
    const inputDate = e.currentTarget.value;
    const dateAfterThirtyMin = dateToStringDate(nowDateAfterSomeMinutes(30));

    // 시간을 수정하지 않고 날짜만 수정한 경우라도 날짜와 시간 두개 동시에 검사해줘야함.
    // 왜냐면 초기값에 시간을 수정하지 않고 날짜만 바꿨을경우, 유효한 Date 이지만 통과되지 않는 엣지케이스 발생

    // 날짜 유효성 검사
    const isDateValid = inputDate >= dateAfterThirtyMin;
    if (!isDateValid) {
      setIsValid({ ...isValid, date: false });
    }
    if (isDateValid) {
      setIsValid({ ...isValid, date: true });
    }

    // 시간 유효성 검사
    const userInputDate = new Date(
      `${e.currentTarget.value}T${inputValue.time}:00`
    );
    const thirtyMinAfterNow = nowDateAfterSomeMinutes(30);
    const isDateAndTimeValid = userInputDate >= thirtyMinAfterNow;

    if (!isDateAndTimeValid) {
      setIsValid({ ...isValid, time: false });
    }
    if (isDateAndTimeValid) {
      setIsValid({ ...isValid, time: true });
    }
  };

  const handleTimeChange = (e: React.FormEvent<HTMLInputElement>) => {
    setInputValue({ ...inputValue, time: e.currentTarget.value });
    setIsTouched({ ...isTouched, time: true });

    // valid: 작성하는 시점에 지정한 시간이 꼭 작성하는 시점으로부터 최소 30분 이후여야되.
    // 그렇기 때문에 여기(HandleChnage)나,onSubmit 할때 한번 더 유효성 검사해줘야되.

    // 근데 시간 유효성은 어떻게 판단하지? 12시가 넘어가면 단순히 시간크기 비교할수없는데 시간분만 비교할 수가 없는데.
    // 엣지케이스 ex) 11시 20분에 화면에 들어왔어, 7월7일 23시50분으로 설정했어.
    // 근데 작성하다 보니 11시 40분이 되서 최소 날짜가 7월8일 00시10분 이후어야되.
    // 전체 시간 + 30으로 비교해야겠다.
    // 유저가 입력한 Date 과 현재 시각 + 30분의 Date 을 비교하면 됨
    const userInputDate = new Date(
      `${inputValue.date}T${e.currentTarget.value}:00`
    );
    const thirtyMinAfterNow = nowDateAfterSomeMinutes(30);
    const isDateAndTimeValid = userInputDate >= thirtyMinAfterNow;

    if (!isDateAndTimeValid) {
      setIsValid({ ...isValid, time: false });
    }
    if (isDateAndTimeValid) {
      setIsValid({ ...isValid, time: true });
    }
  };

  const handleAttendantChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setInputValue({ ...inputValue, attendant: Number(e.currentTarget.value) });
    setIsTouched({ ...isTouched, attendant: true });

    //valid: 어떤걸 선택해도 되서 인원은 무조건 ture 임.
  };

  return {
    inputValue,
    setInputValue,
    isValid,
    setIsValid,
    isTouched,
    setIsTouched,
    isPageLoading,
    error,
    handleImageChange,
    handleTitleChange,
    handleBodyChange,
    handleDateChange,
    handleTimeChange,
    handleAttendantChange,
  };
};

export default useWalkMateForm;
