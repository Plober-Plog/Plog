import { useState, useEffect } from 'react';
import axios from 'axios';
import ProfilePlantCard from './ProfilePlantCard';

const ProfilePlantCardList = ({ searchId }) => {

  const URI = 'https://i11b308.p.ssafy.io/api';
  const TOKEN = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDg3MDYsImlhdCI6MTcyMjgzOTEwNn0.zyGGYRJrG4SELAACBabt-AiBKPOC_TvVsBZdrk8IfZQ';

  const [plants, setPlants] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const fetchPlants = async (searchId, page) => {
    setLoading(true);
    try {
      const response = await axios.get(`${URI}/user/plant`, {
        params: { searchId, page },
        headers: { 'Authorization': TOKEN }
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
          birthDate={plant.birthDate}
        />
      ))}
      {loading && <div>Loading...</div>}
    </div>
  )
}

export default ProfilePlantCardList;