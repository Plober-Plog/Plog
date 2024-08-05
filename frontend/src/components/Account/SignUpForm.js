import React, { useState, useEffect } from 'react';
import axios from 'axios';
import sha256 from 'js-sha256';

import Btn from '../Common/Btn';
import ATag from '../Common/ATag';
import InputField from '../Common/InputField';
import RadioField from '../Common/RadioField';
import SelectField from '../Common/SelectField';
import ModalComplete from '../Common/ModalComplete';
import defaultProfile from '../../assets/image/defaultprofile.png';

const SignUpForm = () => {
  // 회원 정보
  const [searchId, setSearchID] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [nickname, setNickname] = useState('');
  const [birthdate, setBirthdate] = useState('');
  const [source, setSource] = useState('');
  const [gender, setGender] = useState(1);
  const [sido, setSido] = useState('');
  const [gugun, setGugun] = useState('');
  // 회원 동의
  const [agreePersonal, setAgreePersonal] = useState(false);
  const [agreeAdvertisement, setAgreeAdvertisement] = useState(false);
  // 회원가입 조건 만족
  const [isFormValid, setIsFormValid] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [isSearchIdAvailable, setIsSearchIdAvailable] = useState(false);
  const [searchIdCheckMsg, setSearchIdCheckMsg] = useState('');
  const [emailCheckMsg, setEmailCheckMsg] = useState('');
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');
  const [passwordCheckMsg, setPasswordCheckMsg] = useState('');
  const [passwordConfirmCheckMsg, setPasswordConfirmCheckMsg] = useState('');
  // 이메일 인증
  const [emailVerificationMsg, setEmailVerificationMsg] = useState('');
  const [emailVerificationInput, setEmailVerificationInput] = useState('');
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [isEmailVerificationSent, setIsEmailVerificationSent] = useState(false);
  const [timer, setTimer] = useState(0);

  const URI = 'https://i11b308.p.ssafy.io/api';

  // TODO 이메일 인증 완료시 이메일 input 비활성화

  // 유효성 검사
  useEffect(() => {
    setIsFormValid(
      /^[a-z0-9]{5,15}$/.test(searchId) &&
      isSearchIdAvailable &&
      email &&
      password &&
      passwordConfirm &&
      password === passwordConfirm &&
      /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/.test(password) &&
      nickname.length >= 3 &&
      nickname.length <= 10 &&
      agreePersonal &&
      isEmailVerified
    );
  }, [searchId, isSearchIdAvailable, email, password, passwordConfirm, nickname, agreePersonal, isEmailVerified]);


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


  // 이메일 인증
  const handleCheckEmail = async () => {
    // 유효성 검사
    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
      console.log('이메일 형식이 올바르지 않습니다.');
      return;
    }

    // 이메일 중복확인
    try {
      const response = await axios.post(`${URI}/user/email`, { email });
      // 중복 X
      if (response.status === 200) {
        console.log('이메일 중복 확인 성공! (중복 X)');
        setIsEmailVerified(true);
      }
    } catch (error) {
      // 중복 O
      if (error.response && error.response.status === 409) {
        setEmailCheckMsg('이미 사용중인 이메일 입니다.')
        console.error('이메일 중복: ', error);
        return;
        // 실패
      } else {
        console.error('이메일 인증 확인 실패: ', error);
        return;
      }
    }

    // 이메일 인증번호 전송
    try {
      const response = await axios.post(`${URI}/user/email/send`, { email });
      console.log('이메일 인증번호 전송 성공!');
      console.log(response)
      setIsEmailVerificationSent(true);
      setTimer(300)
    } catch(error) {
      console.log('이메일 인증번호 전송 실패 : ', error);
    }
  }


  // 타이머 변환
  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
  };

  useEffect(() => {
    let interval;
    if (isEmailVerificationSent && timer > 0) {
      interval = setInterval(() => {
        setTimer((prevTimer) => prevTimer - 1);
      }, 1000);
    } else if (timer === 0) {
      clearInterval(interval);
      setIsEmailVerificationSent(false);
    }
    return () => clearInterval(interval);
  }, [isEmailVerificationSent, timer]);


  // 이메일 인증 코드 확인
  const handleVerifyEmailCode = async () => {
    try {
      const response = await axios.post(`${URI}/user/email/check`, { email, verifyCode: emailVerificationInput });
      console.log(response)
      console.log('이메일 인증 성공!');
      setIsEmailVerified(true);
    } catch (error) {
      console.error('이메일 인증 확인 실패: ', error);
      setEmailVerificationMsg('인증번호가 일치하지 않습니다.');
    }
  };


  // 회원가입 버튼 클릭
  const handleSignUp = async () => {
    
    const userInfo = {
      // TODO default 이미지 추가하기
      email,
      searchId,
      password: sha256(password),
      nickname,
      profile: defaultProfile, // URL.createObjectURL(defaultProfile),
      gender,
      birthDate: birthdate,
      source,
      sidoCode: sido,
      gugunCode: gugun,
      profileInfo: "",
      isAd: agreeAdvertisement
    };

    console.log('정보 받기 성공!');
    console.log(userInfo);

    // 회원가입 요청
    try {
      await axios.post(`${URI}/user`, userInfo);
      setOpenModal(true);
    } catch (error) {
      console.error('회원가입 실패: ', error);
    }
  };

  const closeModal = () => {
    setOpenModal(false);
  };


  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()} className="form">
        <div>
          {!isFormValid && <p>필수 항목을 모두 입력해 주세요.</p>}
          <InputField
            type="text"
            placeholder="아이디"
            value={searchId}
            onChange={(e) => {
              const value = e.target.value
              setSearchID(value)
              if (!/^[a-z0-9]{5,15}$/.test(value)) {
                setSearchIdCheckMsg('아이디는 5글자 이상 15이하여야 합니다.');
              } else {
                setSearchIdCheckMsg('');
              }
            }}
            isRequired={true}
            className="input"
          />
          <ATag content="중복확인" onClick={handleCheckSearchId}/>
          {searchIdCheckMsg && <p>{searchIdCheckMsg}</p>}
        </div>
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => {
              const value = e.target.value
              setEmail(value)
              if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
                setEmailCheckMsg('올바른 이메일 형식이 아닙니다.');
              } else {
                setEmailCheckMsg('');
              }
            }}
            isRequired={true}
            disabled={isEmailVerified} // 이메일 인증 완료 후 비활성화
            className="input"
          />
          <ATag
            content="인증하기" onClick={handleCheckEmail}
          />
          {emailCheckMsg && <p>{emailCheckMsg}</p>}
        </div>
        {isEmailVerificationSent && (
          <div>
            <InputField
              type="text"
              placeholder="이메일 인증 코드"
              value={emailVerificationInput}
              onChange={(e) => setEmailVerificationInput(e.target.value)}
              isRequired={true}
              disabled={isEmailVerified} // 이메일 인증 완료 후 비활성화
              className="input"
            />
            <p>{formatTime(timer)}</p>
            <ATag content="인증 확인" onClick={handleVerifyEmailCode} />
            {emailVerificationMsg && <p>{emailVerificationMsg}</p>}
          </div>
        )}
        <div>
          <InputField
            type={showPassword ? 'text' : 'password'}
            placeholder="비밀번호"
            value={password}
            onChange={(e) => {
              const value = e.target.value
              setPassword(value)
              if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/.test(value)) {
                setPasswordCheckMsg('비밀번호는 영어와 숫자를 포함해서 8글자 이상 12이하여야 합니다.');
              } else {
                setPasswordCheckMsg('');
              }
            }}
            isRequired={true}
            className="input"
          />
          <ATag
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
          {passwordCheckMsg && <p>{passwordCheckMsg}</p>}
        </div>
        <div>
          <InputField
            type={showPassword ? 'text' : 'password'}
            placeholder="비밀번호 확인"
            value={passwordConfirm}
            onChange={(e) => {
              const value = e.target.value
              setPasswordConfirm(value)
              if (password !== value) {
                setPasswordConfirmCheckMsg('비밀번호가 일치하지 않습니다.');
              } else {
                setPasswordConfirmCheckMsg('');
              }
            }}
            isRequired={true}
            className="input"
          />
          <ATag
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
          {passwordConfirmCheckMsg && <p>{passwordConfirmCheckMsg}</p>}
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
            className="input"
          />
          {nicknameCheckMsg && <p>{nicknameCheckMsg}</p>}
          <ATag content="추천받기" />
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
          onChange={(e) => setSource(e.target.value)}
          options={['가입경로', '지인추천', '인터넷 검색']}
          isRequired={false}
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
          className="button"
        />
      </form>
      <ModalComplete title={'회원가입 완료'} content={'회원가입이 완료되었습니다'} open={openModal} onClose={closeModal} />
    </div>
  );
};

export default SignUpForm;
