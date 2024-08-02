import React, { useState, useEffect } from 'react';
import Btn from "../../components/Common/Btn";
import defaultImg from '../../assets/icon/default.png';
// import ProfileImgEdit from '../../components/Profile/ProfileImgEdit';
import InputField from '../Account/InputField';
import AccountBtn from '../Account/AccountBtn';
import ProfileImgEdit from '../Profile/ProfileImgEdit';


const PlantProfileUpdateForm = ({ plantData }) => {
  // 종, 닉네임, 생일, 소개, 이미지
  /* TODO: otherPlantTypeId일 경우도 추가 */
  const [id, setId] = useState(plantData.plantId);
  const [plantTypeId, setPlantTypeId] = useState(plantData.plantTypeId);
  const [nickname, setNickname] = useState(plantData.nickname);
  const [birthDate, setBirthDate] = useState(plantData.birthDate);
  const [bio, setBio] = useState(plantData.bio);
  const [profile, setProfile] = useState(plantData.profile);

  const [openModal, setOpenModal] = useState(false);

  const [isFormValid, setIsFormValid] = useState(false);

  useEffect(() => {
    // 필수요구사항 확인
    setIsFormValid(plantTypeId && nickname && birthDate);
  }, [plantTypeId, nickname, birthDate]);

  const handleProfileUpdate = () => {
    console.log('업데이트 정보 받기 성공');
    setOpenModal(true);
  };

  const closeModal = () => {
    setOpenModal(false);
  };



  return (
    <div>
      {/* preventDefault()의 존재 이유 잘 모르겠으나 쓰지 않으면 오류 난다고 함 */}
      <form on onSubmit={(e) => e.preventDefault()}>
        <ProfileImgEdit />
        <div>
          {!isFormValid && <p>식물 종을 선택해주세요.</p>}
          <InputField
            type="text"
            placeholder="식물 종"
            value={plantTypeId}
            onChange={(e) => setPlantTypeId(e.target.value)}
            isrequired={true}
          />
        </div>
        <div>
          {!isFormValid && <p>닉네임을 입력해주세요.</p>}
          <InputField
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            isrequired={true}
          />
        </div>
        <div>
          {!isFormValid && <p>식물 생일을 입력해주세요.</p>}
          <InputField
            type="date"
            placeholder="생일"
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
            isrequired={true}
          />
        </div>
        <div>
          <InputField
            type="text"
            placeholder="식물 소개를 입력해주세요."
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            isrequired={true}
          />
        </div>
        <Btn 
          content="수정하기"
          disable={!isFormValid}
        />
      </form>
    </div>
  )
};

export default PlantProfileUpdateForm;