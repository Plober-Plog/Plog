import React from "react";
import { useNavigate } from 'react-router-dom';

const NeighborCard = ({user}) => {
  const navigate = useNavigate();

  const handleClickCard = () => {
    navigate(`/profile/${user.searchId}`);
  }
  console.log('User profile image URL:', user.profile);

  return (
    <li
      key={user.searchId}
      onClick={handleClickCard}
    >
      <img src={user.profile} alt={'profile img'}/>
      <span>{user.nickname}</span>
    </li>
  )
}

export default NeighborCard;