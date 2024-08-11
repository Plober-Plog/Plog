import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';

const ProfileHeaderPlantIconList = ({ ownerId, hasNotification, isFixed, profileData }) => {

  const navigate = useNavigate();
  const [nowNotified, setNowNotified] = useState(hasNotification);
  const [nowFixed, setNowFixed] = useState(isFixed);

  const handleToggleFixed = async () => {
    const updatedFixedStatus = !nowFixed;

    try {
      const response = await API.patch(`/user/plant/${ownerId}/fix`, 
        { isFixed: updatedFixedStatus },
        {
          headers: {
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


  const handleToggledNotification = async () => {
    const updatedNotificationStatus = !nowNotified;

    try {
      const response = await API.patch(`/user/plant/${ownerId}/notification`,
        { hasNotification: updatedNotificationStatus},
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );

      if (response.status === 200) {
        setNowNotified(updatedNotificationStatus);
      } else {
        console.error('Fail to update notification status', response.data);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  const handleEdit = () => {
    navigate(`/plant/register`,
      { state: { plantId: ownerId } }
    );
  }

  const handleWriteDiary = async () => {
    const currentDate = new Date().toISOString().split('T')[0];
  
    try {
      const response = await API.get(`/user/plant/${ownerId}`, {
        params: {date: currentDate }
      })

      if (response.data.plantDiary || response.data.plantCheck) {
        navigate(`/plant/${ownerId}/${currentDate}`, {
          state: {
            date: currentDate,
            plantId: ownerId
          }
        });
      } else {
        navigate(`/plant/${ownerId}/${currentDate}/write`, {
          state: {
            date: currentDate,
            plantId: ownerId
          }
        })
      }
    } catch (error) {
      console.error('***일지 및 관리기록 체크하는 파트에서 오류***', error);
    }
  }

  return (
    <div>
      <i title="알람" onClick={handleToggledNotification}>
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
