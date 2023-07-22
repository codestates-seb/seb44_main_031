// import { useRef } from 'react';
import { useState, useRef } from 'react';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import { styled } from 'styled-components';
import useWalkMateForm from './hooks/useWalkMateForm';
import WalkMateCreateKakaoMap from './WalkMateCreateKakaoMap';
import { nowDateAfterSomeMinutes } from '../../utils/date-utils';
import {
  LoadingSpinner,
  SibaLoadingSpinner,
} from '../../components/styles/LoaodingSpinner';
import WalkMateSelectPetsList from './WalkMateSelectPetsList';
import {
  axiosInstance,
  isAxiosError,
  postCreateArticleUrl,
} from '../../api/walkMateAxios';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AxiosError } from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

const WalkMateCreate = () => {
  const navigate = useNavigate();

  const {
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
  } = useWalkMateForm();

  const inputRef = useRef<HTMLInputElement | null>(null);
  const [isSubmitBtnTouched, setIsSubmitBtnTouched] = useState<boolean>(false);

  const isAllValid =
    isValid.image &&
    isValid.title &&
    isValid.body &&
    isValid.date &&
    isValid.time &&
    isValid.attendant &&
    isValid.location &&
    inputValue.selectedPets.length !== 0;

  // POST 요청할 때 로딩용 상태값
  const [isLoading, setIsLoading] = useState(false);

  // onSubmit Handler: submit 될때 validatoin check, 유효성 검사 통과 안될경우 에러 메세지 띄우고 해당 input으로 focus 스크롤이동, 통과했을대만 POST 요청 보냄
  const handleSubmit = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    setIsSubmitBtnTouched(true);

    // 반려견 유효성 검사
    if (inputValue.selectedPets.length === 0) {
      return;
    }

    // 날짜 & 시간 유효성 검사
    const userInputDate = new Date(`${inputValue.date}T${inputValue.time}:00`);
    const thirtyMinAfterNow = nowDateAfterSomeMinutes(30);
    const isDateAndTimeValid = userInputDate >= thirtyMinAfterNow;

    // 시간 유효성 검사 후 통과 안되면 input scroll 로 보내기
    if (!isDateAndTimeValid) {
      inputRef?.current?.scrollIntoView({ behavior: 'smooth' });
      inputRef?.current?.focus();
      setIsValid({ ...isValid, time: false });

      return;
    }

    // 모든 유효성 검사 통과됐고, 다른 POST 요청이 없는 경우만 POST 요청을 보냄,
    if (isAllValid && isDateAndTimeValid && !isLoading) {
      // 이미지가 포함되야하기 때문에 form data 형식으로 보냄
      const formData = new FormData();

      // 이미지 데이터
      if (inputValue.image !== null) {
        formData.append('image', inputValue.image);
      }

      // json 으로 보낼 blob 데이터
      const requestData = {
        title: inputValue.title,
        body: inputValue.body,
        location: inputValue.walkAddress,
        latitude: inputValue.walkLocation.lat,
        longitude: inputValue.walkLocation.lng,
        startDate: `${inputValue.date}T${inputValue.time}`,
        endDate: inputValue.duration,
        attendant: inputValue.attendant,
        petIds: inputValue.selectedPets,
      };

      const jsonBlob = new Blob([JSON.stringify(requestData)], {
        type: 'application/json',
      });
      formData.append('request', jsonBlob);

      console.log(requestData);
      console.log([...formData]);

      try {
        setIsLoading(true);
        const response = await axiosInstance.post(
          postCreateArticleUrl,
          formData
        );

        toast.success('산책 모집 글 등록 성공!', { position: 'bottom-right' });
        navigate(`/walk-mate/${response.data.result}`);
      } catch (error: unknown | Error | AxiosError) {
        console.log(error);
        if (isAxiosError(error)) {
          if (error.response) {
            const responseData: unknown = error.response.data;
            const errorMessage: string = (responseData as { message: string })
              .message;
            // const errorMessage: string = error.response.data.message;
            const status: number = error.response.status;

            // Show the error message as a pop-up
            toast.error(`${status}: ${errorMessage}`);
          } else {
            // Handle other types of errors (e.g., network error)
            toast.error('An error occurred. Please try again later.');
          }
        } else {
          // Handle other types of errors (e.g., network error)
          toast.error('An error occurred. Please try again later.');
        }
      } finally {
        // setTimeout으로 버튼 disabled, loading spinner 되는지 가상 테스트, 나중에 setIsLoading(false); 만 남기고 지우기
        await setTimeout(() => {
          setIsLoading(false);
        }, 2000);
      }
    }
  };

  if (isPageLoading) {
    return <SibaLoadingSpinner />;
  }

  if (error) {
    return (
      <FormDivContainer>
        <StyledErrorMessage>{error}</StyledErrorMessage>
      </FormDivContainer>
    );
  }

  return (
    <FormDivContainer>
      <StyledForm onSubmit={handleSubmit}>
        <h1>🐶 산책 모임 등록하기</h1>
        <div className="input-field input-field-image">
          <label htmlFor="image">산책 모임 이미지</label>
          <input
            className="input-field-image"
            type="file"
            id="image"
            name="image"
            // value={inputValue.image}
            onChange={handleImageChange}
            accept="image/png, image/jpeg"
            required
            autoFocus
          />
          {!isValid.image && isTouched.image && (
            <p className="error-message">error message</p>
          )}
        </div>
        <div className="input-field">
          <label htmlFor="title">산책 모임 제목</label>
          <input
            className="input-field-title"
            type="text"
            id="title"
            name="title"
            placeholder="제목을 입력해 주세요."
            value={inputValue.title}
            onChange={handleTitleChange}
            size={50}
            minLength={15}
            maxLength={100}
            required
          />
          {!isValid.title && isTouched.title && (
            <p className="error-message">{`최소 15자 최대 100자 (현재 입력된 글자수: ${inputValue.title.length})`}</p>
          )}
        </div>
        <div className="input-field">
          <label htmlFor="body">산책 모임 내용</label>
          <textarea
            className="input-field-body"
            id="body"
            name="body"
            placeholder="내용을 입력해 주세요."
            value={inputValue.body}
            onChange={handleBodyChange}
            minLength={30}
            maxLength={250}
            required
          />
          {!isValid.body && isTouched.body && (
            <p className="error-message">{`최소 30자 최대 250자 (현재 입력된 글자수: ${inputValue.body.length})`}</p>
          )}
        </div>
        <div className="date-time-container">
          <div className="input-field">
            <label htmlFor="date">산책 모임 날짜</label>
            <input
              className="input-field-date"
              type="date"
              id="date"
              name="date"
              min={inputValue.initDate}
              value={inputValue.date}
              onChange={handleDateChange}
              required
            />

            {!isValid.date && isTouched.date && (
              <p className="error-message">
                이미 지난 날짜는 선택할 수 없습니다.
              </p>
            )}
          </div>
          <div className="input-field">
            <label htmlFor="time">산책 모임 시간</label>
            <input
              className="input-field-time"
              type="time"
              id="time"
              name="time"
              value={inputValue.time}
              onChange={handleTimeChange}
              required
              ref={inputRef}
            />
            {!isValid.time && isTouched.time && (
              <p className="error-message">
                현재 시각의 30분 후 부터 선택 가능합니다.
              </p>
            )}
          </div>
          <div className="input-field">
            <label htmlFor="duration">예상 소요 시간</label>
            <select
              id="duration"
              name="selectedDuration"
              className="input-field-duration"
              value={inputValue.duration}
              onChange={(e) => {
                setInputValue({
                  ...inputValue,
                  duration: e.target.value,
                });
              }}
              required
            >
              <option value={30}>30분</option>
              <option value={60}>1시간</option>
              <option value={90}>1시간 30분</option>
              <option value={120}>2시간</option>
              <option value={150}>2시간 30분</option>
              <option value={180}>3시간</option>
            </select>
          </div>
        </div>
        <p className="notice-p notice-p-date">
          ( 현재 시각의 30분 후 부터 선택 가능합니다. )
        </p>
        <div className="input-field input-attendant-container">
          <label htmlFor="attendant">산책 모임 인원</label>
          <p className="notice-p">( 본인 포함 )</p>
          <div>
            <select
              id="attendant"
              className="select-attendant"
              value={inputValue.attendant}
              onChange={handleAttendantChange}
            >
              <option value={2}>2</option>
              <option value={3}>3</option>
              <option value={4}>4</option>
            </select>
            <span>명</span>
          </div>
        </div>
        <div className="input-field">
          <div className="input-field-label-p-error">
            <div className="input-field-label-p">
              <label htmlFor="map">산책 모임 장소</label>
              <p className="notice-p">
                ( 나의 위치의 3km 이내에서만 선택 가능 )
              </p>
            </div>
            {!isValid.location && isTouched.location && (
              <p className="error-message">
                원 안의 위치만 선택하실 수 있습니다
              </p>
            )}
          </div>
          <WalkMateCreateKakaoMap
            inputValue={inputValue}
            setInputValue={setInputValue}
            setIsTouched={setIsTouched}
            setIsValid={setIsValid}
          />
        </div>
        <div className="input-field select-pets">
          <div className="input-field-label-p-error">
            <div className="input-field-label-p">
              <label htmlFor="map">산책 갈 반려견 선택</label>
              <p className="notice-p">( 최소 1마리, 최대 4마리 선택 가능 )</p>
            </div>
            {inputValue.selectedPets.length === 0 && (
              <p className="error-message">최소 1마리 이상 선택해주세요</p>
            )}
          </div>
          <WalkMateSelectPetsList
            inputValue={inputValue}
            setInputValue={setInputValue}
          />
        </div>
        <div className="btn-submit-container">
          <StyledButtonPink3D type="submit" disabled={isLoading}>
            <span className="span-img-container">
              {isLoading ? (
                <LoadingSpinner />
              ) : (
                <>
                  <span>산책 모임 개설하기</span>
                  <img
                    src="/src/assets/petmily-logo-white.png"
                    alt=""
                    width={25}
                    height={25}
                  />
                </>
              )}
            </span>
          </StyledButtonPink3D>
          {!isAllValid && isSubmitBtnTouched && (
            <p>입력 사항들을 다시 한번 확인해주세요</p>
          )}
        </div>
      </StyledForm>
    </FormDivContainer>
  );
};

export default WalkMateCreate;

// Styled Components
const FormDivContainer = styled.div`
  width: 100vw;
`;

const StyledForm = styled.form`
  width: 600px;
  margin: 60px auto;
  display: flex;
  flex-direction: column;
  gap: 15px;
  border: 1px solid var(--pink-400);
  border-radius: 20px;
  padding: 50px 50px;

  h1 {
    font-size: 26px;
    font-weight: 600;
    text-align: center;
    margin-bottom: 20px;
  }

  .input-field {
    display: flex;
    flex-direction: column;
    gap: 5px;
    label {
      align-self: flex-start;
      font-size: 16px;
    }

    input,
    textarea {
      padding: 10px;
      width: 100%;
      border: 1.5px solid var(--black-400);
      border-radius: 15px;

      &:focus {
        outline: 0;
        border: 2px solid var(--pink-400);
      }
    }

    button {
      align-self: center;
    }
  }

  .error-message {
    font-weight: 500;
    font-size: 16px;
    align-self: flex-end;
    color: var(--pink-400);
  }

  .input-field-title {
    height: 50px;
  }

  .input-field-body {
    height: 150px;
  }
  .date-time-container {
    display: flex;
    gap: 20px;

    input {
      width: 130px;
      font-size: 16px;
    }
  }

  .btn-submit-container {
    margin-top: 15px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 20px;

    button {
      width: 220px;
    }

    p {
      color: var(--pink-400);
    }
  }

  .notice-p {
    color: var(--black-800);
    font-size: 13px;
  }

  .span-img-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px;
  }

  .select-attendant {
    font-size: 16px;
    color: var(--black-800);
    padding: 12px 20px;
    border: 1.5px solid var(--black-400);
    border-radius: 15px;
    width: 130px;
    margin-right: 10px;

    &:focus {
      outline: 0;
      border: 2px solid var(--pink-400);
    }
  }

  .input-field-label-p-error {
    display: flex;
    justify-content: space-between;

    .input-field-label-p {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
  }

  .select-pets {
    margin-top: 20px;
  }

  .input-attendant-container {
    margin-top: 10px;
  }

  .input-field-duration {
    font-size: 16px;
    border: 1.5px solid var(--black-400);
    border-radius: 15px;
    padding: 10px;
    width: 100%;

    &:focus {
      outline: 0;
      border: 2px solid var(--pink-400);
    }
  }
`;

const StyledErrorMessage = styled.p`
  padding: 100px 50px;
  text-align: center;
`;
