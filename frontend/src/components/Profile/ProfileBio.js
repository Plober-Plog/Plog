import React from 'react';
import ProfileHeaderPlantIconList from './ProfileHeaderPlantIconList';
import ProfileHeaderUserBtnList from './ProfileHeaderUserBtnList';
<<<<<<< HEAD

const ProfileBio = ({ bio, type, ownerId, hasNotified, isFixed, profileData }) => {
=======
import './ProfileBio.css'

const ProfileBio = ({ etcPlantType = false, bio, type, ownerId, hasNotification, isFixed, profileData }) => {
>>>>>>> master

  const renderBtnIcon = () => {
    if (type === 'user') {
      return <ProfileHeaderUserBtnList ownerId={ownerId}/>;
    } else if (type === 'plant') {
      return (
        <ProfileHeaderPlantIconList 
          ownerId={ownerId}
<<<<<<< HEAD
          hasNotified={hasNotified}
          isFixed={isFixed}
          profileData={profileData}
=======
          hasNotification={hasNotification}
          isFixed={isFixed}
          profileData={profileData}
          etcPlantType={etcPlantType}
>>>>>>> master
        />
      )
    } else {
      console.error(`Error. type이 ${type}로 들어왔습니다.`)
      return <p>잘못된 정보입니다.</p>;
    }
  };


  return (
<<<<<<< HEAD
    <div>
      <span>{bio}</span>
      {renderBtnIcon()}
=======
    <div className="profile-bio-container">
      <div className="profile-bio">
        <span>{bio}</span>
      </div>
      <div className="profile-bio-btn justify-end">
        {renderBtnIcon()}
      </div>
>>>>>>> master
    </div>
  );
};

export default ProfileBio;