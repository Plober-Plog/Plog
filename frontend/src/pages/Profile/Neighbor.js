import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import NeighborCardList from '../../components/Profile/NeighborCardList';
import NeighborTypeTab from '../../components/Profile/NeighborTypeTab';

const Neighbor = () => {

  const [activeTab, setActiveTab] = useState('follow');
  const [followList, setFollowList] = useState([]);
  const [followerList, setFollowerList] = useState([]);
  const { searchId } = useParams();

  const fetchFollowList = async () => {
    try {
      const response = await API.get(`/user/neighbor/${searchId}/from`)
      setFollowList(response.data);
      console.log('followList', followList)
    } catch (error) {
      console.error("팔로우 목록 에러", error);
    }
  }

  const fetchFollowerList = async () => {
    try {
      const response = await API.get(`/user/neighbor/${searchId}/to`)
      setFollowerList(response.data);
      console.log('followerList', followerList)
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

      <NeighborTypeTab activeTab={activeTab} setActiveTab={setActiveTab} />

      <div className='pt-20'>

        {activeTab === 'follow' && (
          <>
            <NeighborCardList 
              users={followList}
            />
          </>
        )}
        
        {activeTab === 'follower' && (
          <>
            <NeighborCardList 
              users={followerList}
            />
          </>
        )}
        
      </div>



    </div>
  )
}

export default Neighbor;