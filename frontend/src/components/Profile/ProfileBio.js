import React from 'react';

const ProfileBio = ({ bio, type }) => {

  /* TODO: 유저 프로필을 구현하게 될 때
    요청 유저와 페이지 유저의 관계를 체크 후 관계에 따라 다르게 렌더해야 함

    1. 본인일 경우 : 북마크 아이콘
    2. 이웃이 아닐 경우 : 이웃 신청 Btn
    3. 이웃일 경우 : 서로이웃 신청 Btn, 이웃 취소 Btn
    4. 서로이웃일 경우 : 이웃 취소 Btn
      (추후 모달로 이웃으로 전환, 완전히 이웃 끊기 선택지 제공)
  */
  const renderUserButtons = () => (
    <div>
      <button>이웃 신청</button>
      <button>서로이웃 신청</button>
      <button>이웃 끊기</button>
    </div>
  );

  const renderPlantIcons = () => (
    <div>
      <i title ="알람">🔔</i>
      <i title ="편집">✏️</i>
      <i title ="일지">📒</i>
      <i title ="고정">📌</i>
    </div>
  );

  const renderBtnIcon = () => {
    if (type === 'user') {
      return renderUserButtons();
    } else if (type === 'plant') {
      return renderPlantIcons();
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