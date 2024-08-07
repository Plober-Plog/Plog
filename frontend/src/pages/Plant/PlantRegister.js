import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import API from '../../apis/api';

import defaultImage from '../../assets/icon/default.png';
import InputField from '../../components/Common/InputField';
import SelectField from '../../components/Common/SelectField';
import TextareaField from '../../components/Common/TextareaField';
import Btn from '../../components/Common/Btn';

const PlantRegister = () => {
  const location = useLocation();
  // const { plantId } = location.state;
  // const plantId = 0; // 임시 plantId (작성)
  const plantId = 58; // 임시 plantId (수정)

  // 식물 정보
  const [plantTypeId, setPlantTypeId] = useState(2);
  const [otherPlantName, setOtherPlantName] = useState('');
  const [profile, setProfile] = useState(defaultImage);
  const [bio, setBio] = useState('');
  const [nickname, setNickname] = useState('');
  const [birthDate, setBirthDate] = useState('');


  // 사진 업로드
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
  useEffect(() => {
    if (plantId !== 0) {
      const fetchPlantInfo = async () => {
        try {
          const response = await API.get(`/user/plant/${plantId}/info`);
          console.log('식물 정보:', response.data);

          // 식물 정보 input에 로딩
          setPlantTypeId(response.data.plantTypeId);
          setOtherPlantName(response.data.otherPlantTypeId);
          setProfile(response.data.profile || defaultImage);
          setBio(response.data.bio);
          setNickname(response.data.nickname);
          setBirthDate(response.data.birthDate);
        } catch (err) {
          console.error('식물 정보 불러오기 실패 : ', err);
        }
      };
      fetchPlantInfo();
    }
  }, [plantId]);


  // 식물 등록
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const formData = new FormData();
      console.log(otherPlantName)

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
        const response = await fetch(defaultImage);
        const blob = await response.blob();
        const file = new File([blob], "defaultProfile.png", { type: blob.type });
        formData.append('profile', file);
      }

      let response;
      if (plantId === 0) {
        // 식물 등록
        response = await API.post('/user/plant', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('식물 정보:', formData);
      } else {
        // 식물 수정
        response = await API.patch(`/user/plant/${plantId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('식물 정보:', formData);
      }

      console.log(plantId === 0 ? '식물 등록 성공:' : '식물 수정 성공:', response.data);
    } catch (err) {
      console.error('식물 등록/수정 실패 : ', err);
    }
  };


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
          />
          {profile !== defaultImage && (
            <button type="button" onClick={handleImageRemove}>
              이미지 삭제
            </button>
          )}
        </div>
        <div>
          <p>식물 종</p>
          <SelectField
            value={plantTypeId}
            onChange={(e) => setPlantTypeId(Number(e.target.value))}
            options={plantTypeOptions}
            className="drop-box"
          />
          {plantTypeId === 1 && (
            <InputField
              type="text"
              placeholder="식물 종을 입력해주세요."
              value={otherPlantName}
              onChange={(e) => setOtherPlantName(e.target.value)}
              className="input"
            />
          )}
        </div>
        <div>
          <p>식물 닉네임</p>
          <InputField
            type="text"
            placeholder="식물 닉네임을 입력해주세요."
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className="input"
          />
        </div>
        <div>
          <p>식물 생일</p>
          <InputField
            type="date"
            placeholder="식물 생일을 입력해주세요."
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
            className="input"
          />
        </div>
        <div>
          <p>식물 소개</p>
          <TextareaField
            placeholder="식물 소개를 입력해주세요."
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            className="textarea"
          />
        </div>
        <Btn
          content={plantId ? '수정하기' : '등록하기'}
          onClick={handleSubmit}
          className="button"
        />
      </form>
    </div>
  );
};

export default PlantRegister;