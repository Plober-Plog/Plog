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
<<<<<<< HEAD
  const clearToken = useAuthStore((state) => state.clearToken);

=======
  const [openModal, setOpenModal] = useState(false);
  const clearToken = useAuthStore((state) => state.clearToken);

  const getrefreshToken = () => useAuthStore.getState().refreshToken;
>>>>>>> master
  const getAccessToken = () => useAuthStore.getState().accessToken;

  const handleLogout = async () => {
    try {
<<<<<<< HEAD
      const accessToken = getAccessToken();
      if (!accessToken) {
        console.log('이미 로그아웃된 상태입니다.');
=======
      const refreshToken = getrefreshToken();
      if (!refreshToken) {
>>>>>>> master
        clearToken();
        navigate('/login');
        return;
      }

      const response = await API.get('/user/logout');
<<<<<<< HEAD
      console.log('로그아웃 성공:', response.data);

=======
>>>>>>> master
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
<<<<<<< HEAD
        console.log('로그인되지 않은 상태입니다.');
=======
>>>>>>> master
        clearToken();
        navigate('/login');
        return;
      }

      const response = await API.delete('/user');
<<<<<<< HEAD
      console.log('회원 탈퇴 성공:', response.data);
=======
>>>>>>> master

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
<<<<<<< HEAD
          <div className="settings-item">서비스 이용 동의</div>
        </div>
      </section>
      <section className="settings-section">
        <h2>일반 설정</h2>
        <div className="settings-box">
          <div className="settings-item">푸시 알림 설정</div>
          <div className="settings-item">뭐시기 설정</div>
        </div>
      </section>
      <section className="settings-section">
        <h2>카테고리</h2>
        <div className="settings-box">
          <div className="settings-item">카테고리 목록</div>
          <div className="settings-item">카테고리 목록</div>
=======
        </div>
      </section>
      <section className="settings-section">
        <h2>알림 설정</h2>
        <div className="settings-box">
          <div className="settings-item" onClick={() => navigate('/profile/pushalarm')}>푸시알림 설정</div>
>>>>>>> master
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
<<<<<<< HEAD
        content="정말로 탈퇴 하시겠습니까?"
=======
        content="탈퇴 하시겠습니까?"
>>>>>>> master
        confirmText="탈퇴하기"
        onConfirm={handleSignOut}
        onClose={() => setShowSignoutConfirm(false)}
      />
      <ModalConfirm
        open={showLogoutConfirm}
        title="로그아웃"
<<<<<<< HEAD
        content="정말 로그아웃 하시겠습니까?"
=======
        content="로그아웃 하시겠습니까?"
>>>>>>> master
        confirmText="로그아웃"
        onConfirm={handleLogout}
        onClose={() => setShowLogoutConfirm(false)}
      />
    </div>
  );
}

export default Setting;
