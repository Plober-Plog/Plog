import React, { useState, useEffect } from 'react';
<<<<<<< HEAD
=======
import { useNavigate } from 'react-router-dom';
>>>>>>> master
import axios from 'axios';
import sha256 from 'js-sha256';

import Btn from '../Common/Btn';
<<<<<<< HEAD
import ATag from '../Common/ATag';
=======
>>>>>>> master
import InputField from '../Common/InputField';
import RadioField from '../Common/RadioField';
import SelectField from '../Common/SelectField';
import ModalComplete from '../Common/ModalComplete';
<<<<<<< HEAD
import defaultProfile from '../../assets/image/defaultprofile.png';

const SignUpForm = () => {
=======
import defaultProfile from './defaultprofile.png';
import './ProfileUpdateForm.css';

const SignUpForm = () => {
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const navigate = useNavigate();
>>>>>>> master
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
<<<<<<< HEAD
  const [sido, setSido] = useState('');
  const [gugun, setGugun] = useState('');
=======
  const [sidoCode, setSidoCode] = useState(0);
  const [gugunCode, setGugunCode] = useState(0);
  const [sidoOptions, setSidoOptions] = useState([]);
  const [gugunOptions, setGugunOptions] = useState([]);
  const [filteredGugunOptions, setFilteredGugunOptions] = useState([]);
>>>>>>> master
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
<<<<<<< HEAD
=======
  const [dateError, setDateError] = useState(false);
>>>>>>> master
  // 이메일 인증
  const [emailVerificationMsg, setEmailVerificationMsg] = useState('');
  const [emailVerificationInput, setEmailVerificationInput] = useState('');
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [isEmailVerificationSent, setIsEmailVerificationSent] = useState(false);
<<<<<<< HEAD
  const [timer, setTimer] = useState(0);

  const URI = 'https://i11b308.p.ssafy.io/api';

=======
  const [isSendingEmail, setIsSendingEmail] = useState(false); // 이메일 전송 중 상태
  const [timer, setTimer] = useState(0);


  // 시도 옵션 가져오기
  useEffect(() => {
    const getSidoOptions = async () => {
      try {
        const response = await axios.get(`${API_BASE_URL}/area/sido`)
        setSidoOptions(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    getSidoOptions();

  }, []);

  // 구군 옵션 가져오기
  useEffect(() => {
    const getGugunOptions = async (sidoCode) => {
      try {
        const response = await axios.get(`${API_BASE_URL}/area/gugun/${sidoCode}`,
          { params: { sidoCode } }
        )
        setGugunOptions(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    if (sidoCode) {
      getGugunOptions(sidoCode);
    }
  }, [sidoCode]);

  // sidoCode 변경 시 해당 구군 코드 필터링
  useEffect(() => {
    if (gugunOptions.length > 0) {
      const filtered = gugunOptions.filter(gugunOption => gugunOption.sidoCode === Number(sidoCode));
      setFilteredGugunOptions(filtered);
      setGugunCode(filtered.length > 0 ? filtered[0].gugunCode : 0);
    } else {
      setFilteredGugunOptions([]);
      setGugunCode(0);
    }
  }, [gugunOptions, sidoCode]);
>>>>>>> master

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
<<<<<<< HEAD
      nickname.length <= 10 &&
=======
      nickname.length <= 6 &&
>>>>>>> master
      agreePersonal &&
      isEmailVerified
    );
  }, [searchId, isSearchIdAvailable, email, password, passwordConfirm, nickname, agreePersonal, isEmailVerified]);

<<<<<<< HEAD

  // 아이디 중복확인
  const handleCheckSearchId = async () => {
    // 유효성 검사
    if (!/^[a-z0-9]{5,15}$/.test(searchId)) {
      console.log('아이디 형식이 올바르지 않습니다.');
=======
  // 아이디 중복확인
  const handleCheckSearchId = async () => {
    if (!/^[a-z0-9]{5,15}$/.test(searchId)) {
      setSearchIdCheckMsg('아이디 형식이 올바르지 않습니다.');
>>>>>>> master
      return;
    }

    try {
<<<<<<< HEAD
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
=======
      const response = await axios.get(`${API_BASE_URL}/user/${searchId}`);
      if (response.status === 200) {
        setSearchIdCheckMsg('사용 가능한 아이디입니다.');
        setIsSearchIdAvailable(true);
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setSearchIdCheckMsg('이미 사용 중인 아이디입니다.');
        setIsSearchIdAvailable(false);
      } else {
        setSearchIdCheckMsg('아이디 중복확인 중 오류가 발생했습니다.');
        setIsSearchIdAvailable(false);
>>>>>>> master
      }
    }
  };

<<<<<<< HEAD

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
        setIsEmailVerificationSent(true);
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
=======
  // 이메일 인증
  const handleCheckEmail = async () => {
    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
      setEmailCheckMsg('올바른 이메일 형식이 아닙니다.');
      return;
    }

    setIsSendingEmail(true); // 이메일 전송 시작 표시

    try {
      const response = await axios.post(`${API_BASE_URL}/user/email`, { email });
      if (response.status === 200) {
        setIsEmailVerificationSent(true);
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setEmailCheckMsg('이미 사용중인 이메일 입니다.');
        setIsSendingEmail(false); // 전송 실패 시 초기화
>>>>>>> master
        return;
      }
    }

<<<<<<< HEAD
    // 이메일 인증번호 전송
    try {
      console.log("인증 이메일 전송 : ",email);
      const response = await axios.post(`${URI}/user/email/send`, { email }, {
        headers: {
          'Content-Type': 'application/json',
        },
        // withCredentials: true // 자격 증명 포함
      });
      console.log('이메일 인증번호 전송 성공!');
      console.log(response)
      setIsEmailVerificationSent(true);
      setTimer(300)
    } catch(error) {
      console.log('이메일 인증번호 전송 실패 : ', error);
    }
  }


=======
    try {
      await axios.post(`${API_BASE_URL}/user/email/send`, { email }, {
        headers: {
          'Content-Type': 'application/json',
        },
      });
      setIsEmailVerificationSent(true);
      setTimer(300);  // 타이머 시작
    } catch(error) {
      setIsSendingEmail(false); // 전송 실패 시 초기화
    }
  }

>>>>>>> master
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

<<<<<<< HEAD

  // 이메일 인증 코드 확인
  const handleVerifyEmailCode = async () => {
    try {
      const response = await axios.post(`${URI}/user/email/check`, { email, verifyCode: emailVerificationInput });
      console.log(response)
      console.log('이메일 인증 성공!');
      setIsEmailVerified(true);
      setIsEmailVerificationSent(false); // 이메일 인증 성공 후 인증번호 입력 필드 비활성화
    } catch (error) {
      console.error('이메일 인증 확인 실패: ', error);
=======
  // 이메일 인증 코드 확인
  const handleVerifyEmailCode = async () => {
    try {
      const response = await axios.post(`${API_BASE_URL}/user/email/check`, { email, verifyCode: emailVerificationInput });
      setIsEmailVerified(true);
      setIsEmailVerificationSent(false);
    } catch (error) {
>>>>>>> master
      setEmailVerificationMsg('인증번호가 일치하지 않습니다.');
    }
  };

<<<<<<< HEAD

// 회원가입 버튼 클릭
const handleSignUp = async () => {
    
  const userInfo = {
    // TODO default 이미지 추가하기
    email,
    searchId,
    password: sha256(password),
    nickname,
    gender,
    birthDate: birthdate,
    source,
    sidoCode: sido,
    gugunCode: gugun,
    profileInfo: "",
    isAd: agreeAdvertisement
  };

  //// git push 전, 지울부분  -> 이해를 위해 남겨두겠음..!////
  // 기본 프로필 이미지를 Blob으로 변환하여 추가
  // 장현준 : 아니 하.. 그냥 defaultProfile로 보낼려 했으나..
  // 뭔가 네트워크 쪽에서 싹 없애 버려서 null로 도착하는 문제가 있었음.
  // Blob으로 실제 파일을 만들어서 보내니까 잘 감.
  // 우려되는 부분은 await의 비동기 방식이라서 좀 느려질꺼같다는 우려..?
  // 지금은 단순히 사진 하나를 전송하는거지만, 추후 이 부분을 고려해야할꺼같음
  //// 지울부분 ////
=======
  // 회원가입 버튼 클릭
  const handleSignUp = async () => {
    const today = new Date().toISOString().split("T")[0];
  
    if (birthdate > today) {
      setDateError(true); // 미래 날짜 오류 발생 시 상태 설정
      return;
    }

    const userInfo = {
      email,
      searchId,
      password: sha256(password),
      nickname,
      gender,
      birthDate: birthdate,
      source,
      sidoCode,
      gugunCode,
      profileInfo: "",
      isAd: agreeAdvertisement
    };

>>>>>>> master
  const formData = new FormData();
  formData.append('userSignUpRequestDto', new Blob([JSON.stringify(userInfo)], { type: 'application/json' }));
  const response = await fetch(defaultProfile);
  const blob = await response.blob();
<<<<<<< HEAD
  const file = new File([blob], "../../assets/image/defaultprofile.png", { type: blob.type });
  formData.append('profile', file);
  
  console.log('정보 받기 성공!');
  console.log(userInfo);


  // 회원가입 요청
  try {
    await axios.post(`${URI}/user`, formData, {
=======
  const file = new File([blob], "defaultprofile.png", { type: blob.type });
  formData.append('profile', file);

  try {
    await axios.post(`${API_BASE_URL}/user`, formData, {
>>>>>>> master
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    setOpenModal(true);
  } catch (error) {
    console.error('회원가입 실패: ', error);
  }
};

const closeModal = () => {
  setOpenModal(false);
<<<<<<< HEAD
};


  return (
    <div>
      <form onSubmit={(e) => e.preventDefault()} className="form">
        <div>
          {!isFormValid && <p className="error">필수 항목을 모두 입력해 주세요.</p>}
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
          {searchIdCheckMsg && <p className="error">{searchIdCheckMsg}</p>}
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
          {!isEmailVerified && (
            <ATag
              content="인증번호 전송" onClick={handleCheckEmail}
            />
          )}
          {emailCheckMsg && <p className="error">{emailCheckMsg}</p>}
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
            {emailVerificationMsg && <p className="error">{emailVerificationMsg}</p>}
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
          {passwordCheckMsg && <p className="error">{passwordCheckMsg}</p>}
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
          {passwordConfirmCheckMsg && <p className="error">{passwordConfirmCheckMsg}</p>}
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
          {nicknameCheckMsg && <p className="error">{nicknameCheckMsg}</p>}
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
          className="drop-box"
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
            className="drop-box"
          />
          <SelectField
            value={gugun}
            onChange={(e) => setGugun(e.target.value)}
            options={['구/군']}
            isRequired={false}
            className="drop-box"
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
=======
  navigate('/login')
};

return (
  <div>
    <form onSubmit={(e) => e.preventDefault()} className="account-form">
      {!isFormValid && <p className="account-error">필수 항목을 모두 입력해 주세요.</p>}
      <div className="password-container">
        <InputField
          type="text"
          placeholder="아이디"
          value={searchId}
          onChange={(e) => {
            const value = e.target.value
            setSearchID(value)
            if (!/^[a-z0-9]{5,15}$/.test(value)) {
              setSearchIdCheckMsg('아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다.');
            } else {
              setSearchIdCheckMsg('');
            }
          }}
          isRequired={true}
          className="account-input"
        />
        <span
          onClick={handleCheckSearchId}
          className="password-toggle"
        >
          중복확인
        </span>
      </div>
      {searchIdCheckMsg && <p className="account-error">{searchIdCheckMsg}</p>}
      <div className="password-container">
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
          disabled={isEmailVerified}
          className="account-input"
        />
        {!isEmailVerified && (
          <span
            onClick={handleCheckEmail}
            className="password-toggle"
          >
            {isSendingEmail ? '재전송하기' : '인증번호 전송'}
          </span>
        )}
        {emailCheckMsg && <p className="account-error">{emailCheckMsg}</p>}
      </div>
      {isEmailVerificationSent && (
        <div className="password-container">
          <InputField
            type="text"
            placeholder="이메일 인증 코드"
            value={emailVerificationInput}
            onChange={(e) => setEmailVerificationInput(e.target.value)}
            isRequired={true}
            disabled={isEmailVerified}
            className="account-input"
          />
          <span className="password-toggle">
            <span>{formatTime(timer)}</span>
            <span onClick={handleVerifyEmailCode}> 인증확인</span>
          </span>
          {emailVerificationMsg && <p className="account-error">{emailVerificationMsg}</p>}
        </div>
      )}
      <div className="password-container">
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
          className="account-input"
        />
        <span
          onClick={() => setShowPassword(!showPassword)}
          className="password-toggle"
        >
          {showPassword ? '숨기기' : '보기'}
        </span>
        {passwordCheckMsg && <p className="account-error">{passwordCheckMsg}</p>}
      </div>
      <div className="password-container">
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
          className="account-input"
        />
        <span
          onClick={() => setShowPassword(!showPassword)}
          className="password-toggle"
        >
          {showPassword ? '숨기기' : '보기'}
        </span>
        {passwordConfirmCheckMsg && <p className="account-error">{passwordConfirmCheckMsg}</p>}
      </div>
      <div>
        <InputField
          type="text"
          placeholder="닉네임"
          value={nickname}
          onChange={(e) => {
            const value = e.target.value
            setNickname(value)
            if (value.length < 3 || value.length > 6) {
              setNicknameCheckMsg('닉네임은 3~6 글자여야 합니다.');
            } else{
              setNicknameCheckMsg('');
            }
          }}
          isRequired={false}
          className="account-input"
        />
        {nicknameCheckMsg && <p className="account-error">{nicknameCheckMsg}</p>}
      </div>
      <div>
        <div className='mb-3'>
          <h2>생년월일</h2>
        </div>
        <InputField
          type="date"
          placeholder="생일"
          value={birthdate}
          onChange={(e) => setBirthdate(e.target.value)}
          isRequired={false}
          className="account-input"
        />
      </div>
      <div className="profile-radio-field-container">
        <label className='mb-3'>성별</label>
        <div>
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
      </div>
      <div>
        <label>지역</label>
        <div className="profile-region-group mt-2">
          <div className="profile-region-select-container">
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
              {filteredGugunOptions.map(filteredGugunOption => (
                <option key={filteredGugunOption.gugunCode} value={filteredGugunOption.gugunCode}>
                  {filteredGugunOption.gugunName}
                </option>
              ))}
            </select>
          </div>
        </div>
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
        className="account-button"
      />
    </form>
    <ModalComplete 
      title={'회원가입 완료'} 
      content={'회원가입이 완료되었습니다'} 
      open={openModal} 
      onClose={closeModal} />

    <ModalComplete
      title="생일 재설정"
      content="날짜가 올바르지 않습니다."
      open={dateError}
      onClose={() => setDateError(false)}
    />

  </div>
);
};

export default SignUpForm;
>>>>>>> master
