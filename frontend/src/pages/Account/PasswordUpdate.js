import React from 'react';
import { useState } from 'react';
import Btn from '../../components/Common/Btn';
import InputField from '../../components/Account/InputField';
import AccountBtn from '../../components/Account/AccountBtn';

const PasswordUpdate = () => {
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div>
      <h2>비밀번호 변경</h2>
      <form onSubmit={e => e.preventDefault()}>
        <div>
          <InputField
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)}
            isRequired={true} 
          />
          <AccountBtn
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
        </div>
        <div>
          <InputField
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호 확인" 
            value={passwordConfirm} 
            onChange={(e) => setPasswordConfirm(e.target.value)}
            isRequired={true} 
          />
          <AccountBtn
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
        </div>
        <Btn
          content="비밀번호 변경"
          disabled={!password || !passwordConfirm}
          onClick={
            () => {
              // console.log('비밀번호:', password, '비밀번호확인:', passwordConfirm);
              console.log('비밀번호 입력 받기 성공!')
            }
          }
        />
      </form>
    </div>
  )
}

export default PasswordUpdate;