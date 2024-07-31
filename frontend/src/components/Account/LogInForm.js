import { useState } from 'react';
import Btn from '../Common/Btn';
import InputField from './InputField';
import AccountBtn from './AccountBtn';
import ModalComplete from './ModalComplete';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  const handleSignUpComplete = (event) => {
    event.preventDefault();
    setOpenModal(true);
  };

  const closeModal = () => {
    setOpenModal(false);
  };

  return (
    <div>
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
          disabled={!email && !password}
          onClick={handleSignUpComplete}
        />
      </form>
      <ModalComplete title={'로그인 완료'} content={'로그인이 완료되었습니다'} open={openModal} onClose={closeModal} />
    </div>
  );
}

export default LoginForm;
