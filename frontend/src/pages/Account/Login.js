import React from 'react';
import LoginForm from '../../components/Account/LoginForm';
import SocialLogIn from '../../components/Account/SocialLogIn';
import AccountBtn from '../../components/Account/AccountBtn';
import Btn from '../../components/Common/Btn';
// import '../../components/Account/Login.css';

const Login = () => {
  return (
    <div className="container">
      <h1>로그인</h1>
      <LoginForm />
      <AccountBtn 
        content={'회원가입 하기'}
        onClick={
          () => {
            console.log('회원가입 페이지로 이동')
          }
        }
      />
      <AccountBtn 
        content={'비밀번호 찾기'}
        onClick={
          () => {
            console.log('비밀번호 찾기 페이지로 이동')
          }
        }
      />
      <SocialLogIn />
    </div>
  );
}

export default Login;