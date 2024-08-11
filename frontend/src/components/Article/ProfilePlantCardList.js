import React, { useState, useEffect } from 'react';
import ProfilePlantCard from './ProfilePlantCard';
import useAuthStore from '../../stores/member';
import API from '../../apis/api';
import './ProfilePlantCardList.css';

const ProfilePlantCardList = ({ plants, searchId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

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
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPlantList(searchId, 0);
  }, [searchId]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight && hasMore && !loading) {
      fetchPlantList(searchId, page);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [hasMore, page, loading]);

  return (
    <div className='profile-plant-card-list'>
      {plants.map((plant) => (
        <ProfilePlantCard
          key={plant.plantId}
          plantId={plant.plantId}
          profile={plant.profile}
          nickname={plant.nickname}
          plantTypeName={plant.plantTypeName}
          otherPlantTypeName={plant.otherPlantTypeName}
          birthDate={plant.birthDate}
          isClickable={authSearchId === searchId}
        />
      ))}
      {loading && <div>Loading...</div>}
    </div>
  )
}

export default ProfilePlantCardList;
