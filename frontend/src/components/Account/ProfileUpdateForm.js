import React, { useState, useEffect } from 'react';
import { getUserInfo, updateUserInfo } from '../../api';
import useUserStore from '../../stores/useUserStore';

import Btn from '../Common/Btn';
import InputField from './InputField';
import SelectField from './SelectField';
import RadioField from './RadioField';
import AccountBtn from './AccountBtn';
import ModalComplete from '../../components/Account/ModalComplete';


const ProfileUpdateForm = ({ userData }) => {
  const [id, setId] = useState(userData.id);
  const [email, setEmail] = useState(userData.email);
  const [nickname, setNickname] = useState(userData.nickname || '');
  const [birthdate, setBirthdate] = useState(userData.birthdate || '');
  const [source, setSource] = useState(userData.source || '');
  const [gender, setGender] = useState(userData.gender);
  const [sido, setSido] = useState(userData.sido || '');
  const [gugun, setGugun] = useState(userData.gugun || '');
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  // 프로필 정보 불러오기
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const userInfo = await getUserInfo();
        setUser(userInfo);
        setId(userInfo.id);
        setEmail(userInfo.email);
        setNickname(userInfo.nickname);
        setBirthdate(userInfo.birthdate);
        setSource(userInfo.source);
        setGender(userInfo.gender);
        setSido(userInfo.sido);
        setGugun(userInfo.gugun);
      } catch (error) {
        console.error('사용자 정보 불러오기 실패:', error);
      }
    };

    fetchUserInfo();
  }, [setUser]);


  // 아이디, 닉네임 유효성 검사
  useEffect(() => {
    setIsFormValid(id && nickname);
  }, [id, nickname]);

  const handleProfileUpdate = () => {
    console.log('업데이트 정보 받기성공!');
    
    setOpenModal(true);
  }


  const closeModal = () => {
    setOpenModal(false);
  };

  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()}>
        <h2>회원정보 수정</h2>
        <div>
          {!isFormValid && <p>아이디를 입력해 주세요.</p>}
          <InputField
            type="text"
            placeholder="아이디"
            value={id}
            onChange={(e) => setId(e.target.value)}
            isRequired={true}
          />
          <AccountBtn 
            content='중복확인'
            onClick={console.log('중복확인 코드 적용')}
          />
        </div>
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
            disabled={true}
          />
        </div>
        <div>
          <InputField
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            isRequired={false}
          />
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
          onChange={(e) => setSource(e.target.value)}
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
