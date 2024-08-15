import API from '../../apis/api';
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuthStore from '../../stores/member';
import ModalConfirm from "../../components/Common/ModalConfirm";
import './Setting.css';

const Setting = () => {
  const navigate = useNavigate();
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const [showSignoutConfirm, setShowSignoutConfirm] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const clearToken = useAuthStore((state) => state.clearToken);

  const getrefreshToken = () => useAuthStore.getState().refreshToken;
  const getAccessToken = () => useAuthStore.getState().accessToken;

  const handleLogout = async () => {
    try {
      const refreshToken = getrefreshToken();
      if (!refreshToken) {
        clearToken();
        navigate('/login');
        return;
      }

      const response = await API.get('/user/logout');
      clearToken();
      navigate('/login');
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  const handleSignOut = async () => {
    try {
      const accessToken = getAccessToken();
      if (!accessToken) {
        clearToken();
        navigate('/login');
        return;
      }

      const response = await API.delete('/user');

      clearToken();
      navigate('/login');
    } catch (error) {
      console.error('회원 탈퇴 실패:', error);
    }
  };

  return (
    <div className="settings-container">
      <section className="settings-section">
        <h2>개인정보</h2>
        <div className="settings-box">
          <div className="settings-item" onClick={() => navigate('/password/change')}>비밀번호 변경</div>
          <div className="settings-item" onClick={() => navigate('/profileupdate')}>회원정보 수정</div>
        </div>
      </section>
      <section className="settings-section">
        <h2>알림 설정</h2>
        <div className="settings-box">
          <div className="settings-item" onClick={() => navigate('/profile/pushalarm')}>푸시알림 설정</div>
        </div>
      </section>
      <section className="settings-section">
        <h2>로그아웃</h2>
        <div className="settings-box">
          <div className="settings-item" onClick={() => setShowLogoutConfirm(true)}>로그아웃</div>
        </div>
      </section>
      <section className="settings-section">
        <h2>회원탈퇴</h2>
        <div className="settings-box">
          <div className="settings-item" onClick={() => setShowSignoutConfirm(true)}>탈퇴하기</div>
        </div>
      </section>
      <ModalConfirm
        open={showSignoutConfirm}
        title="회원탈퇴"
        content="탈퇴 하시겠습니까?"
        confirmText="탈퇴하기"
        onConfirm={handleSignOut}
        onClose={() => setShowSignoutConfirm(false)}
      />
      <ModalConfirm
        open={showLogoutConfirm}
        title="로그아웃"
        content="로그아웃 하시겠습니까?"
        confirmText="로그아웃"
        onConfirm={handleLogout}
        onClose={() => setShowLogoutConfirm(false)}
      />
    </div>
  );
}

export default Setting;
