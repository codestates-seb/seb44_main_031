import React, { useState, useEffect } from 'react';
import { styled } from 'styled-components';
import UserCard from './UserCard';
import axios from 'axios';

interface Comment {
  id: number;
  content: string;
}

const WalkMateDetailBody = () => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState<string>('');

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = async () => {
    try {
      const response = await axios.get<Comment[]>(
        'http://localhost:3001/comments'
      );
      setComments(response.data);
    } catch (error) {
      console.error('Failed to fetch comments:', error);
    }
  };

  const handleCommentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const handleCommentSubmit = async () => {
    if (newComment.trim() !== '') {
      try {
        const response = await axios.post<Comment>(
          'http://localhost:3001/comments',
          {
            content: newComment,
          }
        );
        setComments([...comments, response.data]);
        setNewComment('');
      } catch (error) {
        console.error('Failed to submit comment:', error);
      }
    }
  };

  const handleCommentDelete = async (id: number) => {
    try {
      await axios.delete(`http://localhost:3001/comments/${id}`);
      const updatedComments = comments.filter((comment) => comment.id !== id);
      setComments(updatedComments);
    } catch (error) {
      console.error('Failed to delete comment:', error);
    }
  };

  return (
    <BodyContainer>
      <WalkMateBodyContainer>
        <WalkDogImage src="/src/assets/Walkdog.png" alt="강아지사진" />
        <TextBox>
          <div className="TextBoxTitle">간단한 소개! (글제목)</div>
          <div className="TextBoxBody">
            우리 콩이가 소형견 이다보니 대형견을 보면 무서워 해요 ㅠㅠ 그래서
            사교성 좋은 강아지들이랑 산책하고 싶어요! (대형견도 사교성 좋은
            강아지면 괜찮을것 같아요!) 매너좋은 반려견, 견주분들 이셨 으면
            좋겠습니다.!
          </div>
        </TextBox>
        <UserCard />

        <ButtonBox>
          <button>참가하기</button>
          <button>수정하기</button>
        </ButtonBox>

        {comments.map((comment) => (
          <Comment key={comment.id}>
            <UserProfileImage src="path_to_profile_image" alt="프로필 사진" />
            <CommentContent>
              <CommentAuthor>작성자 이름</CommentAuthor>
              <CommentText>{comment.content}</CommentText>
            </CommentContent>
            <CommentDeleteButton
              onClick={() => handleCommentDelete(comment.id)}
            >
              삭제
            </CommentDeleteButton>
          </Comment>
        ))}

        <CommentBox>
          <UserProfileImage src="path_to_profile_image" alt="프로필 사진" />
          <CommentInput
            type="text"
            value={newComment}
            onChange={handleCommentChange}
            placeholder="댓글을 입력하세요."
          />
          <CommentSubmitButton onClick={handleCommentSubmit}>
            댓글 작성
          </CommentSubmitButton>
        </CommentBox>
      </WalkMateBodyContainer>
    </BodyContainer>
  );
};

const WalkMateBodyContainer = styled.div`
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 600px;
  margin: 0 auto;
`;

const WalkDogImage = styled.img`
  margin-top: 40px;
  border-radius: 30px;
  width: 500px;
  margin-bottom: 40px;
`;

const TextBox = styled.div`
  width: 500px;
  height: 300px;
  padding: 10px;
  border-radius: 10px;

  .TextBoxTitle {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
    color: #333333;
  }

  .TextBoxBody {
    line-height: 1.6;
    font-size: 16px;
    color: #666666;
  }
`;

const ButtonBox = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;

  button {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    background-color: var(--pink-400);
    color: white;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
  }
`;

const CommentBox = styled.div`
  margin-top: 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  width: 580px;
`;

const UserProfileImage = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
`;

const CommentInput = styled.input`
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 5px 0 0 5px;
`;

const CommentSubmitButton = styled.button`
  padding: 10px 20px;
  border: none;
  border-radius: 0 5px 5px 0;
  background-color: var(--pink-400);
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
`;

const Comment = styled.div`
  display: flex;
  align-items: center;
  margin-top: 10px;
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 5px;

  /* 추가된 스타일 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
`;

const CommentContent = styled.div`
  flex: 1;
  width: 580px;
`;

const CommentText = styled.div`
  margin-bottom: 5px;
  font-size: 14px;
  color: #333333;
`;

const CommentAuthor = styled.div`
  font-weight: bold;
  color: #555555;
  margin-bottom: 3px;
`;

const CommentDeleteButton = styled.button`
  padding: 5px 10px;
  border: none;
  background-color: transparent;
  color: #333333;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
  transition: color 0.3s ease;

  &:hover {
    color: #ff0000;
  }
`;

const BodyContainer = styled.div`
  display: flex;
`;

export default WalkMateDetailBody;
