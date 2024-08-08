import React from 'react';
import ProfilePlantCard from './ProfilePlantCard';
import useAuthStore from '../../stores/member';

const ProfilePlantCardList = ({ plants, searchId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());

  return (
    <div>
      <h3>유저의 식물 목록</h3>
      {plants.map((plant) => (
        <ProfilePlantCard
          key={plant.plantId}
          plantId={plant.plantId}
          profile={plant.profile}
          nickname={plant.nickname}
          // plantTypeId={plant.plantTypeId}
          // otherPlantTypeId={plant.otherPlantTypeId}
          plantTypeName={plant.plantTypeName}
          otherPlantTypeName ={plant.otherPlantTypeName}
          birthDate={plant.birthDate}
          isClickalbe={authSearchId === searchId}
        />
      ))}
    </div>
  )
}

export default ProfilePlantCardList;
