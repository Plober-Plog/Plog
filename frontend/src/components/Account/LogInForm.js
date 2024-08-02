import { useState } from 'react';
import { useNavigate } from'react-router-dom';
import axios from 'axios';
import sha256 from 'js-sha256';
import useAuthStore from './useAuthStore';

import Btn from '../Common/Btn';
import InputField from './InputField';
import AccountBtn from './AccountBtn';

// const API_URL = 'http://localhost:3000/api/user';
const API_URL = 'https://i11b308.p.ssafy.io/api/user';


const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  // 로그인 확인
  const [loginError, setLoginError] = useState('');
  const setToken = useAuthStore((state) => state.setToken);

  // 로그인 버튼 클릭
  const handleLogin = async(event) => {
    event.preventDefault();

    const userInfo = {
      email,
      password: sha256(password),
    }

    console.log('로그인 정보 받기 성공!')
    console.log(userInfo)

    try {
      const response = await axios.post(API_URL, userInfo)

      const { accessToken, refreshToken } = response.data;

      if (response.data.token) {
        console.log('로그인 성공!');
        setToken(response.data.token); // JWT 토큰을 Zustand 스토어에 저장
      } else {
        setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
      }

      // TODO 메인으로 이동

    } catch (error) {
      console.error('로그인 오류:', error);
    }

  };

  return (
    <div>
      <form onSubmit={e => e.preventDefault()}>
        <div>
          <InputField
            type="email" 
            placeholder="이메일" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
          />
        </div>
        <div>
          <InputField
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)}
            isRequired={true} 
          />
          <AccountBtn
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
        </div>
        {loginError && <p>{loginError}</p>}
        <Btn
          content="로그인"
          disabled={!email || !password}
          onClick={handleLogin}
        />
      </form>
    </div>
  );
}

export default LoginForm;
