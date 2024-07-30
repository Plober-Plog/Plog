import { useState } from 'react';
import Btn from '../Common/Btn';
import InputField from './InputField';
import AccountBtn from './AccountBtn';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  return (
    <form onSubmit={e => e.preventDefault()}>
      <div>
        <InputField
          type="email" 
          placeholder="이메일" 
          value={email} 
          onChange={(e) => setEmail(e.target.value)}
          isRequired={true}
        />
      </div>
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
      <Btn
        content="로그인"
        onClick={
          () => {
            console.log('Email:', email);
            console.log('Password:', password);
          }
        }
      />
    </form>
  );
}

export default LoginForm;
