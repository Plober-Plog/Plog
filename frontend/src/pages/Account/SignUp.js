import React from 'react';
import SignUpForm from '../../components/Account/SignUpForm';
import Btn from '../../components/Common/Btn';

const SignUp = () => {
  return (
    <div>
      <h1>회원가입</h1>
      <Btn
        content='로그인'
        onClick={
          () => console.log('로그인 페이지로 이동')
        }
      />
      <SignUpForm />
    </div>
  );
}

export default SignUp;