import React from'react';
import ProfileImgAndTitle from './ProfileImgAndTitle';
import ProfileBio from './ProfileBio';

const ProfileHeader = ({ data = {}, type }) => {

  const profileData = {}

  if (type === 'plant') {
    profileData.imgSrc = data.profile || '';
    profileData.title = data.plantTypeId || '미지정 식물';
    profileData.nickname = data.nickname || '닉네임 없음';
    profileData.bio = data.bio || '자기소개가 비어 있습니다.';
    profileData.ownerId = data.plantId || null;
    profileData.hasNotified = data.hasNotified;
    profileData.isFixed = data.isFixed;
  } else if (type === 'user') {
    profileData.imgSrc = data.profile || '';
    profileData.title = data.title || '타이틀이 없습니다';
    profileData.nickname = data.nickname || '닉네임 없음';
    profileData.bio = data.profile_nifo || '자기소개가 비어 있습니다.';
    profileData.ownerId = data.searchId || null;
  } else {
    console.error(`Unexpected type: ${type}`)
  }

  return (
    <div>
      <ProfileImgAndTitle 
        imgSrc={profileData.imgSrc}
        title={profileData.title}
        nickname={profileData.nickname}
      />
      <ProfileBio 
        bio={profileData.bio}
        type={type}
        ownerId={profileData.ownerId}
        hasNotified={profileData.hasNotified}
        isFixed={profileData.isFixed}
        profileData={data}
      />
    </div>
  );
};

export default ProfileHeader;