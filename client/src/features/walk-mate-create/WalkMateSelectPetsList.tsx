import { Dispatch, SetStateAction } from 'react';
import { styled } from 'styled-components';
import { FormDatas } from './hooks/useWalkMateForm';

interface propType {
  inputValue: FormDatas;
  setInputValue: Dispatch<SetStateAction<FormDatas>>;
}

interface PetCardPropType {
  id: number;
  imgUrl: string;
  name: string;
  selectedPets: number[];
  inputValue: FormDatas;
  setInputValue: Dispatch<SetStateAction<FormDatas>>;
}

// 개별 펫카드 컴포넌트
const PetCard = ({
  id,
  imgUrl,
  name,
  selectedPets,
  inputValue,
  setInputValue,
}: PetCardPropType) => {
  // 현재 Card 의 펫이 선택이 돼있는지 아닌지 판단하기 위한 불리안 값.
  // isSelected 값에 따라서 UI와 state 을 업데이트하는 용도.
  const isSelected = selectedPets.some((pet) => pet === id);

  const handlePetCardClick = () => {
    // 본인의 펫 리스트중에 선택되지 않은 펫들의 id[]
    const unselectPetResult = inputValue.selectedPets.filter(
      (pet) => pet !== id
    );

    // 클릭된 펫이 이미 선택되있는 경우는 선택 취소되게 하고,
    // 선택되있지 않은 경우는 선택되게 해야함.
    if (isSelected) {
      setInputValue((prev) => {
        return { ...prev, selectedPets: unselectPetResult };
      });
    }
    if (!isSelected) {
      setInputValue((prev) => {
        return { ...prev, selectedPets: [...prev.selectedPets, id] };
      });
    }
  };

  return (
    <StyledPetCardContainer
      className="pet-card-container"
      onClick={handlePetCardClick}
      $isSelected={isSelected}
    >
      <img src={imgUrl} alt="img" className="pet-image" />
      <p>{name}</p>
    </StyledPetCardContainer>
  );
};

// 페이지 컴포넌트 단에서 렌더될 PetCard의 컨테이너 컴포넌트
const WalkMateSelectPetsList = ({ inputValue, setInputValue }: propType) => {
  return (
    <StyledPetsListContainer>
      {inputValue.pets.map((pet) => (
        <PetCard
          key={pet.id}
          id={pet.id}
          imgUrl={pet.imgUrl}
          name={pet.name}
          selectedPets={inputValue.selectedPets}
          inputValue={inputValue}
          setInputValue={setInputValue}
        />
      ))}
    </StyledPetsListContainer>
  );
};

// 스타일드 컴포넌츠
const StyledPetsListContainer = styled.ul`
  display: flex;
  gap: 20px;
  margin-top: 8px;
`;

interface PetCardContainerProps {
  readonly $isSelected: boolean;
}

const StyledPetCardContainer = styled.li<PetCardContainerProps>`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 15px;
  cursor: pointer;

  border: 1px solid var(--black-400);
  border-radius: 20px;
  width: 120px;
  height: 120px;
  background-color: ${({ $isSelected }) => ($isSelected ? 'pink' : 'white')};

  .pet-image {
    height: 60px;
  }

  &:hover {
    /* background-color: var(--pink-200); */
    background-color: ${({ $isSelected }) => ($isSelected ? 'pink' : 'white')};
    transform: translateY(-5px);
    box-shadow: 0px 10px 10px rgba(0, 0, 0, 0.1),
      0px 5px 10px rgba(0, 0, 0, 0.1);
  }

  &:active {
    background-color: ${({ $isSelected }) => ($isSelected ? 'pink' : 'white')};
    transform: translateY(0px);
  }
`;

export default WalkMateSelectPetsList;
