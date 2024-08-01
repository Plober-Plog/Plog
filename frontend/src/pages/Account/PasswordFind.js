import React from 'react';
import { useState } from 'react';
import Btn from '../../components/Common/Btn';
import InputField from '../../components/Account/InputField';
import AccountBtn from '../../components/Account/AccountBtn';

const PasswordFind = () => {
  const [email, setEmail] = useState('');

  return (
    <div>
      <h2>비밀번호 찾기</h2>
      <form onSubmit={e => e.preventDefault()}>
        <div>
          <InputField
            type="email" 
            placeholder="이메일" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
          />
          <AccountBtn 
            content='인증하기'
          />
        </div>
        <Btn
          content="비밀번호 찾기"
          disabled={!email}
          onClick={
            () => {
              // console.log('이메일:', email);
              console.log('이메일 입력 받기 성공!')
            }
          }
        />
      </form>
    </div>
  )
}

export default PasswordFind;