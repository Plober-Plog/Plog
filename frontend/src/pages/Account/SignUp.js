import React from 'react';
import { useNavigate } from 'react-router-dom';
import SignUpForm from '../../components/Account/SignUpForm';
import ATag from '../../components/Common/ATag';
import './Account.css';

const SignUp = () => {
  const navigate = useNavigate();

  return (
    <div className="account-signup-container">
      <h1 className="account-title mt-20">회원가입</h1>
      <div>
        <SignUpForm />
      </div>
    </div>
  );
}

export default SignUp;