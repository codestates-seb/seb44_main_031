import { styled } from 'styled-components';
import { FaRegClock, FaMapMarkerAlt } from 'react-icons/fa';

const WalkMateDetailSide: React.FC = () => {
  return (
    <SlideBox>
      <SlideContainer>
        <SideDog src="/src/assets/sidedog.png" />
        <SideTextUp>
          <div className="Name">콩이파파: ENFP</div>
          <div className="Name">콩이: INFP</div>
        </SideTextUp>
      </SlideContainer>
      <SlideContainer>
        <SideTextDown>
          <div className="Line">
            <FaRegClock className="Icon" />
            <div className="Name">2023.06.30 금요일</div>
          </div>
          <div className="Line">
            <div className="Name">
              <FaMapMarkerAlt className="LocationIcon" />
              한강공원
            </div>
          </div>
        </SideTextDown>
      </SlideContainer>
      <Map src="/src/assets/map.png" />
    </SlideBox>
  );
};

export default WalkMateDetailSide;

const SlideContainer = styled.div`
  margin-top: 40px;
  width: 250px;
  height: 150px;
  border-radius: 30px;
  background-color: var(--pink-200);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const SideDog = styled.img`
  width: 64px;
  height: 64px;
  margin-right: 10px;
`;

const SideTextUp = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  .Name {
    margin-top: 10px;
    font-size: 16px;
    font-weight: bold;
    color: var(--black-900);
  }
`;

const SideTextDown = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  .Line {
    display: flex;
    align-items: center;
  }

  .Icon {
    margin-top: 14px;
    margin-right: 5px;
    font-size: 17px;
    color: var(--black-900);
  }

  .Name {
    margin-top: 20px;
    font-size: 16px;
    font-weight: bold;
    color: var(--black-900);
  }
`;
const Map = styled.img`
  margin-top: 50px;
  border-radius: 30px;
  width: 250px;
`;

const SlideBox = styled.div``;
