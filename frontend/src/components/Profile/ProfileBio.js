import React from 'react';

// 상황에 따라 다른 component를 보여줘야 함
// 01. 유저일 경우
  // 01-1. 본인일 경우 : 북마크 아이콘
  // 01-2. 이웃이 아닐 경우 : 이웃 신청 Btn
  // 01-3. 이웃일 경우 : 서로이웃 신청 Btn, 이웃 취소 Btn
  // 01-4. 서로이웃일 경우 : 이웃 취소 Btn
    // (추후 모달로 이웃으로 전환, 완전히 이웃 끊기 선택지 제공)
// 02. 식물일 경우
  // 알람, 편집, 일지, 고정 아이콘
  // 해당 아이콘은 상태에 따라 on/off를 다르게 보여주어야 함

// 현재, 요청 user와 페이지 user의 관계 (본인, 이웃, 서로이웃, 친구아님)
// 를 ProfileBio에서 확인할지, ProfileHeader에서 확인할지는 미정이기 때문에
// 그 부분을 체크하는 것은 구현하지 않았음 (추후 SNS 추가할 때에 손볼 예정)

const ProfileBio = ({ bio, type }) => {

  const renderUserButtons = () => (
    <div>
      <button>이웃 신청</button>
      <button>서로이웃 신청</button>
      <button>이웃 끊기</button>
    </div>
  );

  const renderPlantIcons = () => (
    <div>
      <i title ="알람"/>
      <i title ="편집"/>
      <i title ="일지"/>
      <i title ="고정"/>
    </div>
  );


  return (
    <div>
      <span>{bio}</span>
      {type === 'user' ? renderUserButtons() : renderPlantIcons()}
    </div>
  );
};

export default ProfileBio;