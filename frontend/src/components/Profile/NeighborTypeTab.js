
import React, { useEffect } from 'react';
import './Tab.css';

const NeighborTypeTab = ({ activeTab, setActiveTab }) => {

  useEffect(() => {
    console.log('Active Tab:', activeTab);
  }, [activeTab]);

  return (
    <div className="w-screen max-w-none visibility-selector">
      <button
        onClick={() => setActiveTab('follow')}
        className={activeTab === 'follow' ? 'active' : ''}
      >
        팔로우
      </button>
      <button
        onClick={() => setActiveTab('follower')}
        className={activeTab === 'follower' ? 'active' : ''}
      >
        팔로워
      </button>
    </div>
  );
};

export default NeighborTypeTab;