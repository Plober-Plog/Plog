import React from 'react';
import LogInForm from '../../components/Account/LogInForm';
import SocialLogIn from '../../components/Account/SocialLogIn';

function logIn() {
  return (
    <div>
      <h1>로그인</h1>
      <LogInForm />
      
      <SocialLogIn />
    </div>
  );
}

export default logIn;