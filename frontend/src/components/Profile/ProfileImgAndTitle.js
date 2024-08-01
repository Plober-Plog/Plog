import React from'react';
import ProfileTitle from './ProfileTitle';

const ProfileImgAndTitle = ({ imgSrc, title, nickname }) => {
  return (
    <div>
      <div>
        <img src={imgSrc} alt="profile img"/>
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