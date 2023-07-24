import { useState, useEffect } from 'react';
import {
  dateToStringDate,
  dateToStringTime,
  nowDateAfterSomeMinutes,
} from '../../../utils/date-utils';
import { axiosInstance, getCreateArticleUrl } from '../../../api/walkMateAxios';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AxiosError, isAxiosError } from 'axios';
import { toast } from 'react-toastify';
import { MAXIMUM_IMAGE_SIZE } from '../../../constants/fileSize';

export interface FormDatas {
  image: File | null;
  title: string;
  body: string;
  date: string;
  initDate: string;
  time: string;
  duration: string;
  attendant: number;
  location: { lat: number; lng: number };
  walkLocation: { lat: number; lng: number };
  walkAddress: string;
  pets: { id: number; name: string; imgUrl: string }[];
  selectedPets: number[];
}

export interface Validations {
  image: boolean;
  title: boolean;
  body: boolean;
  date: boolean;
  time: boolean;
  duration: boolean;
  attendant: boolean;
  location: boolean;
}

export interface IsTouched {
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
  // error 메세지, isTouched 가 ture && !isValid 일때만 에러 메세지 띄워줌
  // 성공적으로 submit 하면 초기 state 값으로 reset 해야함 (근데 어차피 페이지 navigate 되니까 상관없을듯)

  const [formDatas, setFormDatas] = useState<FormDatas>({
    image: null,
    title: '',
    body: '',
    date: '',
    initDate: '',
    time: '',
    duration: '60',
    attendant: 4,
    location: { lat: 33.450701, lng: 126.570667 },
    walkLocation: { lat: 33.450701, lng: 126.570667 },
    walkAddress: '',
    pets: [{ id: 1, name: '', imgUrl: '' }],
    selectedPets: [],
  });

  const [validations, setValidations] = useState<Validations>({
    image: false,
    title: false,
    body: false,
    date: true,
    time: true,
    duration: true,
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
  const [error, setError] = useState<null | string>(null);

  useEffect(() => {
    // GET 요청보내서 사용자 위치 정보, 강아지정보, 로그인여부 받아와야함.
    const fetchUserData = async () => {
      try {
        setIsPageLoading(true);
        const response = await axiosInstance.get(getCreateArticleUrl, {
          headers: {
            Authorization: localStorage.getItem('accessToken'),
          },
        });
        const data = response.data;
        setFormDatas((prev) => {
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
      } catch (error: unknown | Error | AxiosError) {
        if (isAxiosError(error)) {
          if (error.response) {
            const errorMessage: string = error.response.data.message;
            const status: number = error.response.status;

            // Show the error message as a pop-up
            toast.error(`${status}: ${errorMessage}`);
            setError(`${status}: ${errorMessage}`);
          } else {
            // Handle other types of errors (e.g., network error)
            toast.error(`${error.message}`);
            setError(`${error.message}`);
          }
        } else {
          // Handle other types of errors (e.g., network error)
          toast.error('An error occurred. Please try again later.');
          setError('An error occurred. Please try again later.');
        }
      } finally {
        setIsPageLoading(false);
      }
    };
    fetchUserData();

    // 유저가 페이지에 접속한 시점의 Date initial value
    const fourtyMinLater = nowDateAfterSomeMinutes(40);
    const stringFormatDate = dateToStringDate(fourtyMinLater);
    const stringFormatTime = dateToStringTime(fourtyMinLater);

    setFormDatas((prev) => {
      return {
        ...prev,
        initDate: stringFormatDate,
        date: stringFormatDate,
        time: stringFormatTime,
      };
    });

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleImageChange = (e: React.FormEvent<HTMLInputElement>) => {
    const input = e.currentTarget;
    const file = input?.files && input.files[0];

    // 이미지 파일 1MB 이하만 업로드 가능
    if (file && file.size > MAXIMUM_IMAGE_SIZE) {
      setFormDatas({ ...formDatas, image: null });
      setIsTouched({ ...isTouched, image: true });
      setValidations({ ...validations, image: false });
      alert('1MB 이하의 이미지만 올릴 수 있습니다');
      // Clear the file input to prevent submitting the file
      return;
    }

    if (e.currentTarget.files !== null) {
      setFormDatas({ ...formDatas, image: e.currentTarget?.files[0] });
      setIsTouched({ ...isTouched, image: true });
    }
    // valid: 꼭 한개 이상의 파일이 담겨있어야함: input value 값이 '' 빈문자열이 아니어야함.
    const isImageValid = e.currentTarget.files?.length;
    if (isImageValid) {
      setValidations({ ...validations, image: true });
    } else if (!isImageValid) {
      setValidations({ ...validations, image: false });
    }
  };

  const handleTitleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    setFormDatas({ ...formDatas, title: e.currentTarget.value });
    setIsTouched({ ...isTouched, title: true });

    const isTitleValid =
      e.currentTarget.value.trim().length >= 15 &&
      e.currentTarget.value.trim().length <= 100;
    if (isTitleValid) {
      setValidations({ ...validations, title: true });
    } else if (!isTitleValid) {
      setValidations({ ...validations, title: false });
    }
  };

  const handleBodyChange: React.ChangeEventHandler<HTMLTextAreaElement> = (
    e
  ) => {
    setFormDatas({ ...formDatas, body: e.currentTarget.value });
    setIsTouched({ ...isTouched, body: true });

    const isBodyValid =
      e.currentTarget.value.trim().length >= 30 &&
      e.currentTarget.value.trim().length <= 250;
    if (!isBodyValid) {
      setValidations({ ...validations, body: false });
    }
    if (isBodyValid) {
      setValidations({ ...validations, body: true });
    }
  };

  const handleDateChange = (e: React.FormEvent<HTMLInputElement>) => {
    setFormDatas({ ...formDatas, date: e.currentTarget.value });
    setIsTouched({ ...isTouched, date: true });

    // valid: 작성하는 시점에 지정한 날짜가 작성하는 시점 + 30min 의 날짜여야됨.
    // input 에 min 속성으로 선택 가능 날짜가 이미 지정되있음. 하지만 엣지케이스 대비해야함.

    // 엣지케이스 ex) 7월7일 11시 29분에 화면에 들어왔을때, 기본값이 7월7일 23시59분으로 설정돼있는데,
    // 작성하다 보니 11시 31분이 되어서 최소 가능 날짜&시간이 7월8일 00시01분 이후어야되.
    // 그렇기 때문에 여기(HandleChnage)나,onSubmit 할때 한번 더 유효성 검사해줘야함.

    // 또 시간을 수정하지 않고 날짜만 수정한 경우라도 날짜와 시간 두개 동시에 검사해줘야함.
    // 날짜만 수정했다고 날짜만 검사하게 되면, 시간을 초기값으로 두고 수정하지 않고 날짜만 바꿨을 경우, 유효한 Date 이지만 통과되지 않는 엣지케이스가 발생

    const inputDate = e.currentTarget.value;
    const dateAfterThirtyMin = dateToStringDate(nowDateAfterSomeMinutes(30));

    // 날짜 유효성 검사
    const isDateValid = inputDate >= dateAfterThirtyMin;
    if (!isDateValid) {
      setValidations({ ...validations, date: false });
    }
    if (isDateValid) {
      setValidations({ ...validations, date: true });
    }

    // 시간 유효성 검사
    const userInputDate = new Date(
      `${e.currentTarget.value}T${formDatas.time}:00`
    );
    const thirtyMinAfterNow = nowDateAfterSomeMinutes(30);
    const isDateAndTimeValid = userInputDate >= thirtyMinAfterNow;

    if (!isDateAndTimeValid) {
      setValidations({ ...validations, time: false });
    }
    if (isDateAndTimeValid) {
      setValidations({ ...validations, time: true });
    }
  };

  const handleTimeChange = (e: React.FormEvent<HTMLInputElement>) => {
    setFormDatas({ ...formDatas, time: e.currentTarget.value });
    setIsTouched({ ...isTouched, time: true });

    // valid: 작성하는 시점에 지정한 시간이 꼭 작성하는 시점으로부터 최소 30분 이후여야함.
    // 그래서 handleTimeChange(여기), onSubmit 할때 한번 더 유효성 검사해줘야함.

    // 시간 유효성 판단 기준
    // 엣지케이스 ex) 7월7일 11시 29분에 화면에 들어왔을때, 기본값이 7월7일 23시59분으로 설정돼있는데,
    // 작성하다 보니 11시 31분이 되어서 최소 가능 날짜&시간이 7월8일 00시01분 이후어야되.
    // 그러므로 시간만 보면 안되고 날짜와 시간을 합친 Date 전체 시간기준 + 30으로 계산해야함.
    // 유저가 입력한 Date 과 현재 시각 30분 후의 Date 을 비교하면 됨
    const userInputDate = new Date(
      `${formDatas.date}T${e.currentTarget.value}:00`
    );
    const thirtyMinAfterNow = nowDateAfterSomeMinutes(30);
    const isDateAndTimeValid = userInputDate >= thirtyMinAfterNow;

    if (!isDateAndTimeValid) {
      setValidations({ ...validations, time: false });
    }
    if (isDateAndTimeValid) {
      setValidations({ ...validations, time: true });
    }
  };

  const handleAttendantChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFormDatas({
      ...formDatas,
      attendant: Number(e.currentTarget.value),
    });
    setIsTouched({ ...isTouched, attendant: true });

    //valid: 어떤걸 선택해도 되서 인원은 무조건 ture 임.
  };

  return {
    inputValue: formDatas,
    setInputValue: setFormDatas,
    isValid: validations,
    setIsValid: setValidations,
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
