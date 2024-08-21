import react from 'react';
import ProfilePlantCardList from '../Article/ProfilePlantCardList';
import ProfileSnsCardList from '../Article/ProfileSnsCardList'

<<<<<<< HEAD
const SnsProfileTab = ({ activeTab, setActiveTab, searchId }) => {

  /* TODO: [예진] ProfileSnsCardList 만들기
  만든 뒤 테스트 잘 해보기...
  이 직전까지의 Plant탭은 확인 결과 잘 됐답니다
  sns의 경우 아직 api가 덜 된 상태로 알고 있어서 뼈대만 만들어 뒀습니다
  만약 동작 체크하는데 안 될 경우 (+당장 머지리퀘 등등 해야할 경우)
  -> UserProfile 페이지의 sns 관련된 것 + tab 모두 주석처리 해버리면 됩니다
  */
=======
const SnsProfileTab = ({ activeTab, searchId }) => {
>>>>>>> master
  return (
    <div>
      {activeTab === 'plant' && <ProfilePlantCardList searchId={searchId} />}
      {activeTab === 'sns' && <ProfileSnsCardList searchId={searchId} />}
<<<<<<< HEAD
=======
      {activeTab === 'bookmark' && <ProfileSnsCardList searchId={searchId} />}
>>>>>>> master
    </div>
  )
}

export default SnsProfileTab;