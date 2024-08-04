import React from 'react';
import { useNavigate } from 'react-router-dom';
import SignUpForm from '../../components/Account/SignUpForm';
import ATag from '../../components/Account/ATag';

const SignUp = () => {
  const navigate = useNavigate();

  return (
    <div>
      <h1>회원가입</h1>
      <ATag
        content='로그인'
        onClick={() => navigate('/login')}
      />
      <SignUpForm />
    </div>
  );
}

export default SignUp;