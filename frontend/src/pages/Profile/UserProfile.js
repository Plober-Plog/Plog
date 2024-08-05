import React, { useEffect, useState } from 'react';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import ArticleCardList from '../../components/Article/ArticleCardList';
import axios from 'axios';

const UserProfile = ({ userId = 1 }) => {

  const URI = 'https://i11b308.p.ssafy.io/api'

  const [userData, setUserData] = useState(null);
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`${URI}/user/plant/${userId}/info`);
        setUserData(response.data);
      } catch (error) {
        console.error("UserData Error:", error.response.data);
      }
    };

    const fetchArticles = async () => {
      try {
        const response = await axios.get(`${URI}/user/plant/${userId}/diary`);
        setArticles(response.data);
      } catch (error) {
        console.error("CardList Error:", error.response.data);
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
        data={{ ...userData, ownerId: userId}}
        type='user'
      />
      <ArticleCardList
        ownerId={userId}
        articles={articles}
        type="user"
      />
    </div>
  )
};

export default UserProfile;