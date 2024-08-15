import React from'react';
import '../../pages/Plant/PlantDetail.css';
import defaultImg from '../../assets/icon/default.png';
import './ProfileImgAndTitle.css'

const ProfileImgAndTitle = ({ imgSrc, title, nickname }) => {
  return (
    <div className='profile-img-and-title-container flex flex-col items-center text-center'>
      <div className="relative w-full max-w-xs">
        <img 
          src={imgSrc || defaultImg} 
          alt="profile img" 
          className="w-full h-full rounded-full object-cover"
          style={{ aspectRatio: '1 / 1' }}
        />
        <span className="w-full profile-img-and-title-title">
          {title}
        </span>
      </div>
      <span className="profile-img-and-title-nickname">
        {nickname}
      </span>
    </div>
  );
};

export default ProfileImgAndTitle;