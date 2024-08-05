import React from "react";
import defaultImg from '../../assets/icon/default.png';

const ProfilePlantCard = ({ profile, nickname, plantTypeId, birthDate }) => {
  return (
    <div>
      <img
        src={profile || defaultImg}
        alt="thumbnail"
      />
      <p>{nickname}</p>
      <p>{plantTypeId}</p>
      <p>생일: {birthDate}</p>
    </div>
  );
};

export default ProfilePlantCard;