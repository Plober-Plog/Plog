import React from 'react';
<<<<<<< HEAD
import kakao from '../../assets/image/kakao.png'
import naver from '../../assets/image/naver.png'
import google from '../../assets/image/google.png'
import './SocialLogin.css';

const SocialLogin = () => {
  return (
    <div className="social-login">
      <p>간편로그인</p>
      <div className="social-buttons">
        <button className="social-button">
          <img src={kakao} alt="카카오" className="social-icon" />
        </button>
        <button className="social-button">
          <img src={naver} alt="네이버" className="social-icon" />
        </button>
        <button className="social-button">
          <img src={google} alt="구글" className="social-icon" />
        </button>
=======
import kakao from '../../assets/image/kakao.png';
import naver from '../../assets/image/naver.png';
import google from '../../assets/image/google.png';
import './SocialLogin.css';

const SocialLogin = () => {
  const googleClientId = '1044248850028-csgv8o025t0cf5u68kgfolvespii9j7b.apps.googleusercontent.com';
  const kakaoClientId = 'YOUR_KAKAO_CLIENT_ID';
  const naverClientId = 'YOUR_NAVER_CLIENT_ID';
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const redirectUri = `${API_BASE_URL}/user/login/oauth2/code/`;

  const handleGoogleLogin = () => {
    
    const googleAuthUrl = `https://accounts.google.com/o/oauth2/auth?client_id=${googleClientId}&redirect_uri=${redirectUri}google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email`;
    window.open(googleAuthUrl, '_blank', 'width=500,height=600');
  };

  const handleKakaoLogin = () => {
    const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${redirectUri}kakao&response_type=code`;
    window.open(kakaoAuthUrl, '_blank', 'width=500,height=600');
  };

  const handleNaverLogin = () => {
    const naverAuthUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${naverClientId}&redirect_uri=${redirectUri}naver&response_type=code`;
    window.open(naverAuthUrl, '_blank', 'width=500,height=600');
  };

  return (
    <div className="social-login">
      <p>간편로그인</p>
      <div className="social-buttons flex-col">
        <button className="social-button justify-center" onClick={handleGoogleLogin}>
          <img src={google} alt="구글" className="social-icon" />
        </button>
        <button className="social-button hidden" onClick={handleKakaoLogin}>
          <img src={kakao} alt="카카오" className="social-icon" />
        </button>
        <button className="social-button hidden" onClick={handleNaverLogin}>
          <img src={naver} alt="네이버" className="social-icon" />
        </button>
>>>>>>> master
      </div>
    </div>
  );
}

<<<<<<< HEAD
export default SocialLogin;
=======
export default SocialLogin;
>>>>>>> master
