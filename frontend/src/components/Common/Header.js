import React from 'react';
import { useNavigate } from "react-router-dom";
import plogLogo from "../../assets/icon/header/ploglogo.png";
import hamBurger from "../../assets/icon/header/hamburger.png";
import back from "../../assets/icon/header/back.png";
import './Header.css';
import useAuthStore from '../../stores/member';

const Header = () => {
  const navigate = useNavigate();
  const { isLogin } = useAuthStore();

  return (
    <div className="header-container">
      <button className="header-button" onClick={() => navigate(-1)}>
        <img src={back} alt="Back" className="header-icon-small" />
      </button>
      <div className="header-logo" onClick={() => navigate('/')}>
        <img src={plogLogo} alt="Logo" className="header-icon" />
      </div>
      {isLogin ? (
        <button className="header-button" onClick={() => navigate('/setting')}>
          <img src={hamBurger} alt="Menu" className="header-icon-small" />
        </button>
      ) : (
        <button className="header-button" onClick={() => navigate('/login')}>
          <span className="header-icon-small">Login</span>
        </button>
      )}
      <div className="header-spacing"></div>
    </div>
  );
};

export default Header;
