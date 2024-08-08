import React from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';

const ProfilePlantCard = ({ plantId, profile, nickname, birthDate, plantTypeName, otherPlantTypeName, isClickable }) => {
  
  const navigate = useNavigate();

  const handleClick = () => {
    if (isClickable) {
      navigate(`/plant/${plantId}`);
    }
  }
  
  return (
    <div onClick={handleClick}>
      <img
        src={profile || defaultImg}
        alt="thumbnail"
      />
      <p>{nickname}</p>
      <p>{plantTypeName}</p>
      <p>{otherPlantTypeName}</p>
      <p>생일: {birthDate}</p>
    </div>
  );
};

export default ProfilePlantCard;