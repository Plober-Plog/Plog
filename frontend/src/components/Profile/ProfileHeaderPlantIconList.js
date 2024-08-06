import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const ProfileHeaderPlantIconList = ({ ownerId, hasNotified, isFixed, profileData }) => {

  const URI = 'https://i11b308.p.ssafy.io/api';
  const TOKEN = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDg3MDYsImlhdCI6MTcyMjgzOTEwNn0.zyGGYRJrG4SELAACBabt-AiBKPOC_TvVsBZdrk8IfZQ'

  const navigate = useNavigate();
  const [nowNotified, setNowNotified] = useState(hasNotified);
  const [nowFixed, setNowFixed] = useState(isFixed);

  const handleToggleFixed = async () => {
    const updatedFixedStatus = !nowFixed;

    try {
      const response = await axios.patch(`${URI}/user/plant/${ownerId}/fix`, 
        { isFixed: updatedFixedStatus },
        {
          headers: {
            'Authorization': `${TOKEN}`,
            'Content-Type': 'application/json'
          }
        }
      );

      if (response.status === 200) {
        setNowFixed(updatedFixedStatus);
      } else {
        console.error('Failed to update fixed status', response.data);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };


  /* TODO: [예진] 윤서가 알람 api PATCH 메서드 추가해주면 이 부분 수정할 것
    아마도 알람 api를 따로 뺄 거라고 합니다
  */
  const handleToggleNotification = async () => {
    const updatedNotificationStatus = !nowNotified;
    const updatedPlantData = {
      ...profileData,
      hasNotified: updatedNotificationStatus
    };

    try {
      const response = await axios.patch(`${URI}/user/plant/${ownerId}`, updatedPlantData, {
        headers: {
          'Authorization': `${TOKEN}`,
          'Content-Type': 'application/json'
        },
      });

      if (response.status === 200) {
        setNowNotified(updatedNotificationStatus);
      } else {
        console.error('Failed to update notification status');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleEdit = () => {
    navigate(`/plant/register/${ownerId}`);
  }

  const handleWriteDiary = () => {
    const currentDate = new Date().toISOString().split('T')[0];
    navigate(`/plant/${ownerId}/${currentDate}/write`, {
      state: {
        date: currentDate,
        plantId: ownerId
      }
    });
  };

  return (
    <div>
      <i title="알람" onClick={handleToggleNotification}>
        {nowNotified ? '🔔' : '🔕'}
      </i>
      <i title="편집" onClick={handleEdit}>✏️</i>
      <i title="일지" onClick={handleWriteDiary}>📒</i>
      <i title="고정" onClick={handleToggleFixed}>
        {nowFixed ? '📌' : '❌'}
      </i>
    </div>
  );
};

export default ProfileHeaderPlantIconList;
