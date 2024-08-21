<<<<<<< HEAD
import React, { useState, useEffect } from 'react';
import HeaderBackBtn from './HeaderBackBtn';
import HeaderMenuBtn from './HeaderMenuBtn';

const Header = () => {

  return (
    <div>
      <HeaderBackBtn />
      <i>로고</i>
      <HeaderMenuBtn />
    </div>
  )
}

export default Header;
=======
import React from 'react';
import { useNavigate } from "react-router-dom";
import plogLogo from "../../assets/icon/header/ploglogo.png";
import hamBurger from "../../assets/icon/header/hamburger.png";
import back from "../../assets/icon/header/back.png";
// import './Header.css';
import useAuthStore from '../../stores/member';

const Header = () => {
  const navigate = useNavigate();
  const { isLogin } = useAuthStore();

  return (
    <header className="w-full max-w-custom mx-auto flex justify-between items-center bg-white border-b border-gray-200 fixed top-0 left-0 right-0">
      <button className="p-4" onClick={() => navigate(-1)}>
        <img src={back} alt="뒤로가기" className="w-6 h-6" />
      </button>
      <div className="flex-1 flex justify-center" onClick={() => navigate('/')}>
        <img src={plogLogo} alt="로고" className="h-8" />
      </div>
      <button className="p-4" onClick={() => isLogin ? navigate('/setting') : navigate('/login')}>
        {isLogin ? (
          <img src={hamBurger} alt="메뉴" className="w-6 h-6" />
        ) : (
          <span className="text-sm text-gray-600">로그인</span>
        )}
      </button>
    </header>
  );
};

export default Header;
>>>>>>> master
