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
  const redirectUri = `${window.location.origin}/popup-callback`;
  const setToken = useAuthStore((state) => state.setToken);
  const setUserData = useAuthStore((state) => state.setUserData);
  const [loginError, setLoginError] = useState('');
  const navigate = useNavigate();

  // 팝업 창 열기 및 소셜 로그인 처리
  const handleSocialLogin = (provider) => {
    let authUrl = '';

    if (provider === 'google') {
      authUrl = `https://accounts.google.com/o/oauth2/auth?client_id=${googleClientId}&redirect_uri=${redirectUri}/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email`;
    } else if (provider === 'kakao') {
      authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${redirectUri}/kakao&response_type=code`;
    } else if (provider === 'naver') {
      authUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${naverClientId}&redirect_uri=${redirectUri}/naver&response_type=code`;
    }

    // 팝업 창 열기
    const popup = window.open(authUrl, '_blank', 'width=500,height=600');
    
    const interval = setInterval(() => {
      if (popup.closed) {
        clearInterval(interval);
      }
    }, 1000);
  };

  // 팝업 창에서 인증 코드 수신
  useEffect(() => {
    const handlePopupMessage = (event) => {
      if (event.origin !== window.location.origin) return;

      const { code, provider } = event.data;
      if (code && provider) {
        fetchUserData(code, provider); // 인증 코드로 토큰 요청 및 유저 데이터 가져오기
      }
    };

    window.addEventListener('message', handlePopupMessage);

    return () => {
      window.removeEventListener('message', handlePopupMessage);
    };
  }, []);

  // 사용자 데이터를 가져오는 함수
  const fetchUserData = async (code, provider) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/user/login/social/${provider}`, { code });

      if (response.status === 200) {
        const { accessToken, refreshToken } = response.data;
        setToken(accessToken, refreshToken);

        // FCM 토큰 요청
        const fcmToken = await requestForToken();

        const tokenInfo = {
          accessToken: accessToken,
          notificationToken: fcmToken,
        };

        const userResponse = await API.get('/user');
        setUserData(userResponse.data);
        navigate('/'); // 메인 화면으로 이동
      } else {
        setLoginError('로그인에 실패했습니다.');
      }
    } catch (error) {
      setLoginError('사용자 정보를 가져오는데 실패했습니다.');
    }
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
};

// 팝업 콜백 처리 컴포넌트
export const PopupCallback = () => {
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    const provider = window.location.pathname.split('/').pop(); // 경로에서 provider 추출

    if (code) {
      // 메인 페이지에 메시지 전달
      window.opener.postMessage({ code, provider }, window.location.origin);
      window.close(); // 팝업 닫기
    }
  }, []);

  return null;
};

export default SocialLogin;
