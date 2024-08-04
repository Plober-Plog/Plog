import React from 'react';
import LoginForm from '../../components/Account/LoginForm';
import SocialLogin from '../../components/Account/SocialLogin';
import ATag from '../../components/Account/ATag';

const Login = () => {
  return (
    <div>
      <h1>로그인</h1>
      <LoginForm />
      <ATag 
        content={'회원가입 하기'}
        onClick={
          () => {
            console.log('회원가입 페이지로 이동')
          }
        }
      />
      <ATag 
        content={'비밀번호 찾기'}
        onClick={
          () => {
            console.log('비밀번호 찾기 페이지로 이동')
          }
        }
      />
      <SocialLogin />
    </div>
  );
}

export default Login;