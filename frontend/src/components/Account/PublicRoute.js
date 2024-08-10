import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthStore from '../../stores/member';
import ModalComplete from '../Common/ModalComplete';

const PublicRoute = ({ children }) => {
  const { isLogin } = useAuthStore();
  const [showModal, setShowModal] = useState(false);
  const [redirect, setRedirect] = useState(false);

  useEffect(() => {
    if (isLogin) {
      setShowModal(true);
    }
  }, [isLogin]);

  const handleCloseModal = () => {
    setShowModal(false);
    setRedirect(true);
  };

  if (redirect) {
    return <Navigate to="/" />;
  }

  return (
    <>
      {isLogin ? (
        <>
          <ModalComplete
            open={showModal}
            onClose={handleCloseModal}
            title="로그인 완료"
            content="이미 로그인된 회원입니다."
          />
        </>
      ) : (
        children
      )}
    </>
  );
};

export default PublicRoute;
