import React from'react';
import ProfileImgAndTitle from './ProfileImgAndTitle';
import ProfileBio from './ProfileBio';

// 식물일 경우 plantId,
// 유저일 경우 userId (?) 미확정
// (user일 경우(plant가 아닐 경우)) data. 뒤의 내용이 바뀔 수 있음
// (현재 해당 API가 확정 상태가 아님)
// 그리고 식물인지 유저인지에 대한 type이 함께 올 예정
// user일 경우 요구한 user와 페이지 주인 user가 어떤 관계인지
// (본인, 이웃, 서로이웃, 이웃아님)
// 를 알아야 하는데, 그 부분을 아래 (btn, icon) 구현 단계에서 할지 고민


// 아래 부분은 아직 api가 없어 전달할 데이터가 없기 때문에 추가한 내용이니
// 추후 확인 후 수정 혹은 삭제할 것
// 근데 원래도 데이터가 제대로 들어오지 않거나 아예 오지 않는 경우를 상정해야 함
// 그러므로 나중에 좀 더 고민해보기!
// data = {},
// || '',
const ProfileHeader = ({ data = {}, type }) => {

  const profileData = type === 'plant' ? {
    imgSrc: data.profile || '',
    title: data.plantTypeId || 'Unknown Plant',
    nickname: data.nickname || 'Unknown Nickname',
    bio: data.bio || '자기소개가 비어 있습니다.',
  } : {
    imgSrc: data.profile || '',
    title: data.title || 'Unknown Plant',
    nickname: data.nickname || 'Unknown Nickname',
    bio: data.bio || '자기소개가 비어 있습니다.',
  };


  return (
    <div>
      <ProfileImgAndTitle 
        imgSrc={profileData.profile}
        title={profileData.plantTypeId}
        nickname={profileData.nickname}
      />
      <ProfileBio 
        bio={profileData.bio}
        type={type}
      />
    </div>
  );
};

export default ProfileHeader;