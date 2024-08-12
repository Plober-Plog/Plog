import React from'react';
import ProfileImgAndTitle from './ProfileImgAndTitle';
import ProfileBio from './ProfileBio';

const ProfileHeader = ({ data = {}, type }) => {

  const profileData = {}

  if (type === 'plant') {
    console.log('ProfileHeader Data:', data)
    profileData.imgSrc = data.profile || '';
    profileData.title = data.plantTypeName || '미지정 식물';
    profileData.nickname = data.nickname || '닉네임 없음';
    profileData.bio = data.bio || '자기소개가 비어 있습니다.';
    profileData.ownerId = data.plantId || null;
    profileData.hasNotification = data.hasNotification;
    profileData.isFixed = data.isFixed;
  } else if (type === 'user') {
    console.log('ProfileHeader Data:', data)
    profileData.imgSrc = data.profile || '';
    profileData.title = data.searchId || '아이디 조회 불가';
    profileData.nickname = data.nickname || '닉네임 없음';
    profileData.bio = data.profile_info || '자기소개가 비어 있습니다.';
    profileData.ownerId = data.searchId || null;
  } else {
  }

  console.log('ProfileHeader Processed Data:', profileData);

  return (
    <div className='grid grid-cols-12 gap-4 mt-5 mb-4'>
      <div className='col-span-4'>
        <ProfileImgAndTitle 
          imgSrc={profileData.imgSrc}
          title={profileData.title}
          nickname={profileData.nickname}
        />
      </div>
      <div className='col-span-8'>
        <ProfileBio 
          bio={profileData.bio}
          type={type}
          ownerId={profileData.ownerId}
          hasNotification={profileData.hasNotification}
          isFixed={profileData.isFixed}
          profileData={data}
        />
      </div>
    </div>
  );
};

export default ProfileHeader;