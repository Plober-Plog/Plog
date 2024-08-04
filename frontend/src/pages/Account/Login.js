import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../../components/Account/LoginForm';
import SocialLogin from '../../components/Account/SocialLogin';
import ATag from '../../components/Common/ATag';

const Login = () => {
  const navigate = useNavigate();

  return (
    <div>
      <h1>로그인</h1>
      <LoginForm />
      <ATag 
        content={'회원가입 하기'}
        onClick={() => navigate('/signup')}
      />
      <ATag 
        content={'비밀번호 찾기'}
        onClick={() => navigate('/passoword/find')}
      />
      <SocialLogin />
    </div>
  );
}

export default Login;