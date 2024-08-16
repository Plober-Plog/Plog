import React, { useState, useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import API from '../../apis/api';
<<<<<<< HEAD

import defaultImage from '../../assets/icon/default.png';
import InputField from '../../components/Common/InputField';
import SelectField from '../../components/Common/SelectField';
import TextareaField from '../../components/Common/TextareaField';
import Btn from '../../components/Common/Btn';
=======
import useAuthStore from '../../stores/member';

import defaultImage from '../../assets/icon/default.png';
import InputField from '../../components/Common/InputField';
import TextareaField from '../../components/Common/TextareaField';
import Btn from '../../components/Common/Btn';
import ModalConfirm from '../../components/Common/ModalConfirm'; 
import ModalComplete from '../../components/Common/ModalComplete'; 
import './PlantRegister.css'; 
>>>>>>> master

const PlantRegister = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId } = location.state;
<<<<<<< HEAD

  // 식물 정보
=======
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

>>>>>>> master
  const [plantTypeId, setPlantTypeId] = useState(2);
  const [otherPlantName, setOtherPlantName] = useState('');
  const [profile, setProfile] = useState(defaultImage);
  const [bio, setBio] = useState('');
  const [nickname, setNickname] = useState('');
  const [birthDate, setBirthDate] = useState('');
<<<<<<< HEAD


  // 사진 업로드
=======
  const [isFarewell, setIsFarewell] = useState(false);

>>>>>>> master
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
<<<<<<< HEAD
      setUploadedFile(file); // 업로드된 파일 저장
    }
  };


  // 사진 삭제
  const handleImageRemove = () => {
    setProfile(defaultImage);
    setUploadedFile(null);
    fileInputRef.current.value = null; // input 필드를 초기화하여 동일 파일 업로드 가능하게 함
  };


  // 수정일 경우 식물 정보 가져오기
=======
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
      } catch (error) {
        console.error('식물 종류 불러오기 실패:', error);
      }
    };
    fetchPlantTypeOptions();
  }, []);

>>>>>>> master
  useEffect(() => {
    if (plantId !== 0) {
      const fetchPlantInfo = async () => {
        try {
          const response = await API.get(`/user/plant/${plantId}/info`);
<<<<<<< HEAD
          console.log('식물 정보:', response.data);

          // 식물 정보 input에 로딩
          setPlantTypeId(response.data.plantTypeId);
          setOtherPlantName(response.data.otherPlantTypeId);
=======

          setPlantTypeId(response.data.plantTypeId);
          setOtherPlantName(response.data.otherPlantTypeName);
>>>>>>> master
          setProfile(response.data.profile || defaultImage);
          setBio(response.data.bio);
          setNickname(response.data.nickname);
          setBirthDate(response.data.birthDate);
<<<<<<< HEAD
=======
          setIsFarewell(response.data.deadDate);
>>>>>>> master
        } catch (err) {
          console.error('식물 정보 불러오기 실패 : ', err);
        }
      };
      fetchPlantInfo();
    }
  }, [plantId]);

<<<<<<< HEAD

  // 식물 등록
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const formData = new FormData();
      console.log(otherPlantName)
=======
  const handleSubmit = async (e) => {
    e.preventDefault();

    const today = new Date().toISOString().split("T")[0];

    if (!nickname || nickname.length > 6) {
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

    if (plantTypeId === 1 && !otherPlantName || otherPlantName.length > 15) {
      setShowOtherPlantErrorModal(true);
      return;
    }

    try {
      const formData = new FormData();
>>>>>>> master

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
<<<<<<< HEAD
        const response = await fetch(defaultImage);
=======
        const response = await fetch(profile);
>>>>>>> master
        const blob = await response.blob();
        const file = new File([blob], "defaultProfile.png", { type: blob.type });
        formData.append('profile', file);
      }

      let response;
      if (plantId === 0) {
<<<<<<< HEAD
        // 식물 등록
=======
>>>>>>> master
        response = await API.post('/user/plant', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
<<<<<<< HEAD
        console.log('식물 정보:', formData);
      } else {
        // 식물 수정
=======
      } else {
>>>>>>> master
        response = await API.patch(`/user/plant/${plantId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
<<<<<<< HEAD
        console.log('식물 정보:', formData);
      }

      console.log(plantId === 0 ? '식물 등록 성공:' : '식물 수정 성공:', response.data);
      // navigate(`/plant/${plantId}`);
      // TODO 작성시 식물 plantId 넘겨주기 변경
=======
      }

      navigate(`/profile/${searchId}`);
>>>>>>> master
    } catch (err) {
      console.error('식물 등록/수정 실패 : ', err);
    }
  };

<<<<<<< HEAD

  // 임시 식물 종 옵션
  const plantTypeOptions = [
    { value: 1, label: '기타' },
    { value: 2, label: '복숭아' },
    { value: 3, label: '참외' },
    { value: 4, label: '수박' },
    { value: 5, label: '바나나' },
    { value: 6, label: '붕어빵' },
    { value: 7, label: '참치' },
    { value: 8, label: '구아바' },
  ];


  return (
    <div>
      <h1>{plantId ? '식물정보 수정' : '식물정보 등록'}</h1>
      <form onSubmit={handleSubmit}>
        <div>
=======
  const handleDelete = async () => {
    try {
      const response = await API.delete(`/user/plant/${plantId}`);
      navigate(`/profile/${searchId}`);
    } catch (err) {
      console.error('식물 삭제 실패 : ', err);
    }
  };

  const handleFarewell = async () => {
    try {
      const response = await API.patch(`/user/plant/${plantId}/farewell`);
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
>>>>>>> master
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
<<<<<<< HEAD
          />
          {profile !== defaultImage && (
            <button type="button" onClick={handleImageRemove}>
=======
            className='plant-detail-image mb-2'
          />
          {profile !== defaultImage && (
            <button 
            type="button" 
            onClick={handleImageRemove}
            className="plant-detail-remove-button ml-5">
>>>>>>> master
              이미지 삭제
            </button>
          )}
        </div>
        <div>
          <p>식물 종</p>
<<<<<<< HEAD
          <SelectField
            value={plantTypeId}
            onChange={(e) => setPlantTypeId(Number(e.target.value))}
            options={plantTypeOptions}
            className="drop-box"
          />
=======
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
>>>>>>> master
          {plantTypeId === 1 && (
            <InputField
              type="text"
              placeholder="식물 종을 입력해주세요."
              value={otherPlantName}
              onChange={(e) => setOtherPlantName(e.target.value)}
<<<<<<< HEAD
              className="input"
            />
          )}
        </div>
=======
              className="plant-register-input mt-2"
            />
          )}
        </div>

>>>>>>> master
        <div>
          <p>식물 닉네임</p>
          <InputField
            type="text"
<<<<<<< HEAD
            placeholder="식물 닉네임을 입력해주세요."
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className="input"
          />
        </div>
=======
            placeholder="3~6 글자의 식물 닉네임을 입력해주세요."
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className="plant-register-input mt-2"
          />
        </div>

>>>>>>> master
        <div>
          <p>식물 생일</p>
          <InputField
            type="date"
            placeholder="식물 생일을 입력해주세요."
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
<<<<<<< HEAD
            className="input"
          />
        </div>
=======
            className="plant-register-input mt-2"
          />
        </div>

>>>>>>> master
        <div>
          <p>식물 소개</p>
          <TextareaField
            placeholder="식물 소개를 입력해주세요."
            value={bio}
            onChange={(e) => setBio(e.target.value)}
<<<<<<< HEAD
            className="textarea"
          />
        </div>
        <Btn
          content={plantId ? '수정하기' : '등록하기'}
          onClick={handleSubmit}
          className="button"
        />
      </form>
=======
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
        content="식물의 닉네임을 6글자 이하로 입력해주세요."
      />

      {/* 기타 식물 종 오류 모달 */}
      <ModalComplete
        open={showOtherPlantErrorModal}
        onClose={closeOtherPlantErrorModal} 
        title="식물 종 입력 필요"
        content="식물 종을 15자 이하로 입력해주세요."
      />
>>>>>>> master
    </div>
  );
};

<<<<<<< HEAD
export default PlantRegister;
=======
export default PlantRegister;
>>>>>>> master
