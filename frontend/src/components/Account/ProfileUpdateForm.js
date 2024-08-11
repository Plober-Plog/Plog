import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from "react-router-dom";
import API from '../../apis/api';
import useAuthStore from '../../stores/member';

import Btn from '../Common/Btn';
import InputField from '../Common/InputField';
import TextareaField from '../../components/Common/TextareaField';
import RadioField from '../Common/RadioField';
import SelectField from '../Common/SelectField';
import ATag from '../Common/ATag';
import ModalComplete from '../Common/ModalComplete';
import defaultImage from '../../assets/icon/default.png';

const ProfileUpdateForm = () => {
  const navigate = useNavigate();
  const { userData, setUserData } = useAuthStore();
  console.log(userData)
  console.log(userData.searchId)
  
  // 상태 초기화
  const [searchId, setSearchId] = useState(userData?.searchId || '');
  const [nickname, setNickname] = useState(userData?.nickname || '');
  const [email, setEmail] = useState(userData?.email || '');
  const [birthdate, setBirthdate] = useState(userData?.birthdate || '');
  const [gender, setGender] = useState(userData?.gender || '');
  const [source, setSource] = useState(userData?.source || '');
  const [sido, setSido] = useState(userData?.sido || '');
  const [gugun, setGugun] = useState(userData?.gugun || '');
  const [profileInfo, setProfileInfo] = useState(userData?.profileInfo || '');
  const [isAd, setIsAd] = useState(userData?.isAd || false);
  // 사진 업로드
  const fileInputRef = useRef(null);
  const [profile, setProfile] = useState(userData?.profile);
  const [uploadedFile, setUploadedFile] = useState(null);
  // 유효성 검사
  const [searchIdCheckMsg, setSearchIdCheckMsg] = useState('');
  const [isSearchIdAvailable, setIsSearchIdAvailable] = useState(true);
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  // 사진 업로드
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

  // 폼 유효성 검사
  useEffect(() => {
    setIsFormValid(
      searchId &&
      nickname &&
      isSearchIdAvailable
    );
  }, [searchId, nickname, isSearchIdAvailable]);

  // 아이디 중복 확인
  const handleCheckSearchId = async () => {
    if (!/^[a-z0-9]{5,15}$/.test(searchId)) {
      console.log('아이디 형식이 올바르지 않습니다.');
      return;
    }

    try {
      const response = await API.get(`/user/${searchId}`);
      if (response.status === 200) {
        setSearchIdCheckMsg('사용 가능한 아이디입니다.');
        setIsSearchIdAvailable(true);
        console.log('아이디 중복확인 성공!');
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setSearchIdCheckMsg('이미 사용 중인 아이디입니다.');
        setIsSearchIdAvailable(false);
        console.error('아이디 중복 확인: 이미 사용 중인 아이디입니다.');
      } else {
        setSearchIdCheckMsg('아이디 중복확인 중 오류가 발생했습니다.');
        setIsSearchIdAvailable(false);
        console.error('아이디 중복확인 실패: ', error);
      }
    }
  };

  const handleProfileUpdate = async () => {
    const updatedUserData = {
      nickname,
      searchId,
      email,
      profileInfo,
      gender,
      birthdate,
      source,
      sido,
      gugun,
      isAd
    };

    try {
      const response = await API.patch('/user', updatedUserData);
      console.log('회원 정보 수정 성공:', response);
      setUserData(updatedUserData);
      setOpenModal(true);
    } catch (error) {
      console.error('회원 정보 수정 실패:', error);
    }
  };

  const closeModal = () => {
    setOpenModal(false);
    navigate('/setting');
  };

  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()} className="form">
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
          {/* TODO 이미지 수정 컴포넌트 추가 */}
        </div>
        <div>
          {!isFormValid && <p>아이디를 입력해 주세요.</p>}
          <InputField
            type="text"
            placeholder="아이디"
            value={searchId}
            onChange={(e) => {
              setSearchId(e.target.value);
              setIsSearchIdAvailable(false);
            }}
            isRequired={true}
            className="input"
          />
          <ATag 
            content='중복확인' onClick={handleCheckSearchId}
          />
          {searchIdCheckMsg && <p>{searchIdCheckMsg}</p>}
        </div>
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            isRequired={true}
            disabled={true}
            className="input"
          />
        </div>
        <div>
          <InputField
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => {
              const value = e.target.value;
              setNickname(value);
              if (value.length < 3 || value.length > 10) {
                setNicknameCheckMsg('닉네임은 3~10 글자여야 합니다.');
              } else {
                setNicknameCheckMsg('');
              }
            }}
            isRequired={false}
            className="input"
          />
          {nicknameCheckMsg && <p>{nicknameCheckMsg}</p>}
        </div>
        <div>
          <TextareaField
              placeholder="자기소개를 입력해주세요."
              value={profileInfo}
              onChange={(e) => setProfileInfo(e.target.value)}
              className="textarea"
            />
          </div>
        <div>
          <InputField
            type="date"
            placeholder="생일"
            value={birthdate}
            onChange={(e) => setBirthdate(e.target.value)}
            isRequired={false}
            className="input"
          />
        </div>
        <SelectField
          value={source}
          options={['가입경로', '지인추천', '인터넷 검색']}
          isRequired={false}
          disabled={true}
        />
        <RadioField
          selectedValue={gender}
          onChange={setGender}
          options={[
            { value: 1, label: '선택하지 않음' },
            { value: 2, label: '남자' },
            { value: 3, label: '여자' },
          ]}
          isRequired={false}
        />
        <div>
          <label>지역</label>
          <SelectField
            value={sido}
            onChange={(e) => setSido(e.target.value)}
            options={['시/도']}
            isRequired={false}
          />
          <SelectField
            value={gugun}
            onChange={(e) => setGugun(e.target.value)}
            options={['구/군']}
            isRequired={false}
          />
        </div>
        <Btn
          content="수정하기"
          disabled={!isFormValid}
          onClick={handleProfileUpdate}
          className="button"
        />
      </form>
      <ModalComplete title={'회원정보 수정 완료'} content={'회원정보 수정이 완료되었습니다'} open={openModal} onClose={closeModal} />
    </div>
  );
};

export default ProfileUpdateForm;
