import React, { PropsWithChildren } from 'react';
import styled from 'styled-components';

interface ModalDefaultType {
  onClickToggleAddPetModal: () => void;
}
//프롭스 드릴링이 머였드라
//레이 쾌스트
function AddPetModal({
  onClickToggleAddPetModal,
  children,
}: PropsWithChildren<ModalDefaultType>) {
  return (
    <ModalContainer>
      <DialogBox>{children}</DialogBox>
      <Backdrop
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();
          console.log(2);
          if (onClickToggleAddPetModal) {
            onClickToggleAddPetModal();
            console.log(1);
          }
        }}
      />
    </ModalContainer>
  );
}

const ModalContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  position: sticky;
  border-radius: 100px;
`;

const DialogBox = styled.dialog`
  width: 800px;
  height: 200px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border: none;
  border-radius: 20px;
  box-shadow: 0 0 30px rgba(30, 30, 30, 0.185);
  box-sizing: border-box;
  background-color: white;
  z-index: 10000;
`;

const Backdrop = styled.div`
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.2);
`;

export default AddPetModal;
