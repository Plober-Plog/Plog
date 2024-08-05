import React, { useEffect, useState } from 'react';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import ProfilePlantCardList from '../../components/Article/ProfilePlantCardList';
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
  
  /* TODO: [예진] 윤서가 Plant Type 리스트 반환해주는 API 만들어 주면 그걸 보여주는 컴포넌트를 만들어 추가해야합니다
    누르면 UI 토글이 되고,
    해당 값을 ProfilePlantCardList의 axios GET 요청의 params로 함께 넘겨줘야 합니다.
  */

  /* TODO: [예진] 아영이가 상태관리쪽을 작업한 뒤,
    searchId를 쉽게 반환할 수 있도록 만들어주면 아래 하드코딩을 수정해야 합니다.
  */

  /* TODO: [예진] 탭 컴포넌트를 만들어 추가해야합니다. */

  return (
    <div>
      <ProfileHeader 
        data={{ ...userData, ownerId: userId}}
        type='user'
      />
      <ProfilePlantCardList
        searchId='zpqmdh'
      />
    </div>
  )
};

export default UserProfile;