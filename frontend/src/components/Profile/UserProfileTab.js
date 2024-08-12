import React, { useEffect } from 'react';
import './UserProfileTab.css';

const ProfileTab = ({ activeTab, setActiveTab }) => {

  return (
    <div className="user-profile-tab-container">
      <button
        onClick={() => setActiveTab('plant')}
        className={activeTab === 'plant' ? 'active' : ''}
      >
        식물
      </button>
      <button
        onClick={() => setActiveTab('sns')}
        className={activeTab === 'sns' ? 'active' : ''}
      >
        SNS
      </button>
      <button
        onClick={() => setActiveTab('bookmark')}
        className={activeTab === 'bookmark' ? 'active' : ''}
      >
        북마크
      </button>
    </div>
  );
};

export default ProfileTab;
