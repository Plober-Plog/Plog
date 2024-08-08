import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ProfilePlantTagList from '../../components/Profile/ProfilePlantTagList';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import ProfilePlantCardList from '../../components/Article/ProfilePlantCardList';
import ProfileSnsCardList from '../../components/Article/ProfileSnsCardList';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';

const UserProfile = () => {
  const { searchId } = useParams();
  const authSearchId = useAuthStore((state) => state.getSearchId());
  const navigate = useNavigate();

  const [userData, setUserData] = useState(null);
  const [activeTab, setActiveTab] = useState('plant');
  const [filteredPlants, setFilteredPlants] = useState([]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await API.get(`/user/profile/${searchId}`);
        if (response.data && response.data.message === '없는 검색 ID 입니다.') {
          console.error('Error: ', response.data.message);
          return;
        }
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

  const handleAddPlant = () => {
    navigate(`/plant/register`, { state: { plantId: 0 } });
  };

  const handleAddSns = () => {
    navigate(`/sns/write`);
  };

  const handleFilterUpdate = (plants) => {
    setFilteredPlants(plants);
  };

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

      {activeTab === 'plant' && (
        <>
          {searchId === authSearchId && <button onClick={handleAddPlant}>plantAdd</button>}
          <ProfilePlantTagList 
            searchId={searchId}
            onFilterUpdate={handleFilterUpdate}
          />
          <ProfilePlantCardList
            searchId={searchId}
            plants={filteredPlants}
          />
        </>
      )}
      
      {activeTab === 'sns' && (
        <>
          <button onClick={handleAddSns}>snsAdd</button>
          <ProfileSnsCardList 
            searchId={searchId}
          />
        </>
      )}
    </div>
  );
};

export default UserProfile;
