import React, { useEffect, useState } from 'react';
import ProfileHeader from '../../components/Profile/ProfileHeader';

const UserProfile = ({ userId = 1 }) => {

  const URI = 'https://i11b308.p.ssafy.io/api'

  const [userData, setUserData] = useState(null);
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`${URI}/user/plant/${userId}/info`);
        const data = await response.json();
        setUserData(data);
      } catch (error) {
        console.error("UserData Error:", error);
      }
    };

    const fetchArticles = async () => {
      try {
        const response = await fetch(`${URI}/user/plant/${userId}/diary`);
        const data = await response.json();
        setArticles(data);
      } catch (error) {
        console.error("CardList Error:", error)
      }
    };

    fetchUserData();
    fetchArticles();
  }, [userId]);

  if (!userData) {
    return <div>Loading</div>
  };

  return (
    <div>
      <ProfileHeader 
        data={userData}
        type='user'
      />
    </div>
  )
};

export default UserProfile;