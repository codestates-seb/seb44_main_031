import { useState, useEffect, useRef } from 'react';
import { StyledButtonPink3D } from '../../components/styles/StyledButtons';
import { styled } from 'styled-components';
import useWalkMateForm from '../../hooks/useWalkMateForm';

declare global {
  interface Window {
    kakao: any;
  }
}

const WalkMateCreate = () => {
  const {
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
  } = useWalkMateForm();
  const [map, setMap] = useState<any>();
  const [marker, setMarker] = useState<any>();
  // console.log(map, marker);
  const inputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    window.kakao.maps.load(() => {
      const container = document.getElementById('map');
      const options = {
        center: new window.kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };

      setMap(new window.kakao.maps.Map(container, options));
      setMarker(new window.kakao.maps.Marker());
    });
  }, []);

  const handleSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();
    const isAllValid =
      isValid.image &&
      isValid.title &&
      isValid.body &&
      isValid.date &&
      isValid.time &&
      isValid.attendant &&
      isValid.location;

    if (!isAllValid) {
      console.log('not valid');
      // invalid 한 태그를 찾아서 스크롤을 이동시켜야함 (현재는 임시로 title input에 지정해놨음)
      // for in 쓰면 될듯, 그러려면 ref 를 input 마다 다걸어놔야되나?
      // 헤딩 태그에 focus 를 해줘야함
      inputRef.current?.scrollIntoView({ block: 'center', behavior: 'smooth' });
      inputRef.current?.focus();
    }

    if (isAllValid) {
      console.log('POST http request is sent');
      return;
    }
  };

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
            value={inputValue.image}
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
            minLength={10}
            maxLength={30}
            required
            ref={inputRef}
            // autoFocus
          />
          {!isValid.title && isTouched.title && (
            <p className="error-message">error message</p>
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
            minLength={40}
            maxLength={500}
            // cols={5}
            required
          />
          {!isValid.body && isTouched.body && (
            <p className="error-message">error message</p>
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
              min={inputValue.date}
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
              // value="13:30"
              value={inputValue.time}
              onChange={handleTimeChange}
              required
            />
            {!isValid.time && isTouched.time && (
              <p className="error-message">
                현재 시각의 최소 30분 이후 부터 선택 가능합니다.
              </p>
            )}
          </div>
        </div>
        <div className="input-field">
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
          <label htmlFor="map">산책 모임 장소</label>
          <p className="notice-p">
            ( 본인 등록 위치의 3km 이내에서만 선택 가능 )
          </p>
          <div id="map" style={{ width: '500px', height: '400px' }}></div>
          {!isValid.location && isTouched.location && (
            <p className="error-message">error message</p>
          )}{' '}
        </div>
        <div className="btn-submit-container">
          <StyledButtonPink3D type="submit">
            <span className="span-img-container">
              <span>산책 모임 개설하기</span>
              <img
                src="/src/assets/petmily-logo-white.png"
                alt=""
                width={25}
                height={25}
              />
            </span>
          </StyledButtonPink3D>
          {<p>위의 입력 사항을 다시 한번 확인해주세요</p>}
        </div>
      </StyledForm>
    </FormDivContainer>
  );
};

export default WalkMateCreate;

// Styled Components

const FormDivContainer = styled.div`
  width: 100vw;
  /* height: 100vh; */
`;

const StyledForm = styled.form`
  width: 600px;
  /* height: 900px; */
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
    font-size: 15px;
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
    font-size: 12px;
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
    /* height: 0px; */
    margin-right: 10px;

    &:focus {
      outline: 0;
      border: 2px solid var(--pink-400);
    }
  }
`;
