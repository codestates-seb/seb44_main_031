import { useState, useEffect } from 'react';
import { styled } from 'styled-components';

declare global {
  interface Window {
    kakao: any;
  }
}

const WalkMateCreate = () => {
  const [map, setMap] = useState<any>();
  const [marker, setMarker] = useState<any>();
  console.log(map, marker);

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

  return (
    <FormDivContainer>
      <StyledForm>
        <h1>🐶 산책 모임 등록하기</h1>
        <div className="input-field input-field-image">
          <label htmlFor="image">산책 모임 이미지</label>
          <input
            className="input-field-image"
            type="file"
            id="image"
            name="image"
            placeholder="제목을 입력해 주세요."
            accept="image/png, image/jpeg"
            required
          />
          <p className="error-message">error message</p>
        </div>
        <div className="input-field">
          <label htmlFor="title">산책 모임 제목</label>
          <input
            className="input-field-title"
            type="text"
            id="title"
            name="title"
            placeholder="제목을 입력해 주세요."
            size={50}
            minLength={5}
            maxLength={30}
            required
            autoFocus
          />
          <p className="error-message">error message</p>
        </div>
        <div className="input-field">
          <label htmlFor="content">산책 모임 내용</label>
          <textarea
            className="input-field-content"
            id="content"
            name="content"
            placeholder="내용을 입력해 주세요."
            minLength={20}
            maxLength={500}
            // cols={5}
            required
          />
          <p className="error-message">error message</p>
        </div>
        <div className="date-time-container">
          <div className="input-field">
            <label htmlFor="date">산책 모임 날짜</label>
            <input
              className="input-field-date"
              type="date"
              id="date"
              name="date"
              min="2023-07-07"
              value="2023-07-07"
              required
            />
            <p className="error-message">error message</p>
          </div>
          <div className="input-field">
            <label htmlFor="time">산책 모임 시간</label>
            <input
              className="input-field-time"
              type="time"
              id="time"
              name="time"
              value="13:30"
              required
            />
            <p className="error-message">error message</p>
          </div>
        </div>
        <div className="input-field">
          <label htmlFor="map">산책 모임 장소</label>
          <p className="map-notice-p">
            ( 본인 등록 위치의 3km 이내에서만 선택 가능 )
          </p>
          <div id="map" style={{ width: '500px', height: '400px' }}></div>
          <p className="error-message">error message</p>
        </div>
        <div className="btn-submit-container">
          <StyledPinkButton3D type="button" role="button">
            산책 모임 개설하기 🐾
          </StyledPinkButton3D>
        </div>
      </StyledForm>
    </FormDivContainer>
  );
};

export default WalkMateCreate;

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
    align-self: flex-end;
    color: var(--pink-400);
  }

  .input-field-title {
    height: 50px;
  }

  .input-field-content {
    height: 150px;
  }
  .date-time-container {
    display: flex;
    gap: 20px;

    input {
      width: 130px;
    }
  }

  .btn-submit-container {
    margin-top: 15px;
    display: flex;
    justify-content: center;
  }

  .map-notice-p {
    color: var(--black-800);
    font-size: 12px;
  }
`;

const StyledPinkButton3D = styled.button`
  background-color: var(--pink-400);
  border: 2px solid var(--pink-400);
  border-radius: 30px;
  box-shadow: var(--pink-100) 4px 4px 0 0;
  color: white;
  cursor: pointer;
  display: inline-block;
  font-weight: 600;
  font-size: 16px;
  padding: 0 18px;
  line-height: 40px;
  text-align: center;
  text-decoration: none;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;

  &:hover {
    background-color: rgb(255, 84, 141);
    border: 2px solid rgb(255, 84, 141);
    /* color: black; */
  }

  &:active {
    box-shadow: var(--pink-300) 2px 2px 0 0;
    transform: translate(2px, 2px);
  }

  @media (min-width: 768px) {
    & {
      min-width: 120px;
      padding: 0 25px;
    }
  }
`;

// const StyeldButton = styled.button`
//   font-weight: bold;
//   padding: 5px;
//   color: white;
//   background-color: var(--pink-400);
//   height: 40px;
//   width: 100px;
//   cursor: pointer;

//   border: none;
//   border-radius: 15px;
// `;
