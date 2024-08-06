// src/pages/TestProfile/ProfilePage.js
import React from 'react';
import { useParams } from 'react-router-dom';
// import ProfileHeader from '../../components/Common/ProfileHeader';
import PlantList from './PlantList';

const ProfilePage = () => {
  const { searchId } = useParams(); // URL 파라미터에서 searchId 받아오기

  return (
    <div>
      {/* <ProfileHeader data={{ searchId }} type="user" /> */}
      <div className="tabs">
        <button className="tab active">식물</button>
        <button className="tab">게시글</button>
      </div>
      <div className="tab-content">
        <PlantList searchId={searchId} />
      </div>
    </div>
  );
};

export default ProfilePage;
