import { Dispatch, SetStateAction } from 'react';
import { styled } from 'styled-components';
import { inputValueType } from '../../hooks/useWalkMateForm';

interface propType {
  inputValue: inputValueType;
  setInputValue: Dispatch<SetStateAction<inputValueType>>;
}

interface PetCardPropType {
  id: number;
  imgUrl: string;
  name: string;
  selectedPets: number[];
  inputValue: inputValueType;
  setInputValue: Dispatch<SetStateAction<inputValueType>>;
}

const PetCard = ({
  id,
  imgUrl,
  name,
  selectedPets,
  inputValue,
  setInputValue,
}: PetCardPropType) => {
  const isSelected = selectedPets.some((pet) => pet === id);

  const handlePetCardClick = () => {
    const unselectPetResult = inputValue.selectedPets.filter(
      (pet) => pet !== id
    );

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
    <PetCardContainer
      className="pet-card-container"
      onClick={handlePetCardClick}
      isSelected={isSelected}
    >
      <img src={imgUrl} alt="img" className="pet-image" />
      <p>{name}</p>
    </PetCardContainer>
  );
};

const WalkMateSelectPetsList = ({ inputValue, setInputValue }: propType) => {
  return (
    <PetsListContainer>
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
    </PetsListContainer>
  );
};

const PetsListContainer = styled.ul`
  display: flex;
  gap: 20px;
  margin-top: 8px;
`;

interface PetCardContainerProps {
  readonly isSelected: boolean;
}

const PetCardContainer = styled.li<PetCardContainerProps>`
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
  background-color: ${({ isSelected }) => (isSelected ? 'pink' : 'white')};

  .pet-image {
    height: 60px;
  }

  &:hover {
    /* background-color: var(--pink-200); */
    background-color: ${({ isSelected }) => (isSelected ? 'pink' : 'white')};
    transform: translateY(-5px);
    box-shadow: 0px 10px 10px rgba(0, 0, 0, 0.1),
      0px 5px 10px rgba(0, 0, 0, 0.1);
  }

  &:active {
    background-color: ${({ isSelected }) => (isSelected ? 'pink' : 'white')};
    transform: translateY(0px);
  }
`;

export default WalkMateSelectPetsList;
