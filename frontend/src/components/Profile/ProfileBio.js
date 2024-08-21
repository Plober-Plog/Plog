import React from 'react';
import ProfileHeaderPlantIconList from './ProfileHeaderPlantIconList';
import ProfileHeaderUserBtnList from './ProfileHeaderUserBtnList';
import './ProfileBio.css'

const ProfileBio = ({ etcPlantType = false, bio, type, ownerId, hasNotification, isFixed, profileData }) => {

  const renderBtnIcon = () => {
    if (type === 'user') {
      return <ProfileHeaderUserBtnList ownerId={ownerId}/>;
    } else if (type === 'plant') {
      return (
        <ProfileHeaderPlantIconList 
          ownerId={ownerId}
          hasNotification={hasNotification}
          isFixed={isFixed}
          profileData={profileData}
          etcPlantType={etcPlantType}
        />
      )
    } else {
      console.error(`Error. type이 ${type}로 들어왔습니다.`)
      return <p>잘못된 정보입니다.</p>;
    }
  };


  return (
    <div className="profile-bio-container">
      <div className="profile-bio">
        <span>{bio}</span>
      </div>
      <div className="profile-bio-btn justify-end">
        {renderBtnIcon()}
      </div>
    </div>
  );
};

export default ProfileBio;