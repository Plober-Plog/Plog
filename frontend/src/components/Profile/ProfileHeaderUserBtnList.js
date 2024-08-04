import React from 'react';

/* TODO: 추후 유저 프로필을 구현하게 될 때
  요청 유저와 페이지 유저의 관계를 체크 후 관계에 따라 다르게 렌더해야 함

  1. 본인일 경우 : 북마크 아이콘
  2. 이웃이 아닐 경우 : 이웃 신청 Btn
  3. 이웃일 경우 : 서로이웃 신청 Btn, 이웃 취소 Btn
  4. 서로이웃일 경우 : 이웃 취소 Btn
    (추후 모달로 이웃으로 전환, 완전히 이웃 끊기 선택지 제공)
*/

const ProfileHeaderUserBtnList = ({}) => {
  return (
    <div>
      <button>이웃 신청</button>
      <button>서로이웃 신청</button>
      <button>이웃 끊기</button>
    </div>
  );
};

export default ProfileHeaderUserBtnList;