import React from 'react';
import LoginForm from '../../components/LoginForm';
import SocialLogIn from '../../components/Account/SocialLogIn';

const Login = () => {
  return (
    <div>
      <h1>로그인</h1>
      <LoginForm />
      <SocialLogIn />
    </div>
  );
}

export default Login;