import React from 'react';
import fertilizedFill from '../../assets/icon/fertilized-select.png';
import waterFill from '../../assets/icon/water-select.png';
import repottedFill from '../../assets/icon/repotted-select.png';

const AlarmItem = ({ alarm }) => {
  // 이미지 출력 선택
  const getImageSrc = () => {
    if (alarm.type === 1 || alarm.type === 2 || alarm.type === 3 || alarm.type === 7) {
      return alarm.image;
    } else if (alarm.type === 13) {
      return waterFill;
    } else if (alarm.type === 15) {
      return repottedFill;
    } else if (alarm.type === 14) {
      return fertilizedFill;
    } else {
      return alarm.image;
    }
  };

  // 관련 페이지로 이동
  const handleMove = () => {
    window.location.href = alarm.clickUrl;
  };

  return (
    <div className='alarm-item-box'>
      <img 
        src={getImageSrc()}
        alt='프로필 이미지'
        className='alarm-image'
      />
      <span
        onClick={handleMove}
        className='alarm-content'
      >
        {alarm.content}
      </span>
    </div>
  );
}

export default AlarmItem;
