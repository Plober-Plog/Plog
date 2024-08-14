import React from 'react';
import { useNavigate } from 'react-router-dom';
import SignUpForm from '../../components/Account/SignUpForm';
import ATag from '../../components/Common/ATag';
import './Account.css';

const SignUp = () => {
  const navigate = useNavigate();

  return (
    <div className="account-container mt-20 mb-20">
      <h1 className="account-title mt-10">회원가입</h1>
      <div className='mb-10'>
        <SignUpForm />
      </div>
    </div>
  );
}

export default SignUp;