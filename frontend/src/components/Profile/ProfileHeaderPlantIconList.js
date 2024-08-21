import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';

<<<<<<< HEAD
const ProfileHeaderPlantIconList = ({ ownerId, hasNotified, isFixed, profileData }) => {

  const navigate = useNavigate();
  const [nowNotified, setNowNotified] = useState(hasNotified);
=======
import bellIcon from '../../assets/icon/bell-default.png';
import bellSelectIcon from '../../assets/icon/bell-select.png';
import fixIcon from '../../assets/icon/fix-default.png';
import fixSelectIcon from '../../assets/icon/fix-select.png';
import pencilIcon from '../../assets/icon/pencil-select.png';
import docsIcon from '../../assets/icon/docs-select.png';

import './ProfileHeaderPlantIconList.css';

const ProfileHeaderPlantIconList = ({ etcPlantType, ownerId, hasNotification, isFixed, profileData }) => {

  const navigate = useNavigate();
  const [nowNotified, setNowNotified] = useState(hasNotification);
>>>>>>> master
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


<<<<<<< HEAD
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
      const response = await API.patch(`/user/plant/${ownerId}`, updatedPlantData, {
        headers: {
          'Content-Type': 'application/json'
        },
      });
=======
  const handleToggledNotification = async () => {

    if (etcPlantType) {
      return;
    }

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
>>>>>>> master

      if (response.status === 200) {
        setNowNotified(updatedNotificationStatus);
      } else {
<<<<<<< HEAD
        console.error('Failed to update notification status');
=======
        console.error('Fail to update notification status', response.data);
>>>>>>> master
      }
    } catch (error) {
      console.error('Error:', error);
    }
<<<<<<< HEAD
  };
=======
  }
>>>>>>> master

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
<<<<<<< HEAD
    <div>
      <i title="알람" onClick={handleToggleNotification}>
        {nowNotified ? '🔔' : '🔕'}
      </i>
      <i title="편집" onClick={handleEdit}>✏️</i>
      <i title="일지" onClick={handleWriteDiary}>📒</i>
      <i title="고정" onClick={handleToggleFixed}>
        {nowFixed ? '📌' : '❌'}
=======
    <div className='profile-header-icon-container'>
      <i title="알람" className='profile-header-icon' onClick={handleToggledNotification}>
        {nowNotified ? <img src={bellSelectIcon} alt='알람on'/> : <img src={bellIcon} alt='알람off'/>}
      </i>
      <i title="편집" className='profile-header-icon' onClick={handleEdit}>
        <img src={pencilIcon} alt="편집" />
      </i>
      <i title="일지" className='profile-header-icon' onClick={handleWriteDiary}>
        <img src={docsIcon} alt="일지" />
      </i>
      <i title="고정" className='profile-header-icon' onClick={handleToggleFixed}>
        {nowFixed ? <img src={fixSelectIcon} alt='고정on'/> : <img src={fixIcon} alt='고정off'/>}
>>>>>>> master
      </i>
    </div>
  );
};

export default ProfileHeaderPlantIconList;
