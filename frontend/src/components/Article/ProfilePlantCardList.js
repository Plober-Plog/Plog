import { useState, useEffect } from 'react';
import ProfilePlantCard from './ProfilePlantCard';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';

const ProfilePlantCardList = ({ searchId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());

  const [plants, setPlants] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

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
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPlants(searchId, 0);
  }, [searchId]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight && hasMore && !loading) {
      fetchPlants(searchId, page);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [hasMore, page, loading]);

  return (
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
          isClickalbe={authSearchId === searchId}
        />
      ))}
      {loading && <div>Loading...</div>}
    </div>
  )
}

export default ProfilePlantCardList;
