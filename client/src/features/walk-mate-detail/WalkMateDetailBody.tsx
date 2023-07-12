import React, { useState, useEffect } from 'react';
import { styled } from 'styled-components';
import UserCard from './UserCard';
import axios from 'axios';

interface Comment {
  commentId: number;
  userId: number;
  username: string;
  commentContent: string;
  createdAt: string;
}

interface Owner {
  userId: number;
  username: string;
  userimUrl: string;
}

interface Post {
  postId: number;
  title: string;
  body: string;
  location: string;
  attendants: number;
  createdAt: string;
  modifiedAt: string;
  comments: Comment[];
}

interface Attendee {
  userId: number;
  username: string;
  userimUrl: string;
}

interface WalkMateDetailData {
  owner: Owner;
  post: Post;
  attendees: Attendee[];
}

const WalkMateDetailBody: React.FC = () => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState<string>('');

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = async () => {
    try {
      const response = await axios.get<Comment[]>(
        'http://localhost:3001/walk-mate'
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
          'http://localhost:3001/walk-mate',
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
      await axios.delete(`http://localhost:3001/walk-mate/${id}`);
      const updatedComments = comments.filter(
        (comment) => comment.commentId !== id
      );
      setComments(updatedComments);
    } catch (error) {
      console.error('Failed to delete comment:', error);
    }
  };

  const walkMateDetailData: WalkMateDetailData = {
    owner: {
      userId: 1,
      username: 'JohnDoe',
      userimUrl: 'imageURL',
    },
    post: {
      postId: 1,
      title: '산책 장소 수정합니다',
      body: '호수 공원에 1시간 정도 같이 산책 다녀오실 분 계신가요?',
      location: '일산 호수 공원',
      attendants: 3,
      createdAt: '2023-07-11 15:00:00',
      modifiedAt: '2023-07-11 15:00:00',
      comments: [
        {
          commentId: 1,
          userId: 2,
          username: 'Kevin',
          commentContent: '참여 눌렀습니다, 이따봐요!',
          createdAt: '2023-07-11 15:00:00',
        },
      ],
    },
    attendees: [
      {
        userId: 1,
        username: 'JohnDoe',
        userimUrl: 'default',
      },
      {
        userId: 2,
        username: 'Kevin',
        userimUrl: 'imageURL',
      },
    ],
  };

  return (
    <BodyContainer>
      <WalkMateBodyContainer>
        <WalkDogImage src="/src/assets/Walkdog.png" alt="강아지사진" />
        <TextBox>
          <div className="TextBoxTitle">{walkMateDetailData.post.title}</div>
          <div className="TextBoxBody">{walkMateDetailData.post.body}</div>
        </TextBox>
        <UserCard />

        <ButtonBox>
          <button>참가하기</button>
          <button>수정하기</button>
        </ButtonBox>

        {walkMateDetailData.post.comments.map((comment) => (
          <Comment key={comment.commentId}>
            <UserProfileImage src="path_to_profile_image" alt="프로필 사진" />
            <CommentContent>
              <CommentAuthor>{comment.username}</CommentAuthor>
              <CommentText>{comment.commentContent}</CommentText>
            </CommentContent>
            <CommentDeleteButton
              onClick={() => handleCommentDelete(comment.commentId)}
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
