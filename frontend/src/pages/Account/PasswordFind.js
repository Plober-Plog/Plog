import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { verifyEmail } from '../../api';

import Btn from '../../components/Common/Btn';
import InputField from '../../components/Account/InputField';
import AccountBtn from '../../components/Account/AccountBtn';

const PasswordFind = () => {
  // input fields
  const [email, setEmail] = useState('');
  const [emailError, setEmailError] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  // 인증 코드 전송
  const [timer, setTimer] = useState(0);
  const [codeSent, setCodeSent] = useState(false);
  const [codeError, setCodeError] = useState('');
  const navigate = useNavigate();


  // 인증코드 타이머 설정
  useEffect(() => {
    let interval;
    if (codeSent && timer > 0) {
      interval = setInterval(() => {
        setTimer((prevTimer) => prevTimer - 1);
      }, 1000);
    } else if (timer === 0) {
      clearInterval(interval);
      setCodeSent(false);
    }
    return () => clearInterval(interval);
  }, [codeSent, timer]);


  // 이메일 인증 코드 전송
  const handleEmailVerification = async () => {
    try {
      const response = await axios.post(`${API}/user/email/send`, { email });
      if (response.data) {
        setCodeSent(true);
        setTimer(300); // 5분 타이머
      } else {
        setEmailError('이메일 인증에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      setEmailError('이메일 인증에 실패했습니다. 다시 시도해주세요.');
    }
  };


  // 이메일 인증 코드 확인
  const handleCodeVerification = async () => {
    try {
      const response = await axios.post(`${API}/user/email/check`, { code: verificationCode });
      if (response.data.result) {
        navigate('/password-update');
      } else {
        setCodeError('인증 코드가 올바르지 않습니다.');
      }
    } catch (error) {
      setCodeError('인증 코드 확인에 실패했습니다. 다시 시도해주세요.');
    }
  };


  // 타이머 설정
  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
  };


  return (
    <div>
      <h2>비밀번호 찾기</h2>
      <form onSubmit={e => e.preventDefault()}>
        <div>
          <InputField
            type="email" 
            placeholder="이메일" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
          />
          <AccountBtn 
            content='인증하기'
            onClick={() => {handleEmailVerification}}
          />
        </div>
        {codeSent && (
          <div>
            <InputField
              type="text"
              placeholder="인증 코드"
              value={verificationCode}
              onChange={(e) => setVerificationCode(e.target.value)}
              isRequired={true}
            />
            <p>남은 시간: {formatTime(timer)}</p>
            {codeError && <p>{codeError}</p>}
          </div>
        )}
        <Btn
          content="비밀번호 찾기"
          disabled={!verificationCode}
          onClick={handleCodeVerification}
        />
      </form>
    </div>
  )
}

export default PasswordFind;