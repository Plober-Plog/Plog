import React, { useState, useEffect } from 'react';
import Btn from "../../components/Common/Btn";
import defaultImg from '../../assets/icon/default.png';
import ProfileImgEdit from '../../components/Profile/ProfileImgEdit';


const PlantProfileUpdateForm = ({ plantData }) => {

  const [isFormValid, setIsFormValid] = useState(false);

  useEffect(() => {
    // 필수요구사항 확인
    setIsFormValid();
  }, [])

  return (
    <div>
      <Btn 
        content="수정하기"
        disable={!isFormValid}
      />
    </div>
  )
};

export default PlantProfileUpdateForm;