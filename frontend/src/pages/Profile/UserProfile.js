import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
<<<<<<< HEAD
import SnsProfileTab from '../../components/Profile/SnSProfileTab';
import ProfilePlantTagList from '../../components/Profile/ProfilePlantTagList';
import axios from 'axios';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import API from '../../apis/api';

const UserProfile = () => {
  const { searchId } = useParams();
  // const URI = 'https://i11b308.p.ssafy.io/api';
=======
import ProfilePlantTagList from '../../components/Profile/ProfilePlantTagList';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import ProfilePlantCardList from '../../components/Article/ProfilePlantCardList';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import SnsCardMdList from '../../components/Article/SnsCardMdList';
import AddBtn from '../../components/Common/AddBtn';
import './UserProfile.css';
import UserProfileTab from '../../components/Profile/UserProfileTab'
import BtnChat from '../../components/Profile/BtnChat';

const UserProfile = () => {
  const { searchId } = useParams();
  const authSearchId = useAuthStore((state) => state.getSearchId());
>>>>>>> master
  const navigate = useNavigate();

  const [userData, setUserData] = useState(null);
  const [activeTab, setActiveTab] = useState('plant');
<<<<<<< HEAD
=======
  const [filteredPlants, setFilteredPlants] = useState([]);
  const [allPlants, setAllPlants] = useState([]);
>>>>>>> master

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await API.get(`/user/profile/${searchId}`);
<<<<<<< HEAD
        // const response = await axios.get(`${URI}/user/profile/${searchId}`);
=======
>>>>>>> master
        if (response.data && response.data.message === '없는 검색 ID 입니다.') {
          console.error('Error: ', response.data.message);
          return;
        }
<<<<<<< HEAD
        console.log('User Data:', response.data);
        setUserData(response.data);
=======
        setUserData(response.data);

        const plantResponse = await API.get('/user/plant', {
          params: { searchId }
        });
        setAllPlants(plantResponse.data);
        setFilteredPlants(plantResponse.data);
>>>>>>> master
      } catch (error) {
        console.error("***UserData Error:***", error);
      }
    };

    fetchUserData();
  }, [searchId]);

  if (!userData) {
    return <div>Loading...</div>;
  }

<<<<<<< HEAD
  const handleAddPlant = () => {
    navigate(`/plant/register`,
      { state: { plantId: '0' } }
    );
  };

  const handleAddSns = () => {}

=======
  const handleFilterUpdate = (plants) => {
    setFilteredPlants(plants);
  };

>>>>>>> master
  return (
    <div>
      <ProfileHeader 
        data={userData}
        ownerId={searchId}
        type='user'
      />
<<<<<<< HEAD
      <div>
        <button className={activeTab === 'plant' ? 'active' : ''} onClick={() => setActiveTab('plant')}>식물</button>
        <button className={activeTab === 'sns' ? 'active' : ''} onClick={() => setActiveTab('sns')}>SNS</button>
      </div>

      {activeTab === 'plant' && (
        <>
          <button onClick={handleAddPlant}>plantAdd</button>
          <ProfilePlantTagList searchId={searchId} />
=======
      <div className='mb-20'>
        <UserProfileTab activeTab={activeTab} setActiveTab={setActiveTab} />
      </div>

      {searchId !== authSearchId && (
        <BtnChat 
          userData={userData}
        />
      )}

      {activeTab === 'plant' && (
        <>
          {searchId === authSearchId && (
            <AddBtn 
              type='plant'
            />
          )}
          <ProfilePlantTagList 
            searchId={searchId}
            onFilterUpdate={handleFilterUpdate}
          />
          <ProfilePlantCardList
            plants={filteredPlants}
            searchId={searchId}
          />
>>>>>>> master
        </>
      )}
      
      {activeTab === 'sns' && (
        <>
<<<<<<< HEAD
          <button onClick={handleAddSns}>snsAdd</button>
        </>
      )}
      <SnsProfileTab 
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        searchId={searchId}
      />
      
=======
          {searchId === authSearchId && (
            <AddBtn 
              type='sns'
            />
          )}
          <SnsCardMdList 
            type='sns'
            searchId={searchId}
          />
        </>
      )}

      {activeTab === 'bookmark' && (
        <>
          {/* <AddBtn 
            type='bookmark'
          /> */}
          <SnsCardMdList 
            type='bookmark'
            searchId={searchId}
          />
        </>
      )}
>>>>>>> master
    </div>
  );
};

export default UserProfile;
