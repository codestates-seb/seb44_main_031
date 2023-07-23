import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { styled } from 'styled-components';
import { API_URL, AUTH_TOKEN } from '../../api/APIurl';
interface Owner {
  userId: number;
  nickname: string;
  imgUrl: string;
  pets: Pet[];
}

interface Pet {
  petImUrl: string;
  petName: string;
}

interface Article {
  articleId: number;
  title: string;
  body: string;
  location: string;
  attendants: number;
  date: string;
  comments: Comment[];
}

interface Comment {
  commentId: number;
  userId: number;
  username: string;
  commentContent: string;
  createdAt: string;
}

interface WalkMateData {
  owner: Owner;
  article: Article;
  attendees: Owner[];
}

const WalkMateDetailHeader = () => {
  const [walkMateData, setWalkMateData] = useState<WalkMateData | null>(null);
  const { articleId } = useParams<{ articleId: string }>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get<WalkMateData>(
          `${API_URL}/articles/${articleId}`,
          {
            headers: {
              Authorization: AUTH_TOKEN,
            },
          }
        );
        setWalkMateData(response.data);
      } catch (error) {
        console.error('데이터를 가져오는 데 실패했습니다:', error);
      }
    };
    fetchData();
  }, [articleId]);

  if (!walkMateData) {
    return <div>Loading...</div>;
  }

  const { owner, article } = walkMateData;

  return (
    <Container>
      <WalkMateDetailHeaderBox>
        <div className="title">{article.title}</div>
        <DetailHeaderBox>
          <ProfileImage src={owner.imgUrl} alt="프로필이미지" />
          <DetailTextBox>
            <div className="uptext">Hosted By</div>
            <div className="profilename">{owner.nickname}</div>
          </DetailTextBox>
        </DetailHeaderBox>
      </WalkMateDetailHeaderBox>
    </Container>
  );
};

export default WalkMateDetailHeader;

const Container = styled.div`
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
`;
const WalkMateDetailHeaderBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 300px;
  background-color: #ffffff;
  height: 100px;

  .title {
    font-size: 25px;
    font-weight: 500;
  }
`;

const DetailHeaderBox = styled.div`
  display: flex;
  align-items: center;
`;

const ProfileImage = styled.img`
  width: 50px;
  height: 50px;

  border-radius: 50%;
  margin-right: 10px;
`;
const DetailTextBox = styled.div`
  margin-top: 10px;
  .uptext {
    font-weight: 300;
    font-size: 13px;
  }
  .profilename {
    margin-top: 2px;
    font-weight: bold;
  }
`;
