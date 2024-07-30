import React from 'react';
import Btn from '../Common/Btn';

const logInForm = () => {
  return (
    <form>
      <label>
        이메일
        <input type="email" name="email" required />
      </label>
      <label>
        비밀번호
        <input type="password" name="password" required />
      </label>
      <Btn 
        content="로그인"
      />
    </form>
  );
}

export default logInForm;