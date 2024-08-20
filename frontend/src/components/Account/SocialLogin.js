import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import kakao from '../../assets/image/kakao.png';
import naver from '../../assets/image/naver.png';
import google from '../../assets/image/google.png';
import './SocialLogin.css';
import { requestForToken } from '../../firebase'; // FCM 관련 코드 추가
import useAuthStore from '../../stores/member';
import axios from 'axios';
import API from '../../apis/api';

const SocialLogin = () => {
  const googleClientId = '1044248850028-csgv8o025t0cf5u68kgfolvespii9j7b.apps.googleusercontent.com';
  const kakaoClientId = '0b74706441a714cf08af98a8d8121147';
  const naverClientId = 'jqR7BnKBxSlcPNDnGrTs';
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const redirectUri = `${API_BASE_URL}/user/login/oauth2/code`; // 백엔드의 리디렉션 URI
  const setToken = useAuthStore((state) => state.setToken);
  const setUserData = useAuthStore((state) => state.setUserData);
  const [loginError, setLoginError] = useState('');
  const navigate = useNavigate();

  // 소셜 로그인 URL 생성 및 리디렉션
  const handleSocialLogin = (provider) => {
    let authUrl = '';

    if (provider === 'google') {
      authUrl = `https://accounts.google.com/o/oauth2/auth?client_id=${googleClientId}&redirect_uri=${redirectUri}/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email`;
    } else if (provider === 'kakao') {
      authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${redirectUri}/kakao&response_type=code`;
    } else if (provider === 'naver') {
      authUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${naverClientId}&redirect_uri=${redirectUri}/naver&response_type=code`;
    }

    // 소셜 로그인 페이지로 리디렉션
    window.location.href = authUrl;
  };

  // 서버에서 리디렉션된 URL에서 토큰을 처리
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await API.get('/user');
        if (response.status === 200) {
          setUserData(response.data);
          navigate('/'); // 메인 화면으로 이동
        } else {
          setLoginError('로그인에 실패했습니다.');
        }
      } catch (error) {
        setLoginError('사용자 정보를 가져오는데 실패했습니다.');
      }
    };

    // URL에서 서버로부터 전달된 토큰 확인
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('accessToken');
    const refreshToken = urlParams.get('refreshToken');

    if (accessToken && refreshToken) {
      // 토큰을 상태에 저장하고 사용자 데이터 가져오기
      setToken(accessToken, refreshToken);
      fetchUserData();
    }
  }, [setToken, setUserData, navigate]);

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
};

export default SocialLogin;
