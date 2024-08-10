import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthStore from '../../stores/member';
import ModalComplete from '../Common/ModalComplete';

const PrivateRoute = ({ children }) => {
  const { isLogin } = useAuthStore();
  const [showModal, setShowModal] = useState(false);
  const [redirect, setRedirect] = useState(false);

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
