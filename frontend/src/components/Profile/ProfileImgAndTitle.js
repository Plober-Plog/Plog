import React from'react';
<<<<<<< HEAD
import ProfileTitle from './ProfileTitle';
import '../../pages/Plant/PlantDetail.css';
import defaultImg from '../../assets/icon/default.png';

const ProfileImgAndTitle = ({ imgSrc, title, nickname }) => {
  return (
    <div>
      <div>
        <img src={imgSrc || defaultImg} alt="profile img" className='profile-image'/>
      </div>
      <div>
        <ProfileTitle title={title}/>
      </div>
      <div>
        <span>
          {nickname}
        </span>
      </div>
=======
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
>>>>>>> master
    </div>
  );
};

export default ProfileImgAndTitle;