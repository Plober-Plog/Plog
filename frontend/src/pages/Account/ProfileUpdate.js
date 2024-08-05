import React, { useState, useEffect } from 'react';
import API from '../../apis/api';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';

const ProfileUpdate = () => {
  const [userData, setUserData] = useState({});

    // 프로필 정보 불러오기
    useEffect(() => {
      const fetchUserData = async () => {
        try {
          console.log('유정 정보 불러오기...');
          const response = await API.get('/user');
          console.log('응답 데이터:', response);
          const data = response.data;
          console.log('유저 데이터:', data);
          setUserData(data);
        } catch (error) {
          console.error('유저 정보 불러오기 실패 : ', error);
        }
      };

      fetchUserData();
    }, []);

  return (
    <div>
      <h1>회원정보 수정</h1>
      <ProfileUpdateForm 
        userData={userData}
      />
    </div>
  );
}

export default ProfileUpdate;