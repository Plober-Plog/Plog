import React, { useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import plogLogo from "../../assets/icon/header/ploglogo.png";
import hamBurger from "../../assets/icon/header/hamburger.png";
import back from "../../assets/icon/header/back.png";
import './Header.css';

const Header = () => {
  const navigate = useNavigate();

  return (
    <div className="header-container">
      <button className="header-button" onClick={() => navigate(-1)}>
        <img src={back} alt="Back" className="header-icon-small" />
      </button>
      <div className="header-logo" onClick={() => navigate('/')}>
        <img src={plogLogo} alt="Logo" className="header-icon" />
      </div>
      <button className="header-button" onClick={() => navigate('/setting')}>
        <img src={hamBurger} alt="Menu" className="header-icon-small" />
      </button>
      <div className="header-spacing"></div>
    </div>
  );
};

export default Header;
