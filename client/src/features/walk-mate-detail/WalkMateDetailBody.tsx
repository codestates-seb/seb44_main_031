import React, { useState, useEffect } from 'react';
import {  useParams } from 'react-router-dom';
import styled from 'styled-components';
import UserCard from './UserCard';
import axios from 'axios';
import { API_URL, AUTH_TOKEN, TOKEN_USERID } from '../../api/APIurl';


interface Comment {
  commentId: number;
  userId: number;
  username: string;
  commentContent: string;
  createdAt: string;
  imgurl: string;
  body: string;
}

interface Article {
  postId: number;
  title: string;
  body: string;
  location: string;
  attendants: number;
  createdAt: string;
  modifiedAt: string;
  comments: Comment[];
  imageUrls: string[];
  article: any;
}

const WalkMateDetailBody = () => {
  const { articleId } = useParams<{ articleId: string }>();

  const [article, setArticle] = useState<Article | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState<string>('');

  useEffect(() => {
    const fetchArticle = async () => {
      try {
        const response = await axios.get<Article>(
          `${API_URL}/articles/${articleId}`,
          {
            headers: {
              Authorization: AUTH_TOKEN,
            },
          }
        );
        setArticle(response.data.article);

        setComments(response.data.comments || []);
      } catch (error) {
        console.error('Failed to fetch comments:', error);
      }
    };
    fetchArticle();
  }, []);

  const handleCommentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const handleCommentSubmit = async () => {
    if (newComment.trim() !== '') {
      try {
        const response = await axios.post<Comment>(
          `${API_URL}/articles/${articleId}/comments`,
          {
            body: newComment,
          },
          {
            headers: {
              Authorization: AUTH_TOKEN,
            },
          }
        );
        setComments([...comments, response.data]);
        setNewComment('');
        window.location.reload();
      } catch (error) {
        console.error('Failed to submit comment:', error);
      }
    }
  };

  const handleCommentDelete = async (id: number) => {
    try {
      await axios.delete(`${API_URL}/articles/${articleId}/comments/${id}`, {
        headers: {
          Authorization: AUTH_TOKEN,
        },
      });
      const updatedComments = comments.filter(
        (comment) => comment.commentId !== id
      );
      setComments(updatedComments);
      window.location.reload();
    } catch (error) {
      console.error('Failed to delete comment:', error);
    }
  };

  return (
    <BodyContainer>
      <WalkMateBodyContainer>
        {article && (
          <>
            <WalkDogImage src={article.imageUrls[0]} alt="강아지사진" />
            <TextBox>
              <div className="TextBoxTitle">{article.title}</div>
              <div className="TextBoxBody">{article.body}</div>
            </TextBox>
          </>
        )}
        <UserCard />

        {article?.comments.map((comment) => (
          <Comment key={comment.commentId}>
            <UserProfileImage src={comment.imgurl} alt="프로필 사진" />
            <CommentContent>
              <CommentAuthor>{comment.username}</CommentAuthor>
              <CommentText>{comment.body}</CommentText>
            </CommentContent>
            {comment.userId ===
              (TOKEN_USERID !== null ? parseInt(TOKEN_USERID) : null) && (
              <CommentDeleteButton
                onClick={() => handleCommentDelete(comment.commentId)}
              >
                삭제
              </CommentDeleteButton>
            )}
          </Comment>
        ))}

        <CommentBox>
          {/* <UserProfileImage src={comments.imgurl} alt="프로필 사진" /> */}
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

const CommentBox = styled.div`
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f7eaf3;
  display: flex;
  align-items: center;
  width: 580px;
  border-radius: 5px;
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
  background-color: #f7eaf3;
  padding: 10px;
  border-radius: 5px;
  width: 600px;

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
  color: var(--black-900);
`;

const CommentAuthor = styled.div`
  font-weight: bold;
  color: var(--black-900);
  margin-bottom: 3px;
`;

const CommentDeleteButton = styled.button`
  padding: 5px 10px;
  border: none;
  background-color: transparent;
  color: var(--black-900);
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
