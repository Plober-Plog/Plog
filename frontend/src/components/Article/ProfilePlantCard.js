import React from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';

const ProfilePlantCard = ({ plantId, profile, nickname, plantTypeId, birthDate, plantTypeName }) => {
  
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/plant/${plantId}`);
  }
  
  return (
    <div onClick={handleClick}>
      <img
        src={profile || defaultImg}
        alt="thumbnail"
      />
      <p>{nickname}</p>
      <p>{plantTypeName}</p>
      <p>생일: {birthDate}</p>
    </div>
  );
};

export default ProfilePlantCard;