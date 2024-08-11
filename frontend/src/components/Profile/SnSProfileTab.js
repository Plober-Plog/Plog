import react from 'react';
import ProfilePlantCardList from '../Article/ProfilePlantCardList';
import ProfileSnsCardList from '../Article/ProfileSnsCardList'

const SnsProfileTab = ({ activeTab, searchId }) => {
  return (
    <div>
      {activeTab === 'plant' && <ProfilePlantCardList searchId={searchId} />}
      {activeTab === 'sns' && <ProfileSnsCardList searchId={searchId} />}
      {activeTab === 'bookmark' && <ProfileSnsCardList searchId={searchId} />}
    </div>
  )
}

export default SnsProfileTab;