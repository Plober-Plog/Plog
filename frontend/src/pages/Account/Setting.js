import React from "react";
import './Setting.css';

const Setting = () => {

  return (
    <div className="settings-container">
      <section className="settings-section">
        <h2>개인정보</h2>
        <div className="settings-item">회원정보 수정</div>
        <div className="settings-item">비밀번호 변경</div>
        <div className="settings-item">서비스 이용 동의</div>
      </section>
      <section className="settings-section">
        <h2>일반 설정</h2>
        <div className="settings-item">푸시 알림 설정</div>
        <div className="settings-item">뭐시기 설정</div>
      </section>
      <section className="settings-section">
        <h2>카테고리</h2>
        <div className="settings-item">카테고리 목록</div>
        <div className="settings-item">카테고리 목록</div>
      </section>
      <section className="settings-section">
        <h2>로그아웃</h2>
        <div className="settings-item">로그아웃</div>
      </section>
      <section className="settings-section">
        <h2>회원탈퇴</h2>
        <div className="settings-item">탈퇴하기</div>
      </section>
    </div>
  );
}

export default Setting;