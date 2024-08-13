import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import axios from 'axios';
import useAuthStore from '../../stores/member';

import Btn from '../Common/Btn';
import InputField from '../Common/InputField';
import TextareaField from '../Common/TextareaField';
import RadioField from '../Common/RadioField';
import SelectField from '../Common/SelectField';
import ATag from '../Common/ATag';
import ModalComplete from '../Common/ModalComplete';
import './ProfileUpdateForm.css';  // CSS 파일을 임포트합니다

const ProfileUpdateForm = () => {
  const navigate = useNavigate();
  const { userData, setUserData } = useAuthStore();
  const URI = 'https://i11b308.p.ssafy.io/api';
  
  const [searchId, setSearchId] = useState(userData?.searchId || '');
  const [nickname, setNickname] = useState(userData?.nickname || '');
  const [email] = useState(userData?.email || '');
  const [birthdate, setBirthdate] = useState(userData?.birthdate || '');
  const [gender, setGender] = useState(userData?.gender || '');
  const [source] = useState(userData?.source || '');
  const [sidoCode, setSidoCode] = useState(userData?.sidoCode || 0);
  const [gugunCode, setGugunCode] = useState(userData?.gugunCode || 0);
  const [sidoOptions, setSidoOptions] = useState([]);
  const [gugunOptions, setGugunOptions] = useState([]);
  const [profileInfo, setProfileInfo] = useState(userData?.profileInfo || '');
  const [isAd, setIsAd] = useState(userData?.isAd || false);
  
  const fileInputRef = useRef(null);
  const [profile, setProfile] = useState(userData?.profile || ''); 
  const [uploadedFile, setUploadedFile] = useState(null);

  const [searchIdCheckMsg, setSearchIdCheckMsg] = useState('');
  const [isSearchIdAvailable, setIsSearchIdAvailable] = useState(true);
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  // 이미지 업로드 핸들러
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

  // 시도 및 구군 옵션 가져오기
  useEffect(() => {
    const fetchLocationOptions = async () => {
      try {
        const [sidoRes, gugunRes] = await Promise.all([
          axios.get(`${URI}/area/sido`),
          axios.get(`${URI}/area/gugun/${sidoCode}`)
        ]);
        setSidoOptions(sidoRes.data);
        setGugunOptions(gugunRes.data);
      } catch (error) {
        console.error('지역 옵션 불러오기 실패:', error);
      }
    };

    fetchLocationOptions();
  }, [sidoCode]);

  useEffect(() => {
    if (sidoCode) {
      const filtered = gugunOptions.filter(option => option.sidoCode === Number(sidoCode));
      setGugunCode(filtered.length > 0 ? filtered[0].gugunCode : 0);
    }
  }, [sidoCode, gugunOptions]);

  // 이미지 삭제 핸들러
  const handleImageRemove = () => {
    setProfile(userData.profile || ''); 
    setUploadedFile(null);
    if (fileInputRef.current) fileInputRef.current.value = null;
  };

  // 폼 유효성 검사
  useEffect(() => {
    setIsFormValid(
      searchId.length >= 5 && nickname.length >= 3 && nickname.length <= 10 && isSearchIdAvailable
    );
  }, [searchId, nickname, isSearchIdAvailable]);

  // 아이디 중복 확인 핸들러
  const handleCheckSearchId = async () => {
    if (!/^[a-z0-9]{5,15}$/.test(searchId)) {
      setSearchIdCheckMsg('아이디 형식이 올바르지 않습니다.');
      setIsSearchIdAvailable(false);
      return;
    }

    try {
      const response = await API.get(`/user/${searchId}`);
      if (response.status === 200) {
        setSearchIdCheckMsg('사용 가능한 아이디입니다.');
        setIsSearchIdAvailable(true);
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setSearchIdCheckMsg('이미 사용 중인 아이디입니다.');
      } else {
        setSearchIdCheckMsg('아이디 중복 확인 중 오류가 발생했습니다.');
      }
      setIsSearchIdAvailable(false);
    }
  };

  // 회원 정보 수정 요청 핸들러
  const handleProfileUpdate = async () => {
    const updatedUserData = {
      nickname,
      searchId,
      gender,
      birthData: birthdate,
      source,
      sidoCode,
      gugunCode,
      profileInfo,
      Ad: isAd
    };
    const formData = new FormData();
    formData.append('userUpdateRequestDto', new Blob([JSON.stringify(updatedUserData)], { type: 'application/json' }));
    const response = await fetch(profile);
    const blob = await response.blob();
    const file = new File([blob], "updateImage.png", { type: blob.type });
    formData.append('profile', file);

    try {
      console.log('updatedUserData:', formData);
      const response = await API.patch('/user', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      // 유저 데이터 갱신
      setUserData({
       ...userData,
       ...updatedUserData,
        profile: uploadedFile? URL.createObjectURL(uploadedFile) : userData.profile
      });

      console.log('회원 정보 수정 성공:', response.data);
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
      <form onSubmit={(e) => e.preventDefault()}>
        <div className="profile-image-container mb-3">
          <input
            type="file"
            ref={fileInputRef}
            style={{ display: 'none' }}
            accept="image/*"
            onChange={handleImageUpload}
          />
          <img
            src={profile}
            alt="Profile"
            onClick={() => fileInputRef.current.click()}
            className="profile-image mb-2"
          />
          {profile !== userData.profile && (
            <button type="button" onClick={handleImageRemove} className='profile-remove-button mt-2' >
              이미지 삭제
            </button>
          )}
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
            disabled={true}
            className="account-disable-input mt-5"
          />
        </div>
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            isRequired={true}
            disabled={true}
            className="account-disable-input"
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
            className="account-input"
          />
          {nicknameCheckMsg && <p>{nicknameCheckMsg}</p>}
        </div>
        <div>
          <TextareaField
            placeholder="자기소개를 입력해주세요."
            value={profileInfo}
            onChange={(e) => setProfileInfo(e.target.value)}
            className="mt-1 mb-1"
          />
        </div>
        <div>
          <InputField
            type="date"
            placeholder="생일"
            value={birthdate}
            onChange={(e) => setBirthdate(e.target.value)}
            isRequired={false}
            className="account-input"
          />
        </div>
        {/* <SelectField
          value={source}
          options={['가입경로', '지인추천', '인터넷 검색']}
          isRequired={false}
          disabled={true}
        /> */}
        <div className="profile-inline-group">
          <label className="profile-inline-label">성별</label>
          <RadioField
            selectedValue={gender}
            onChange={setGender}
            options={[
              { value: 1, label: '선택하지 않음' },
              { value: 2, label: '남자' },
              { value: 3, label: '여자' },
            ]}
            isRequired={false}
            className="profile-radio-field"
          />
        </div>
        <div className="profile-region-group">
          <div className="profile-region-select-container">
            <label>지역</label>
            <select
              value={sidoCode}
              onChange={(e) => setSidoCode(e.target.value)}
              required={false}
              className="profile-region-select"
            >
              <option value="0">시/도 선택</option>
              {sidoOptions.map(sidoOption => (
                <option key={sidoOption.sidoCode} value={sidoOption.sidoCode}>{sidoOption.sidoName}</option>
              ))}
            </select>
            <select
              value={gugunCode}
              onChange={(e) => setGugunCode(e.target.value)}
              required={false}
              className="profile-region-select"
            >
              <option value="0">구/군 선택</option>
              {gugunOptions
                .filter(option => option.sidoCode === Number(sidoCode))
                .map(filteredGugunOption => (
                  <option key={filteredGugunOption.gugunCode} value={filteredGugunOption.gugunCode}>
                    {filteredGugunOption.gugunName}
                  </option>
                ))}
            </select>
          </div>
        </div>

        <div className='mb-5'>
          <Btn
            content="수정하기"
            disabled={!isFormValid}
            onClick={handleProfileUpdate}
          />
        </div>
      </form>
      <ModalComplete title="회원정보 수정 완료" content="회원정보 수정이 완료되었습니다" open={openModal} onClose={closeModal} />
    </div>
  );
};

export default ProfileUpdateForm;
