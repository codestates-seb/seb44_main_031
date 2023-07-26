import React from 'react';
import { IoCopyOutline } from 'react-icons/io5';
import { toast } from 'react-toastify';
import { styled } from 'styled-components';
import ClipboardJS from 'clipboard';

// const CopyIcon = ({ textToCopy }: { textToCopy: string }) => {
//   const handleCopyClick = async (event: React.MouseEvent<HTMLElement>) => {
//     event.stopPropagation();
//     try {
//       await navigator.clipboard.writeText(textToCopy);
//       toast.success('해당 모임 URL이 복사되었습니다!', {
//         toastId: 'copy-success',
//         autoClose: 900,
//         position: 'bottom-center',
//         hideProgressBar: true,
//       });
//     } catch (error) {
//       toast.error(`Failed to copy URL: ${error}`, {
//         toastId: 'copy-error',
//         autoClose: 900,
//         position: 'bottom-center',
//       });
//     }
//   };

//   return (
//     <StyledCopyIconContainer>
//       <IoCopyOutline className="copy-icon" onClick={handleCopyClick} />
//     </StyledCopyIconContainer>
//   );
// };

// 라이브러리
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

// 기능은 안되고 에러메세지 띄워주기
// const CopyIcon = ({ textToCopy }: { textToCopy: string }) => {
//   const handleCopyClick = async (event: React.MouseEvent<HTMLElement>) => {
//     event.stopPropagation();
//     try {
//       if (navigator.clipboard) {
//         await navigator.clipboard.writeText(textToCopy);
//         toast.success('해당 모임 URL이 복사되었습니다!', {
//           toastId: 'copy-success',
//           autoClose: 900,
//           position: 'bottom-center',
//           hideProgressBar: true,
//         });
//       } else {
//         toast.error('해당 브라우저에서는 복사 기능을 지원하지 않습니다.', {
//           toastId: 'copy-error',
//           autoClose: 900,
//           position: 'bottom-center',
//         });
//       }
//     } catch (error) {
//       toast.error(`Failed to copy URL: ${error}`, {
//         toastId: 'copy-error',
//         autoClose: 900,
//         position: 'bottom-center',
//       });
//     }
//   };

//   return (
//     <StyledCopyIconContainer>
//       <IoCopyOutline className="copy-icon" onClick={handleCopyClick} />
//     </StyledCopyIconContainer>
//   );
// };

const StyledCopyIconContainer = styled.div`
  .copy-icon {
    width: 18px;
    height: 18px;
    color: var(--black-700);
    stroke-width: 0;
  }
`;

export default CopyIcon;
