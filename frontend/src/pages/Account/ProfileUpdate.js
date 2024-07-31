import React from 'react';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';

const ProfileUpdate = () => {
  const userData = {}

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