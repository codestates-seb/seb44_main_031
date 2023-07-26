import React from 'react';
import { IoCopyOutline } from 'react-icons/io5';
import { toast } from 'react-toastify';
import { styled } from 'styled-components';
import ClipboardJS from 'clipboard';

const CopyIcon = ({ textToCopy }: { textToCopy: string }) => {
  const handleCopyClick = (event: React.MouseEvent<HTMLElement>) => {
    event.stopPropagation();
    const clipboard = new ClipboardJS('.copy-icon', {
      text: () => textToCopy,
    });

    clipboard.on('success', () => {
      toast.success('해당 모임 URL이 복사되었습니다!', {
        toastId: 'copy-success',
        autoClose: 900,
        position: 'bottom-center',
        hideProgressBar: true,
      });
    });

    clipboard.on('error', () => {
      toast.error('Failed to copy URL.', {
        toastId: 'copy-error',
        autoClose: 900,
        position: 'bottom-center',
      });
    });

    //@ts-ignore
    clipboard.onClick(event.nativeEvent as ClipboardJS.Event);
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
