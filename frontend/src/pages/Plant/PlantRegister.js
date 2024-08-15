import React, { useState, useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';

import defaultImage from '../../assets/icon/default.png';
import InputField from '../../components/Common/InputField';
import TextareaField from '../../components/Common/TextareaField';
import Btn from '../../components/Common/Btn';
import ModalConfirm from '../../components/Common/ModalConfirm'; 
import ModalComplete from '../../components/Common/ModalComplete'; 
import './PlantRegister.css'; 

const PlantRegister = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId } = location.state;
  const [plantTypeOptions, setPlantTypeOptions] = useState([]);
  const [searchId, setSearchId] = useState(null);

  const [showDeleteModal, setShowDeleteModal] = useState(false); 
  const [showFarewellModal, setShowFarewellModal] = useState(false); 
  const [showDateErrorModal, setShowDateErrorModal] = useState(false); 
  const [showFutureDateErrorModal, setShowFutureDateErrorModal] = useState(false); 
  const [showNicknameErrorModal, setShowNicknameErrorModal] = useState(false);
  const [showOtherPlantErrorModal, setShowOtherPlantErrorModal] = useState(false);

  useEffect(() => {
    const fetchSearchId = useAuthStore.getState().getSearchId;
    const searchId = fetchSearchId();
    setSearchId(searchId);
  }, []);

  const [plantTypeId, setPlantTypeId] = useState(2);
  const [otherPlantName, setOtherPlantName] = useState('');
  const [profile, setProfile] = useState(defaultImage);
  const [bio, setBio] = useState('');
  const [nickname, setNickname] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [isFarewell, setIsFarewell] = useState(false);

  const fileInputRef = useRef(null);
  const [uploadedFile, setUploadedFile] = useState(null);

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfile(reader.result);
      };
      reader.readAsDataURL(file);
      setUploadedFile(file); 
    }
  };

  const handleImageRemove = () => {
    setProfile(defaultImage);
    setUploadedFile(null);
    fileInputRef.current.value = null;
  };

  useEffect(() => {
    const fetchPlantTypeOptions = async () => {
      try {
        const response = await API.get('/user/plant-type/all');
        setPlantTypeOptions(response.data);
        console.log('식물종류:', response.data);
      } catch (error) {
        console.error('식물 종류 불러오기 실패:', error);
      }
    };
    fetchPlantTypeOptions();
  }, []);

  useEffect(() => {
    if (plantId !== 0) {
      const fetchPlantInfo = async () => {
        try {
          const response = await API.get(`/user/plant/${plantId}/info`);
          console.log('식물 정보:', response.data);

          setPlantTypeId(response.data.plantTypeId);
          setOtherPlantName(response.data.otherPlantTypeName);
          setProfile(response.data.profile || defaultImage);
          setBio(response.data.bio);
          setNickname(response.data.nickname);
          setBirthDate(response.data.birthDate);
          setIsFarewell(response.data.deadDate);
        } catch (err) {
          console.error('식물 정보 불러오기 실패 : ', err);
        }
      };
      fetchPlantInfo();
    }
  }, [plantId]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const today = new Date().toISOString().split("T")[0];

    if (!nickname) {
      setShowNicknameErrorModal(true); 
      return;
    }

    if (!birthDate) {
      setShowDateErrorModal(true);
      return;
    }

    if (birthDate > today) {
      setShowFutureDateErrorModal(true); 
      return;
    }

    if (plantTypeId === 1 && !otherPlantName) {
      setShowOtherPlantErrorModal(true);
      return;
    }

    try {
      const formData = new FormData();
      console.log(otherPlantName);

      formData.append('plantTypeId', plantTypeId);
      if (plantTypeId === 1) {
        formData.append('otherPlantName', otherPlantName);
      }
      formData.append('bio', bio);
      formData.append('nickname', nickname);
      formData.append('birthDate', birthDate);

      if (uploadedFile) {
        formData.append('profile', uploadedFile);
      } else {
        const response = await fetch(profile);
        const blob = await response.blob();
        const file = new File([blob], "defaultProfile.png", { type: blob.type });
        formData.append('profile', file);
      }

      let response;
      if (plantId === 0) {
        response = await API.post('/user/plant', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('식물 등록 성공:', response.data);
      } else {
        response = await API.patch(`/user/plant/${plantId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('식물 수정 성공:', response.data);
      }

      navigate(`/profile/${searchId}`);
    } catch (err) {
      console.error('식물 등록/수정 실패 : ', err);
    }
  };

  const handleDelete = async () => {
    try {
      const response = await API.delete(`/user/plant/${plantId}`);
      console.log('식물 삭제 성공:', response.data);
      navigate(`/profile/${searchId}`);
    } catch (err) {
      console.error('식물 삭제 실패 : ', err);
    }
  };

  const handleFarewell = async () => {
    try {
      const response = await API.patch(`/user/plant/${plantId}/farewell`);
      console.log('식물과 이별 성공:', response.data);
      navigate(`/profile/${searchId}`);
    } catch (err) {
      console.error('식물과 이별 실패 : ', err);
    }
  };

  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };

  const openFarewellModal = () => {
    setShowFarewellModal(true);
  };

  const closeFarewellModal = () => {
    setShowFarewellModal(false);
  };

  const closeDateErrorModal = () => {
    setShowDateErrorModal(false); 
  };

  const closeFutureDateErrorModal = () => {
    setShowFutureDateErrorModal(false);
  };

  const closeNicknameErrorModal = () => {
    setShowNicknameErrorModal(false);
  };

  const closeOtherPlantErrorModal = () => {
    setShowOtherPlantErrorModal(false); 
  };

  return (
    <div className="plant-register-container mt-20 mb-20">
      <form onSubmit={handleSubmit}>
        <h1 className="plant-title mr-3">{plantId ? '식물정보 수정' : '식물정보 등록'}</h1>
        <div className="plant-detail-image-container mt-5 mb-3">
          <input
            type="file"
            ref={fileInputRef}
            style={{ display: 'none' }}
            accept="image/*"
            onChange={handleImageUpload}
          />
          <img
            src={profile}
            alt="Plant profile"
            onClick={() => fileInputRef.current.click()}
            style={{ cursor: 'pointer', width: '100px', height: '100px' }}
            className='plant-detail-image mb-2'
          />
          {profile !== defaultImage && (
            <button 
            type="button" 
            onClick={handleImageRemove}
            className="plant-detail-remove-button ml-5">
              이미지 삭제
            </button>
          )}
        </div>
        <div>
          <p>식물 종</p>
          <select
            value={plantTypeId}
            onChange={(e) => setPlantTypeId(Number(e.target.value))}
            className='plant-detail-region-select mt-2 mb-3'
          >
            {plantTypeOptions.map((plantTypeOption) => (
              <option
                key={plantTypeOption.plantTypeId}
                value={plantTypeOption.plantTypeId}
              >
                {plantTypeOption.plantName}
              </option>
            ))}
          </select>
          {plantTypeId === 1 && (
            <InputField
              type="text"
              placeholder="식물 종을 입력해주세요."
              value={otherPlantName}
              onChange={(e) => setOtherPlantName(e.target.value)}
              className="plant-register-input mt-2"
            />
          )}
        </div>

        <div>
          <p>식물 닉네임</p>
          <InputField
            type="text"
            placeholder="3~6 글자의 식물 닉네임을 입력해주세요."
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className="plant-register-input mt-2"
          />
        </div>

        <div>
          <p>식물 생일</p>
          <InputField
            type="date"
            placeholder="식물 생일을 입력해주세요."
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
            className="plant-register-input mt-2"
          />
        </div>

        <div>
          <p>식물 소개</p>
          <TextareaField
            placeholder="식물 소개를 입력해주세요."
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            className="textarea mt-2 mb-3"
          />
        </div>

        <Btn
          content={plantId ? '수정하기' : '등록하기'}
          onClick={handleSubmit}
        />
      </form>
      <div className='mt-3 mb-3'>
        {plantId !== 0 && (
          <Btn
            content="식물 삭제하기"
            onClick={openDeleteModal} 
          />
        )}
      </div>
      <div>
        {plantId !== 0 && !isFarewell && (
          <Btn
            content="식물과 이별하기"
            onClick={openFarewellModal} 
          />
        )}
      </div>

      {/* 삭제 확인 모달 */}
      <ModalConfirm
        open={showDeleteModal}
        onClose={closeDeleteModal}
        onConfirm={handleDelete} 
        title="식물 삭제하기"
        content="이 식물을 삭제하시겠습니까?"
        confirmText="삭제하기"
      />

      {/* 이별 확인 모달 */}
      <ModalConfirm
        open={showFarewellModal}
        onClose={closeFarewellModal}
        onConfirm={handleFarewell} 
        title="식물과 이별하기"
        content="이 식물과 이별하시겠습니까?"
        confirmText="이별하기"
      />

      {/* 생일 미입력 오류 모달 */}
      <ModalComplete
        open={showDateErrorModal}
        onClose={closeDateErrorModal} 
        title="생일 입력 필요"
        content="식물의 생일을 입력해주세요."
      />

      {/* 미래 날짜 오류 모달 */}
      <ModalComplete
        open={showFutureDateErrorModal}
        onClose={closeFutureDateErrorModal} 
        title="생일 재설정 필요"
        content="날짜가 올바르지 않습니다."
      />

      {/* 닉네임 오류 모달 */}
      <ModalComplete
        open={showNicknameErrorModal}
        onClose={closeNicknameErrorModal} 
        title="닉네임 입력 필요"
        content="식물의 닉네임을 입력해주세요."
      />

      {/* 기타 식물 종 오류 모달 */}
      <ModalComplete
        open={showOtherPlantErrorModal}
        onClose={closeOtherPlantErrorModal} 
        title="식물 종 입력 필요"
        content="식물 종을 입력해주세요."
      />
    </div>
  );
};

export default PlantRegister;
