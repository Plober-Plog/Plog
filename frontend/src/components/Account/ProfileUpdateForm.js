import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import API from '../../apis/api';

import Btn from '../Common/Btn';
import InputField from './InputField';
import SelectField from './SelectField';
import RadioField from './RadioField';
import ATag from './ATag';
import ModalComplete from '../../components/Account/ModalComplete';


const ProfileUpdateForm = ({ userData }) => {
  
  const navigate = useNavigate();
  const URI = 'https://i11b308.p.ssafy.io/api'
  // 회원 정보 변경 불가능
  const email = userData.email;
  const source = userData.source || '';
  // 회원 정보 변경 가능
  const [searchId, setsearchId] = useState(userData.searchId);
  const [profile, setProfile] = useState(userData.profile);
  const [nickname, setNickname] = useState(userData.nickname || '');
  const [birthdate, setBirthdate] = useState(userData.birthdate || '');
  const [gender, setGender] = useState(userData.gender);
  const [sido, setSido] = useState(userData.sido || '');
  const [gugun, setGugun] = useState(userData.gugun || '');
  const [profileInfo, setProfileInfo] = useState(userData.profile_info || '');
  const [isAd, setIsAd] = useState(userData.isAd);
  // 유효성 검사
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [isSearchIdAvailable, setIsSearchIdAvailable] = useState(false);
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');
  const [searchIdCheckMsg, setSearchIdCheckMsg] = useState('');



  // 아이디, 닉네임 유효성 검사
  useEffect(() => {
    setIsFormValid(
      searchId &&
      nickname &&
      isSearchIdAvailable
    );
  }, [searchId, nickname, isSearchIdAvailable]);


  // 아이디 중복확인
  const handleCheckSearchId = async () => {
    // 유효성 검사
    if (!/^[a-z0-9]{5,15}$/.test(searchId)) {
      console.log('아이디 형식이 올바르지 않습니다.');
      return;
    }

    try {
      const response = await axios.get(`${URI}/user/${searchId}`);
      // 중복 X
      if (response.status === 200) {
        setSearchIdCheckMsg('사용 가능한 아이디입니다.');
        setIsSearchIdAvailable(true);
        console.log('아이디 중복확인 성공!');
      }
    } catch (error) {
      // 중복 O
      if (error.response && error.response.status === 409) {
        setSearchIdCheckMsg('이미 사용 중인 아이디입니다.');
        setIsSearchIdAvailable(false);
        console.error('아이디 중복 확인: 이미 사용 중인 아이디입니다.');
      } else {
        // 실패
        setSearchIdCheckMsg('아이디 중복확인 중 오류가 발생했습니다.');
        setIsSearchIdAvailable(false);
        console.error('아이디 중복확인 실패: ', error);
      }
    }
  };


  const handleProfileUpdate = async () => {

    const userData = {
      nickname,
      searchId,
      profile,
      gender,
      birthdate,
      source,
      sido,
      gugun,
      profileInfo,
      isAd
    }

    // 회원정보 수정 요청
    try {
      const response = await axios.patch('api/user', userData);
      console.log('회원 정보 수정 성공:', response);
      setOpenModal(true);
    } catch (error) {
      console.error('회원 정보 수정 실패:', error);
    }
  }

  const closeModal = () => {
    setOpenModal(false);
    navigate('/setting');
  };

  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()}>
        <div>
         {/* TODO 이미지 수정 컴포넌트 추가 */}
        </div>
        <div>
          {!isFormValid && <p>아이디를 입력해 주세요.</p>}
          <InputField
            type="text"
            placeholder="아이디"
            value={searchId}
            onChange={(e) => setsearchId(e.target.value)}
            isRequired={true}
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
          />
        </div>
        <div>
          <InputField
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => {
              const value = e.target.value
              setNickname(value)
              if (value.length < 3 || value.length > 10) {
                setNicknameCheckMsg('닉네임은 3~10 글자여야 합니다.');
              } else{
                setNicknameCheckMsg('');
              }
            }}
            isRequired={false}
          />
          {nicknameCheckMsg && <p>{nicknameCheckMsg}</p>}
        </div>
        <div>
          <InputField
            type="date"
            placeholder="생일"
            value={birthdate}
            onChange={(e) => setBirthdate(e.target.value)}
            isRequired={false}
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
        />
      </form>
      <ModalComplete title={'회원정보 수정 완료'} content={'회원정보 수정이 완료되었습니다'} open={openModal} onClose={closeModal}/>
    </div>
  );
};

export default ProfileUpdateForm;
