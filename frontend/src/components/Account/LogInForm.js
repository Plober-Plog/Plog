import { useState } from 'react';
import axios from 'axios';
import sha256 from 'js-sha256';

import Btn from '../Common/Btn';
import InputField from './InputField';
import AccountBtn from './AccountBtn';

const API_URL = 'http://localhost:3000/api/user';


const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

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

      // 토큰을 로컬 스토리지에 저장
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);

      console.log('로그인 성공!', response.data);

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
