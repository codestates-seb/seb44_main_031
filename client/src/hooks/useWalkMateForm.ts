import { useState, useEffect } from 'react';
import {
  dateToStringDate,
  dateToStringTime,
  nowDateAfterSomeMinutes,
} from '../utils/date-utils';

const useWalkMateForm = () => {
  // input 값
  // onChange Handler: input 값 binding, validation check
  // onBlur Handler: blur 될때 validation 일어나야되 (필요없는거 같은데)
  // onSubmit Handler: submit 될때 validatoin check, 해당 input으로 focus 스크롤이동, 통과되면 POST 요청보내
  // error 메세지, isTouched 가 ture && !isValid 일때 에러 메세지 띄워주면되
  // 성공적으로 submit 하면 초기 state 값으로 reset 해야되 (어차피 페이지 navigate 되니까 상관없을듯)

  const [inputValue, setInputValue] = useState({
    image: '',
    title: '',
    body: '',
    date: '',
    time: '',
    attendant: 4,
    location: { lat: '', long: '' },
  });
  // console.log(inputValue);

  const [isValid, setIsValid] = useState({
    image: false,
    title: false,
    body: false,
    date: false,
    time: false,
    attendant: true,
    location: false,
  });

  const [isTouched, setIsTouched] = useState({
    image: false,
    title: false,
    body: false,
    date: false,
    time: false,
    attendant: false,
    location: false,
  });

  useEffect(() => {
    const thirtyMinLater = nowDateAfterSomeMinutes(30);
    const stringFormatDate = dateToStringDate(thirtyMinLater);
    const stringFormatTime = dateToStringTime(thirtyMinLater);

    setInputValue((prev) => {
      return { ...prev, date: stringFormatDate };
    });
    setInputValue((prev) => {
      return { ...prev, time: stringFormatTime };
    });
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
      e.currentTarget.value.trim().length >= 10 &&
      e.currentTarget.value.trim().length <= 30;
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
      e.currentTarget.value.trim().length >= 40 &&
      e.currentTarget.value.trim().length <= 500;
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

    const isDateValid = inputDate === dateAfterThirtyMin;
    if (!isDateValid) {
      setIsValid({ ...isValid, date: false });
    }
    if (isDateValid) {
      setIsValid({ ...isValid, date: true });
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

  const handleLocationChange = (e: any) => {
    // location 은 input 이 아닌데 onChange event에 value 랑 touched, valid 여부를 어떻게 확인하지?

    setInputValue({ ...inputValue, location: e.currentTarget.value });
    setIsTouched({ ...isTouched, location: true });
    // valid: 산책모임을 지정한 장소가 본인 주소로부터 3km 이내여야되.
  };

  // const handleSubmit = (e: React.SyntheticEvent, ref: HTMLDivElement) => {
  //   e.preventDefault();
  //   const isAllValid =
  //     isValid.image &&
  //     isValid.title &&
  //     isValid.body &&
  //     isValid.date &&
  //     isValid.time &&
  //     isValid.attendant &&
  //     isValid.location;

  //   if (!isAllValid) {
  //     console.log('not valid');
  //     ref.scrollIntoView({ behavior: 'smooth' });
  //   }

  //   if (isAllValid) {
  //     console.log('POST http request is sent');
  //     return;
  //   }
  // };

  return {
    inputValue,
    isValid,
    isTouched,
    handleImageChange,
    handleTitleChange,
    handleBodyChange,
    handleDateChange,
    handleTimeChange,
    handleAttendantChange,
    handleLocationChange,
  };
};

export default useWalkMateForm;
