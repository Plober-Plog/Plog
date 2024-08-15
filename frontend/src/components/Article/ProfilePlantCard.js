import React from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './ProfilePlantCardList.css';

const ProfilePlantCard = ({ plantId, profile, nickname, birthDate, plantTypeName, otherPlantTypeName, isClickable, isDeleted, deadDate }) => {
  
  const navigate = useNavigate();

  const handleClick = () => {
    if (isClickable) {
      navigate(`/plant/${plantId}`);
    }
  }
  
  if (isDeleted) {
    return null;
  }
  
  return (
    <div
      className={`profile-plant-card relative ${deadDate ? 'opacity-20' : ''}`}
      onClick={handleClick}
    >
      <img
        src={profile || defaultImg}
        alt="thumbnail"
      />
      <div className="profile-plant-card-text">
        <p className="profile-plant-card-nickname">{nickname}</p>
        <p className="profile-plant-card-planttype">{plantTypeName || otherPlantTypeName}</p>
        <p className="profile-plant-card-birth">{birthDate}</p>
      </div>
    </div>
  );
};

export default ProfilePlantCard;