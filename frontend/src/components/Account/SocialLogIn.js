import React from 'react';

const SocialLogin = () => {
  return (
    <div className="social-login">
      <p>간편로그인</p>
      <div className="social-buttons">

        {/* 버튼은 아이콘 받아와서 변경할 예정 */}
        <button className="social-button kakao">카카오</button>
        <button className="social-button naver">네이버</button>
        <button className="social-button google">구글</button>
      </div>
    </div>
  );
}

export default SocialLogin;