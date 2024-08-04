import { useState } from 'react';
import { useNavigate } from'react-router-dom';
import axios from 'axios';
import API from '../../apis/api';
import sha256 from 'js-sha256';
import useAuthStore from '../../stores/store';

import Btn from '../Common/Btn';
import InputField from './InputField';
import ATag from './ATag';


const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  // 로그인 확인
  const [loginError, setLoginError] = useState('');
  const setToken = useAuthStore((state) => state.setToken);

  const URI = 'https://i11b308.p.ssafy.io/api';
  const navigate = useNavigate();

  // 로그인 버튼 클릭
  const handleLogin = async(event) => {
    event.preventDefault();

    const userInfo = {
      email,
      password: sha256(password),
    }

    try {
      const response = await API.post(`${URI}/user/login`, userInfo)
      const token = response.headers['authorization'];
      console.log('토큰 : ', token)

      if (token) {
        console.log(response.data)
        console.log('로그인 성공!');
        setToken(token); // JWT 토큰을 Zustand 스토어에 저장
        navigate('/');
      } else {
        setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
      }

    } catch (error) {
      console.error('로그인 오류:', error);
      if (error.response) {
        console.error('서버 응답 데이터:', error.response.data);
        setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
      }
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
          <ATag
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
