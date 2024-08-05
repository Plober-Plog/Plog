import React, { useState, useEffect } from 'react';
import API from '../../apis/api';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';
import './Account.css';

const ProfileUpdate = () => {
  const [userData, setUserData] = useState({});

  // 프로필 정보 불러오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        console.log('유저 정보 불러오기...');
        const response = await API.get('/user');
        console.log('응답 데이터:', response);
        console.log('유저 데이터:', response.data);
        setUserData(response.data);
      } catch (error) {
        console.error('유저 정보 불러오기 실패 : ', error);
      }
    };

    fetchUserData();
  }, []);

  return (
    <div>
      <h1 className='title'>회원정보 수정</h1>
      <ProfileUpdateForm 
        userData={userData}
      />
    </div>
  );
}

export default ProfileUpdate;
