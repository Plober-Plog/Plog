import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Btn from '../../components/Common/Btn';
import InputField from '../../components/Common/InputField';
import ATag from '../../components/Common/ATag';

const PasswordFind = () => {
  // input fields
  const [email, setEmail] = useState('');
  const [emailVerificationInput, setEmailVerificationInput] = useState('');
  // 인증 코드 전송
  const [timer, setTimer] = useState(0);
  const [emailCheckMsg, setEmailCheckMsg] = useState('');
  const [isEmailVerificationSent, setIsEmailVerificationSent] = useState(false);
  const [emailVerificationMsg, setEmailVerificationMsg] = useState('');
  const [isEmailVerified, setIsEmailVerified] = useState(false); // 인증완료 여부
  const [userId, setUserId] = useState(null);

  const URI = 'https://i11b308.p.ssafy.io/api'
  const navigate = useNavigate();


  // 인증코드 타이머 설정
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


  // 이메일 인증 코드 전송
  const handleEmailVerification = async () => {
  
    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
      console.log('이메일 형식이 올바르지 않습니다.');
      return;
    }

    try {
      const response = await axios.post(`${URI}/user/password/send`, { email });
      if (response.data) {
        setIsEmailVerificationSent(true);
        setTimer(300); // 5분 타이머
        console.log('인증 코드 전송 성공!');
      } else {
        setEmailCheckMsg('이메일 인증에 실패했습니다. 다시 시도해주세요.');
        console.log('인증 코드 전송 실패! : ',response);
      }
    } catch (error) {
      setEmailCheckMsg('이메일 인증에 실패했습니다. 다시 시도해주세요.');
      console.log('인증 코드 전송 실패! : ', error);
    }
  };


  // 이메일 인증 코드 확인
  const handleCodeVerification = async () => {
    try {
      const response = await axios.post(`${URI}/user/password/check`, { email, verifyCode: emailVerificationInput });
      if (response.data.result) {
        setIsEmailVerified(true);
        setUserId(response.data.userId); // 유저 pk
        setEmailVerificationMsg('이메일 인증 성공!');
      } else {
        setEmailVerificationMsg('인증 코드가 올바르지 않습니다.');
      }
    } catch (error) {
      if (error.response && error.response.status === 408) {
        setEmailVerificationMsg('인증 시간이 만료되었습니다. 다시 시도해주세요.');
        console.log('인증 시간이 만료되었습니다. : ', error);
      } else if (error.response && error.response.status === 400) {
        setEmailVerificationMsg('잘못된 인증 코드입니다. 다시 시도해주세요.');
        console.log('잘못된 인증 코드입니다. : ', error);
      } else {
        setEmailVerificationMsg('인증 코드 확인에 실패했습니다. 다시 시도해주세요.');
      }
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
          />
          <ATag
            content="인증하기" onClick={handleEmailVerification}
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
            />
            <p>{formatTime(timer)}</p>
            <ATag content="인증 확인" onClick={handleCodeVerification} />
            {emailVerificationMsg && <p>{emailVerificationMsg}</p>}
          </div>
        )}
          <Btn
            content="비밀번호 찾기"
            disabled={!isEmailVerified}
            onClick={() => navigate('/password/update', { state: { userId } })}
          />
      </form>
    </div>
  )
}

export default PasswordFind;