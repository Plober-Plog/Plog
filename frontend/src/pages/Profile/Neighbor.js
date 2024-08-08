import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../../apis/api';

const Neighbor = () => {

  const [activeTab, setActiveTab] = useState('follow');
  const [followList, setFollowList] = useState([]);
  const [followerList, setFollowerList] = useState([]);
  const { searchId } = useParams();

  const fetchFollowList = async () => {
    try {
      const response = await API.get(`/user/neighbor/${searchId}/to`)
      setFollowList(response.data);
      console.log('followList', followList)
    } catch (error) {
      console.error("팔로우 목록 에러", error);
    }
  }

  const fetchFollowerList = async () => {
    try {
      const response = await API.get(`/user/neighbor/${searchId}/from`)
      setFollowerList(response.data);
      console.log('followerList', followList)
    } catch (error) {
      console.error("팔로워 목록 에러", error);
    }
  }

  useEffect(() => {
    if (activeTab === 'follow') {
      fetchFollowList();
    } else {
      fetchFollowerList();
    }
  }, [activeTab, searchId]);

  return (
    <div>
      <h3>이웃 목록</h3>
      <div>
        <button className={activeTab === 'follow' ? 'active' : ''} onClick={() => setActiveTab('follow')}>팔로우</button>
        <button className={activeTab === 'follower' ? 'active' : ''} onClick={() => setActiveTab('follower')}>팔로워</button>
      </div>
      
      {activeTab === 'follow' && (
        <>
          <h5>팔로우 목록</h5>
          <ul>
            {followList.map((user) => (
              <li key={user.id}>
                <img src={user.profileImage} alt={`${user.nickname} profile`} width="50" height="50" />
                <span>{user.nickname}</span>
              </li>
            ))}
          </ul>
        </>
      )}
      
      {activeTab === 'follower' && (
        <>
          <h5>팔로워 목록</h5>
        </>
      )}
    </div>
  )
}

export default Neighbor;