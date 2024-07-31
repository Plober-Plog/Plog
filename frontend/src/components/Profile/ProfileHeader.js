import React from'react';
import ProfileImgAndTitle from './ProfileImgAndTitle';
import ProfileBio from './ProfileBio';

// 식물일 경우 plantId
// 유저일 경우 userId (?) -> 아직 API 미확정 상태. 추후 수정
// user일 경우 data. 뒤의 내용이 바뀔 수 있음

// 부모 컴포넌트에서 'plant', 'user' 중 무엇인지에 대한 type이 함께 올 예정
// user일 경우 요구한 user와 페이지 주인 user가 어떤 관계인지 체크해야 함
// (본인, 이웃, 서로이웃, 이웃아님)

// data = {}, || ''
// 위 부분은 아직 api가 없어 전달할 데이터가 존재하지 않기 떄문에 추가한 내용
// api가 만들어지면, 추후 확인 후 수정할 것
// 수정 시 data가 없거나 잘못 들어온 경우를 고려해야 함을 유의하기
const ProfileHeader = ({ data = {}, type }) => {

  const profileData = {}

  if (type === 'plant') {
    profileData.imgSrc = data.profile || '';
    profileData.title = data.plantTypeId || 'Unknown Plant';
    profileData.nickname = data.nickname || 'Unknown Nickname';
    profileData.bio = data.bio || '자기소개가 비어 있습니다.';
  } else if (type === 'user') {
    profileData.imgSrc = data.profile || '';
    profileData.title = data.title || 'Unknown Plant';
    profileData.nickname = data.nickname || 'Unknown Nickname';
    profileData.bio = data.bio || '자기소개가 비어 있습니다.';
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
      />
    </div>
  );
};

export default ProfileHeader;