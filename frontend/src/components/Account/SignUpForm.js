import React, { useState, useEffect } from 'react';
import axios from 'axios';

import Btn from '../Common/Btn';
import InputField from './InputField';
import RadioField from './RadioField';
import SelectField from './SelectField';
import ModalComplete from './ModalComplete';

const SignUpForm = () => {
  // 아이디
  const [searchId, setSearchID] = useState('');
  const [isSearchIdAvailable, setIsSearchIdAvailable] = useState(false);
  const [searchIdCheckMsg, setSearchIdCheckMsg] = useState('');

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [nickname, setNickname] = useState('');
  const [birthdate, setBirthdate] = useState('');
  const [source, setSource] = useState('');
  const [gender, setGender] = useState(0);
  const [sido, setSido] = useState('');
  const [gugun, setGugun] = useState('');

  const [agreePersonal, setAgreePersonal] = useState(false);
  const [agreeAdvertisement, setAgreeAdvertisement] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  const API_URL = 'http://localhost:3000/api/user';


  useEffect(() => {
    setIsFormValid(searchId && email && password && nickname && passwordConfirm);
  }, [searchId, email, password, nickname, passwordConfirm, agreePersonal]);


  // 아이디 중복확인
  const handleCheckSearchId = async () => {
    try {
      const response = await axios.get(`${API_URL}/${searchId}`);

      if (response.data.result) {
        setSearchIdCheckMsg('이미 사용 중인 아이디입니다.');
        setIsSearchIdAvailable(false);
      } else {
        setSearchIdCheckMsg('사용 가능한 아이디입니다.');
        setIsSearchIdAvailable(true);
      }
    } catch (error) {
      console.error('아이디 중복확인 실패: ', error);
      setSearchIdCheckMsg('아이디 중복확인 중 오류가 발생했습니다.');
      setIsSearchIdAvailable(false);
    }
  };


  // 회원가입
  const handleSignUp = async () => {
    if (password !== passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    if (!agreePersonal) {
      alert('개인정보 수집 동의를 완료해주세요');
      return;
    }

    // console.log('성별 :', gender)
    console.log('정보 받기 성공!');

    const userInfo = {
      searchId,
      email,
      password,
      nickname,
      birthdate,
      source,
      gender,
      sido,
      gugun,
    }
  };

  const closeModal = () => {
    setOpenModal(false);
  };

  // 이메일 중복확인 버튼 누르면 인증 inputfield 추가하기

  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()}>
        <div>
          {!isFormValid && <p>필수 항목을 모두 입력해 주세요.</p>}
          <InputField
            type="text"
            placeholder="아이디"
            value={searchId}
            onChange={(e) => setSearchID(e.target.value)}
            isRequired={true}
          />
          <Btn content="중복확인" onClick={handleCheckSearchId}/>
          {/* TODO 모달로 넣기 */}
          {searchIdCheckMsg}
        </div>
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
          />
          <Btn content="인증하기" />
        </div>
        <div>
          <InputField
            type={showPassword ? 'text' : 'password'}
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            isRequired={true}
          />
          <Btn
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
        </div>
        <div>
          <InputField
            type={showPassword ? 'text' : 'password'}
            placeholder="비밀번호 확인"
            value={passwordConfirm}
            onChange={(e) => setPasswordConfirm(e.target.value)}
            isRequired={true}
          />
          <Btn
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
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
          <Btn content="추천받기" />
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
        />
        <RadioField
          selectedValue={gender}
          onChange={setGender}
          options={[
            { value: 0, label: '선택하지 않음' },
            { value: 2, label: '여자' },
            { value: 1, label: '남자' },
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
        <div>
          <input
            type="checkbox"
            checked={agreePersonal}
            onChange={(e) => setAgreePersonal(e.target.checked)}
            required
          />
          <span>(필수) 개인정보 수집 동의</span>
        </div>
        <div>
          <input
            type="checkbox"
            checked={agreeAdvertisement}
            onChange={(e) => setAgreeAdvertisement(e.target.checked)}
          />
          <span>(선택) 광고 수신 동의</span>
        </div>
        <Btn
          content="회원가입"
          disabled={!isFormValid}
          onClick={handleSignUp}
        />
      </form>
      <ModalComplete title={'회원가입 완료'} content={'회원가입이 완료되었습니다'} open={openModal} onClose={closeModal} />
    </div>
  );
};

export default SignUpForm;
