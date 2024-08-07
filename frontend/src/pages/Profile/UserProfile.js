import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import SnsProfileTab from '../../components/Profile/SnSProfileTab';
import ProfilePlantTagList from '../../components/Profile/ProfilePlantTagList';
import axios from 'axios';
import ProfileHeader from '../../components/Profile/ProfileHeader';

const UserProfile = () => {
  const { searchId } = useParams();
  console.log('searchId:', searchId)
  const URI = 'https://i11b308.p.ssafy.io/api';

  const [userData, setUserData] = useState(null);
  const [activeTab, setActiveTab] = useState('plant');

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`${URI}/user/profile/${searchId}`);
        if (response.data && response.data.message === '없는 검색 ID 입니다.') {
          console.error('Error: ', response.data.message);
          return;
        }
        console.log('User Data:', response.data);
        setUserData(response.data);
      } catch (error) {
        console.error("***UserData Error:***", error);
      }
    };

    fetchUserData();
  }, [searchId]);

  if (!userData) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <ProfileHeader 
        data={userData}
        ownerId={searchId}
        type='user'
      />
      <div>
        <button className={activeTab === 'plant' ? 'active' : ''} onClick={() => setActiveTab('plant')}>식물</button>
        <button className={activeTab === 'sns' ? 'active' : ''} onClick={() => setActiveTab('sns')}>SNS</button>
      </div>
      {activeTab === 'plant' && <ProfilePlantTagList searchId={searchId} />}
      <SnsProfileTab 
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        searchId={searchId}
      />
      
    </div>
  );
};

export default UserProfile;
