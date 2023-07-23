import React, { PropsWithChildren } from 'react';
import styled from 'styled-components';

// FEEDBACK: 어떤 인터페이스는 -Type이 아닌데, 여기는 -Type이라고 이름을 붙였습니다. 통일해주세요.
interface ModalDefaultType {
  onClickUsernameChangeToggleModal: () => void;
}

function UsernameChangeModal({
  onClickUsernameChangeToggleModal,
  children,
}: PropsWithChildren<ModalDefaultType>) {
  return (
    <ModalContainer>
      <DialogBox>{children}</DialogBox>
      <Backdrop
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (onClickUsernameChangeToggleModal) {
            onClickUsernameChangeToggleModal();
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
  width: 550px;
  height: 260px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border: none;
  border-radius: 20px;
  box-shadow: 0 0 30px rgba(30, 30, 30, 0.185);
  box-sizing: border-box;
  margin-left: -100px;
  background-color: white;
  z-index: 10000;
`;

const Backdrop = styled.div`
  width: 140vw;
  height: 100vh;
  position: fixed;
  top: 0;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.2);
`;

export default UsernameChangeModal;
