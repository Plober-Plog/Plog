import React, { useState, useEffect } from 'react';
import useAuthStore from '../../stores/member';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';
import './Account.css';

const ProfileUpdate = () => {
  const userData = useAuthStore((state) => state.userData);

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
