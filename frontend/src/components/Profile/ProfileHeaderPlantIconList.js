import React from 'react';
import { useNavigate } from'react-router-dom';

const ProfileHeaderPlantIconList = ({ plantId, hasNotified, isFixed }) => {

  const navigate = useNavigate();

  /* TODO: 예진 / 식물 수정, 식물 조회 API 업데이트 후 추가하기
    1. API-GET: 현재 알람/고정 상태를 받아와서 해당 상태에 맞는 UI를 보여줌
    2. UI: 누르면 알람 버튼이 반대 상태가 됨
    3. API-PATCH: 바뀐 정보를 보냄
  */
  const handleToggleNotification = () => {}
  const handleToggleFixed = () => {}

  const handleEdit = () => {
    navigate(`/plant/write/${plantId}`);
  }

  const handleWriteDiary = () => {
    const currentDate = new Date().toISOString().split('T')[0]; // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
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
        {hasNotified ? '🔔' : '🔕'}
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