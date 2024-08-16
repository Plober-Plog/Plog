import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import axios from 'axios';
import sha256 from 'js-sha256';
import useAuthStore from '../../stores/member';
import { requestForToken } from '../../firebase'; // FCM 관련 코드 추가
import Btn from '../Common/Btn';
import InputField from '../Common/InputField';


const LoginForm = () => {
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loginError, setLoginError] = useState('');
  const setToken = useAuthStore((state) => state.setToken);
  const setUserData = useAuthStore((state) => state.setUserData);
  const navigate = useNavigate();


  const handleLogin = async (event) => {
    event.preventDefault();

    // FCM 토큰 요청
    const fcmToken = await requestForToken();

    // console.log(fcmToken);

    const userInfo = {
      email,
      password: sha256(password),
      notificationToken: fcmToken, // FCM 토큰 추가
    };

    try {
      const response = await axios.post(`${API_BASE_URL}/user/login`, userInfo);
      const { accessToken, refreshToken } = response.data;

      if (accessToken && refreshToken) {
        setToken(accessToken, refreshToken);
        const userResponse = await API.get('/user');
        setUserData(userResponse.data);
        navigate('/');
      } else {
        setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
      }
    } catch (error) {
      setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div>
      <form onSubmit={handleLogin} className="account-form">
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
            className="account-input"
          />
        </div>
        <div className="password-container">
          <InputField
            type={showPassword ? 'text' : 'password'}
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            isRequired={true}
            className="account-input password-input"
          />
          <span
            onClick={() => setShowPassword(!showPassword)}
            className="password-toggle"
          >
            {showPassword ? '숨기기' : '보기'}
          </span>
        </div>
        {loginError && <p className="account-error">{loginError}</p>}
        <Btn
          content="로그인"
          disabled={!email || !password}
          onClick={handleLogin}
          className="account-button"
        />
      </form>
    </div>
  );
};

export default LoginForm;
