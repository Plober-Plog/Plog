import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../../components/Account/LoginForm';
import SocialLogin from '../../components/Account/SocialLogin';
import ATag from '../../components/Common/ATag';

const Login = () => {
  const navigate = useNavigate();

  return (
    <div className="account-container">
      <h1 className="account-title">로그인</h1>
      <LoginForm />
      <div className="account-a">
        <ATag 
          content={'회원가입 하기'}
          onClick={() => navigate('/signup')}
          className="account-link"
        />
        <span>|</span>
        <ATag 
          content={'비밀번호 찾기'}
          onClick={() => navigate('/password/find')}
          className="account-link"
        />
      </div>
      <SocialLogin />
    </div>
  );
}

export default Login;