import React from 'react';
import { useNavigate } from 'react-router-dom';

const HeaderBackBtn = () => {
  let navigate = useNavigate();

  return (
    <div onClick={() => navigate(-1)}>
      <i>뒤로가기</i>
    </div>
  )
}

export default HeaderBackBtn;