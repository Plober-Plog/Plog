import react, {useState} from 'react';

const Neighbor = () => {

  const [activeTab, setActiveTab] = useState('follow');

  return (
    <div>
      <h3>이웃 목록</h3>
      <div>
        <button className={activeTab === 'follow' ? 'active' : ''} onClick={() => setActiveTab('follow')}>팔로우</button>
        <button className={activeTab === 'follower' ? 'active' : ''} onClick={() => setActiveTab('follower')}>팔로워</button>
      </div>

    </div>
  )
}

export default Neighbor;