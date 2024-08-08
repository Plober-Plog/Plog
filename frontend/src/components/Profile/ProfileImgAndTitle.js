import React from'react';
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
    </div>
  );
};

export default ProfileImgAndTitle;