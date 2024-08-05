import React from 'react';
import ProfileHeaderPlantIconList from './ProfileHeaderPlantIconList';
import ProfileHeaderUserBtnList from './ProfileHeaderUserBtnList';

const ProfileBio = ({ bio, type, plantId, hasNotified, plantData }) => {

  const renderBtnIcon = () => {
    if (type === 'user') {
      return <ProfileHeaderUserBtnList />;
    } else if (type === 'plant') {
      return (
        <ProfileHeaderPlantIconList 
          plantId={plantId}
          hasNotified={hasNotified}
          plantData={plantData}
        />
      )
    } else {
      console.error(`Error. type이 ${type}로 들어왔습니다.`)
      return <p>잘못된 정보입니다.</p>;
    }
  };


  return (
    <div>
      <span>{bio}</span>
      {renderBtnIcon()}
    </div>
  );
};

export default ProfileBio;