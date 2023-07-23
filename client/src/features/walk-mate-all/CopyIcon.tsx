import React from 'react';
import { IoCopyOutline } from 'react-icons/io5';
import { toast } from 'react-toastify';
import { styled } from 'styled-components';

const CopyIcon = ({ textToCopy }: { textToCopy: string }) => {
  const handleCopyClick = async (event: React.MouseEvent<HTMLElement>) => {
    event.stopPropagation();
    try {
      await navigator.clipboard.writeText(textToCopy);
      toast.success('해당 모임 URL이 복사되었습니다!', {
        toastId: 'copy-success',
        autoClose: 900,
        position: 'bottom-center',
        hideProgressBar: true,
      });
    } catch (error) {
      toast.error(`Failed to copy URL: ${error}`, {
        toastId: 'copy-error',
        autoClose: 900,
        position: 'bottom-center',
      });
    }
  };

  return (
    <StyledCopyIconContainer>
      <IoCopyOutline className="copy-icon" onClick={handleCopyClick} />
    </StyledCopyIconContainer>
  );
};

const StyledCopyIconContainer = styled.div`
  .copy-icon {
    width: 18px;
    height: 18px;
    color: var(--black-700);
    stroke-width: 0;
  }
`;

export default CopyIcon;
