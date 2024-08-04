import React, { useState, useEffect } from 'react';
import { useNavigate } from'react-router-dom';

/* TODO: 데이터 PATCH 할 때 유저 권한 체크하는 부분은 해당 파트 맡은 팀원이 기능 구현한 이후 추가할 예정
  fixed의 경우 BE에서 관련 api명세서 수정한 뒤 추가할 예정
*/
const ProfileHeaderPlantIconList = ({ plantId, hasNotified, isFixed }) => {

  const navigate = useNavigate();
  const [nowNotified, setNowNotified] = useState(hasNotified);

  const handleToggleFixed = () => {}

  const handleToggleNotification = () => {
    setNowNotified((prev) => !prev);
  };
  
  const handleEdit = () => {
    navigate(`/plant/register/${plantId}`);
  }

  const handleWriteDiary = () => {
    const currentDate = new Date().toISOString().split('T')[0];
    navigate(`/plant/${plantId}/${currentDate}/write`, {
      // 혹시 몰라 state로도 날짜를 보내겠습니다.
      state: {
        date: currentDate,
        plantId: plantId
      }
    });
  };

  return (
    <div>
      <i title ="알람" onClick={handleToggleNotification}>
        {nowNotified ? '🔔' : '🔕'}
      </i>
      <i title ="편집" onClick={handleEdit}>✏️</i>
      <i title ="일지" onClick={handleWriteDiary}>📒</i>
      <i title ="고정" onClick={handleToggleFixed}>
        {isFixed? '📌' : '❌'  }
      </i>
    </div>
  );
};

export default ProfileHeaderPlantIconList;