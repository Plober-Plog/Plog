import React from "react";
import { useNavigate } from 'react-router-dom';

const HeaderMenuBtn = () => {
  const navigate = useNavigate();

  const handleNavigate = () => {
    navigate('/setting');
  }

  return (
    <div onClick={handleNavigate}>
      <i>햄버거 버튼</i>
    </div>
  )
}

export default HeaderMenuBtn;