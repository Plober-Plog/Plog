import React from 'react';
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
      </div>
    </div>
  );
}

export default SocialLogin;