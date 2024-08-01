import React from 'react';
import LoginForm from '../../components/Account/LoginForm';
import SocialLogIn from '../../components/Account/SocialLogIn';
import Btn from '../../components/Common/Btn';

const Login = () => {
  return (
    <div>
      <h1>로그인</h1>
      <LoginForm />
      <Btn 
        content={'회원가입하기'}
        onClick={
          () => {
            console.log('회원가입 페이지로 이동')
          }
        }
      />
      <Btn 
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