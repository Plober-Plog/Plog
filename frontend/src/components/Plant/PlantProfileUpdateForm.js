import React, { useState, useEffect } from 'react';
import Btn from "../../components/Common/Btn";
import defaultImg from '../../assets/icon/default.png';
import ProfileImgEdit from '../../components/Profile/ProfileImgEdit';


const PlantProfileUpdateForm = ({ plantData }) => {
  // 종, 닉네임, 생일, 소개, 이미지
  /* TODO: otherPlantTypeId일 경우도 추가 */
  const [plantTypeId, setPlantTypeId] = useState(plantData.plantTypeId);
  const [nickname, setNickname] = useState(plantData.nickname);
  const [birthDate, setBirthDate] = useState(plantData.birthDate);
  const [bio, setBio] = useState(plantData.bio);
  const [profile, setProfile] = useState(plantData.profile);

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