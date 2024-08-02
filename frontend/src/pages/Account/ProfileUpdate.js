import React, { useState, useEffect } from 'react';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';

const ProfileUpdate = () => {
  const [userData, setUserData] = useState({});

    // 프로필 정보 불러오기
    useEffect(() => {
      const fetchUserInfo = async () => {
        try {
          const userInfo = await getUserInfo();
          setUserData(userInfo);
        } catch (error) {
          console.error('사용자 정보 불러오기 실패:', error);
        }
      };
  
      fetchUserInfo();
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