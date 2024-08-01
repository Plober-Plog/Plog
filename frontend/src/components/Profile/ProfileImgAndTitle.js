import React from'react';
import ProfileTitle from './ProfileTitle';


/* TODO: 프로필 이미지 클릭 시, 프로필 이미지를 바꿀 수 있는 기능과 연결되어야 함
  1. 요청을 준 유저 === 유저(해당 유저 혹은 해당 PlantId의 유저) 확인 후
  2. 만약 일치한다면 프로필 이미지를 바꿀 수 있는 PWA의 기능과 연결
  3. 아닐 경우 무반응
  해당 부분 PWA 학습 후 추가할 예정 */
const ProfileImgAndTitle = ({ imgSrc, title, nickname }) => {
  return (
    <div>
      <div>
        <img src={imgSrc} alt="profile img"/>
      </div>
      <div>
        <ProfileTitle title={title}/>
      </div>
      <div>
        <span>
          {nickname}
        </span>
      </div>
    </div>
  );
};

export default ProfileImgAndTitle;