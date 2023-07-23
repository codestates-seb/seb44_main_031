import React, { useState } from 'react';
import { CiBookmark } from 'react-icons/ci';
import { styled } from 'styled-components';
import { FcBookmark } from 'react-icons/fc';
import { toast } from 'react-toastify';

const BookmarkIcon = () => {
  const [isBookmakred, setIsBookmakred] = useState(false);

  const handleBookmarkClick = (event: React.MouseEvent<HTMLElement>) => {
    event.stopPropagation();

    setIsBookmakred((prev) => !prev);

    if (isBookmakred) {
      toast.info('북마크가 취소 되었습니다', {
        toastId: 'bookmark-off',
        autoClose: 900,
        position: 'bottom-center',
        hideProgressBar: true,
      });
      return;
    }

    // FEEDBACK: if (isMookmarked)를 했고 여기에 도달했다면 이미 !isBookmarked라는 것이 확실하므로
    // 아래 조건문은 불필요합니다.
    if (!isBookmakred) {
      toast.success('북마크가 등록 되었습니다', {
        toastId: 'bookmark-on',
        autoClose: 900,
        position: 'bottom-center',
        hideProgressBar: true,
      });
    }
  };

  return (
    <StyledBookmarkIconContainer>
      {isBookmakred ? (
        <FcBookmark
          className="bookmark-on-icon"
          onClick={handleBookmarkClick}
        />
      ) : (
        <CiBookmark
          className="bookmark-off-icon"
          onClick={handleBookmarkClick}
        />
      )}
    </StyledBookmarkIconContainer>
  );
};

const StyledBookmarkIconContainer = styled.div`
  .bookmark-off-icon {
    width: 18px;
    height: 18px;
    color: var(--black-800);
    stroke-width: 0;
  }

  .bookmark-on-icon {
    width: 18px;
    height: 18px;
    stroke-width: 0;
  }
`;

export default BookmarkIcon;
