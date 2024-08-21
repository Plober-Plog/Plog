<<<<<<< HEAD
import { useState, useEffect } from 'react';
import ProfilePlantCard from './ProfilePlantCard';
import API from '../../apis/api';

const ProfilePlantCardList = ({ searchId }) => {

  const [plants, setPlants] = useState([]);
=======
import React, { useState, useEffect } from 'react';
import ProfilePlantCard from './ProfilePlantCard';
import useAuthStore from '../../stores/member';
import API from '../../apis/api';
import './ProfilePlantCardList.css';

const ProfilePlantCardList = ({ plants, searchId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());
>>>>>>> master
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

<<<<<<< HEAD
  const fetchPlants = async (searchId, page) => {
    setLoading(true);
    try {
      const response = await API.get(`/user/plant`, {
        params: { searchId, page }
      });

      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        if (page === 0) {
          setPlants(response.data);
        } else {
          setPlants((prevPlants) => [...prevPlants, ...response.data]);
        }
        setPage(page + 1);
      }
    } catch (error) {
      console.error('Fetch Plants Error:', error);
=======
  const fetchPlantList = async (searchId, page) => {
    setLoading(true);
    try {
      const response = await API.get('/user/plant', {
        params: { searchId, page }
      });
      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        setPage(page + 1);
      }
    } catch (error) {
      console.error('Fetch PlantList Error:', error);
>>>>>>> master
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
<<<<<<< HEAD
    fetchPlants(searchId, 0);
=======
    fetchPlantList(searchId, 0);
>>>>>>> master
  }, [searchId]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight && hasMore && !loading) {
<<<<<<< HEAD
      fetchPlants(searchId, page);
=======
      fetchPlantList(searchId, page);
>>>>>>> master
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [hasMore, page, loading]);

  return (
<<<<<<< HEAD
    <div>
      {plants.map((plant) => (
        <ProfilePlantCard
          key={plant.plantId}
          plantId={plant.plantId}
          profile={plant.profile}
          nickname={plant.nickname}
          plantTypeId={plant.plantTypeId}
          plantTypeName={plant.plantTypeName}
          birthDate={plant.birthDate}
        />
      ))}
=======
    <div className='profile-plant-card-list'>
      {plants.map((plant) => {
        const displayPlantTypeName = plant.plantTypeName === '기타' ? plant.otherPlantTypeName : plant.plantTypeName;

        return (
          <ProfilePlantCard
            key={plant.plantId}
            plantId={plant.plantId}
            profile={plant.profile}
            nickname={plant.nickname}
            plantTypeName={displayPlantTypeName} // 여기서 처리
            otherPlantTypeName={plant.otherPlantTypeName }
            birthDate={plant.birthDate}
            deadDate={plant.deadDate}
            isClickable={authSearchId === searchId}
            isDeleted={plant.isDeleted}
          />
        );
      })}
>>>>>>> master
      {loading && <div>Loading...</div>}
    </div>
  )
}

export default ProfilePlantCardList;
