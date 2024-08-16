import React from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
<<<<<<< HEAD

const ProfilePlantCard = ({ plantId, profile, nickname, plantTypeId, birthDate, plantTypeName }) => {
=======
import './ProfilePlantCardList.css';

const ProfilePlantCard = ({ plantId, profile, nickname, birthDate, plantTypeName, otherPlantTypeName, isClickable, isDeleted, deadDate }) => {
>>>>>>> master
  
  const navigate = useNavigate();

  const handleClick = () => {
<<<<<<< HEAD
    navigate(`/plant/${plantId}`);
  }
  
  return (
    <div onClick={handleClick}>
=======
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
>>>>>>> master
      <img
        src={profile || defaultImg}
        alt="thumbnail"
      />
<<<<<<< HEAD
      <p>{nickname}</p>
      <p>{plantTypeName}</p>
      <p>생일: {birthDate}</p>
=======
      <div className="profile-plant-card-text">
        <p className="profile-plant-card-nickname">{nickname}</p>
        <p className="profile-plant-card-planttype">{plantTypeName || otherPlantTypeName}</p>
        <p className="profile-plant-card-birth">{birthDate}</p>
      </div>
>>>>>>> master
    </div>
  );
};

export default ProfilePlantCard;