import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthStore from '../../stores/member';
import ModalComplete from '../Common/ModalComplete';

const PrivateRoute = ({ children }) => {
  const { isLogin, autoLogin } = useAuthStore();
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [redirect, setRedirect] = useState(false);

  // 토큰 없을때 자동 로그인
  useEffect(() => {
    const checkLoginStatus = async () => {
      await autoLogin();  // 자동 로그인 시도
      setLoading(false);   // 자동 로그인 후 로딩 상태 해제
    };

    checkLoginStatus();
  }, [autoLogin]);

  if (loading) {
    return <div>Loading...</div>;
  }

  // 토큰 여전히 없으면 로그인 페이지로 이동
  if (!isLogin) {
    if (!showModal && !redirect) {
      setShowModal(true);
    }
    return (
      <>
        <ModalComplete 
          open={showModal} 
          onClose={() => {
            setShowModal(false);
            setRedirect(true);
          }}
          title="로그인이 필요한 페이지입니다" 
          content="로그인 후 이용해주세요." 
        />
        {redirect && <Navigate to="/login" replace />}
      </>
    );
  }

  return children;
};

export default PrivateRoute;
