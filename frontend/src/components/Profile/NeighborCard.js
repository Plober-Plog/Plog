import React from "react";
import { useNavigate } from 'react-router-dom';
import './NeighborCard.css'

const NeighborCard = ({user}) => {
  const navigate = useNavigate();

  const handleClickCard = () => {
    navigate(`/profile/${user.searchId}`);
  }
  console.log('User profile image URL:', user.profile);

  return (
    <li className="neighbor-card col-span-4" onClick={handleClickCard}>
      <div className="neighbor-card-image-container">
        <div className="neighbor-card-image">
          <img src={user.profile} alt="profile img" />
        </div>
        <span className="neighbor-card-id">{user.searchId}</span>
      </div>
      <span className="neighbor-card-nickname">{user.nickname}</span>
    </li>
  )
}

export default NeighborCard;