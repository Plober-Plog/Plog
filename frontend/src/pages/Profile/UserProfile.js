import React, { useEffect, useState } from 'react';
import ProfileHeader from '../../components/Profile/ProfileHeader';
import SnsProfileTab from '../../components/Profile/SnSProfileTab';
import axios from 'axios';
// import TagFilter from '../../components/Profile/TagFilter';

const UserProfile = ({ userId = 1 }) => {

  const URI = 'https://i11b308.p.ssafy.io/api'

  const [userData, setUserData] = useState(null);
  /* TODO: [예진] 이 부분 현재는 plant가 기본 상태인데
    sns 기능이 완료된 뒤에 sns를 기본 상태로 수정할 것
  */
  const [activeTab, setActiveTab] = useState('plant');

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`${URI}/user/plant/${userId}/info`);
        setUserData(response.data);
      } catch (error) {
        console.error("UserData Error:", error);
      }
    };

    // const fetchTag = async () => {
    //   try {
    //     const response = await axios.get(`${URI}/tags`);
    //     setTags(response.data);
    //   } catch (error) {
    //     console.error("***Tag Error: ***", error.response.data);
    //   }
    // }

    fetchUserData();
    // fetchTag();
  }, [userId]);

  // const handleTagClick = (tagId) => {
  //   if (selectedTags.includes(tagId)) {
  //     setSelectedTags(selectedTags.filter(id => id !== tagId));
  //   } else {
  //     setSelectedTags([...selectedTags, tagId]);
  //   }
  // };

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

  return (
    <div>
      <ProfileHeader 
        data={{ ...userData, ownerId: userId}}
        type='user'
      />
      <SnsProfileTab 
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        searchId={'zpqmdh'}
      />
    </div>
  )
};

export default UserProfile;