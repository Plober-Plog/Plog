import React, { useState, useEffect } from 'react';
import useAuthStore from '../../stores/member';
import ProfileUpdateForm from '../../components/Account/ProfileUpdateForm';
<<<<<<< HEAD
import './Account.css';
=======
// import './Account.css';
>>>>>>> master

const ProfileUpdate = () => {

  return (
<<<<<<< HEAD
    <div>
      <h1 className='title'>회원정보 수정</h1>
=======
    <div className="account-signup-container">
      <h1 className="account-title mt-10">회원정보 수정</h1>
>>>>>>> master
      <ProfileUpdateForm />
    </div>
  );
}

export default ProfileUpdate;
