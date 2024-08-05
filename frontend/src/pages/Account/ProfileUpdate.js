import React, { useState, useEffect } from 'react';
import API from '../../apis/api';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';

const ProfileUpdate = () => {
  const [userData, setUserData] = useState({});

  const URI = 'https://i11b308.p.ssafy.io'

    // 프로필 정보 불러오기
    useEffect(() => {
      const fetchUserData = async () => {
        try {
          const response = await API.get('/user');
          const data = await response.data;
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