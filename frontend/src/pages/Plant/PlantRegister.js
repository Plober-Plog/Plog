import React from 'react';
import PlantProfileUpdateForm from '../../components/Plant/PlantProfileUpdateForm';

const PlantRegister = () => {
  const plantData = {}

  return (
    <div>
      <h1>식물정보 수정</h1>
      <PlantProfileUpdateForm 
        plantData = {plantData}
      />
    </div>
  )
}

export default PlantRegister;