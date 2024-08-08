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