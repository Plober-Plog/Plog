import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import kakao from '../../assets/image/kakao.png';
import naver from '../../assets/image/naver.png';
import google from '../../assets/image/google.png';
import './SocialLogin.css';
import useAuthStore from '../../stores/member';
import axios from 'axios';

const SocialLogin = () => {
  const googleClientId = '1044248850028-csgv8o025t0cf5u68kgfolvespii9j7b.apps.googleusercontent.com';
  const kakaoClientId = '0b74706441a714cf08af98a8d8121147';
  const naverClientId = 'jqR7BnKBxSlcPNDnGrTs';
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const redirectUri = `${API_BASE_URL}/user/login/oauth2/code/`;
  const setToken = useAuthStore((state) => state.setToken);
  const setUserData = useAuthStore((state) => state.setUserData);
  const [loginError, setLoginError] = useState('');
  const navigate = useNavigate();

  const handleSocialLogin = (provider) => {
    let authUrl = '';

    if (provider === 'google') {
      authUrl = `https://accounts.google.com/o/oauth2/auth?client_id=${googleClientId}&redirect_uri=${redirectUri}google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email`;
    } else if (provider === 'kakao') {
      authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${redirectUri}kakao&response_type=code`;
    } else if (provider === 'naver') {
      authUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${naverClientId}&redirect_uri=${redirectUri}naver&response_type=code`;
    }

    window.location.href = authUrl;
  };

  return (
    <div className="social-login">
      <p>간편로그인</p>
      <div className="social-buttons flex-row">
        <button className="social-button justify-center" onClick={() => handleSocialLogin('google')}>
          <img src={google} alt="구글" className="social-icon" />
        </button>
        <button className="social-button justify-center" onClick={() => handleSocialLogin('kakao')}>
          <img src={kakao} alt="카카오" className="social-icon" />
        </button>
        <button className="social-button justify-center" onClick={() => handleSocialLogin('naver')}>
          <img src={naver} alt="네이버" className="social-icon" />
        </button>
      </div>
      {loginError && <p className="error-message">{loginError}</p>}
    </div>
  );
}

export default SocialLogin;
