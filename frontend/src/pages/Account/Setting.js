import API from '../../apis/api';
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuthStore from '../../stores/store';
import ModalConfirm from "../../components/Common/ModalConfirm";

const Setting = () => {
  const navigate = useNavigate();
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const clearToken = useAuthStore((state) => state.clearToken);

  // 로그아웃
  const handleLogout = async () => {
    try {
      const accessToken = useAuthStore.getState().accessToken;
      if (!accessToken) {
        console.log('이미 로그아웃된 회원입니다.');
        console.log(accessToken) // null인지 확인
        return;
      }

      // 서버에 로그아웃 요청
      const response = await API.get('/user/logout')
      console.log('로그아웃 성공:', response.data);

      // 토큰 제거 후 로그인 페이지로 이동
      clearToken();
      navigate('/login');

    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  return (
    <div className="settings-container">
      <section className="settings-section">
        <h2>개인정보</h2>
        <div className="settings-item" onClick={() => navigate('/password/change')}>비밀번호 변경</div>
        <div className="settings-item" onClick={() => navigate('/profileupdate')}>회원정보 수정</div>
        <div className="settings-item">서비스 이용 동의</div>
      </section>
      <section className="settings-section">
        <h2>일반 설정</h2>
        <div className="settings-item">푸시 알림 설정</div>
      </section>
      <section className="settings-section">
        <h2>카테고리</h2>
        <div className="settings-item">카테고리 목록</div>
        <div className="settings-item">카테고리 목록</div>
      </section>
      <section className="settings-section">
        <h2>로그아웃</h2>
        <div
          className="settings-item"
          onClick={() => setShowLogoutConfirm(true)}
        >
          로그아웃
        </div>
      </section>
      <section className="settings-section">
        <h2>회원탈퇴</h2>
        <div className="settings-item">탈퇴하기</div>
      </section>
      <ModalConfirm
        open={showLogoutConfirm}
        title="로그아웃"
        content="정말 로그아웃 하시겠습니까?"
        confirmText="로그아웃"
        onConfirm={handleLogout}
        onClose={() => setShowLogoutConfirm(false)}
      />
    </div>
  );
}

export default Setting;
