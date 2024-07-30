import { useState } from 'react';
import Btn from '../Common/Btn';
import InputField from './InputField';
import SelectField from './SelectField';
import AccountBtn from './AccountBtn';
import RadioField from './RadioField';

const SignUpForm = () => {
  const [id, setId] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [nickname, setNickname] = useState('');
  const [birthdate, setBirthdate] = useState('');
  const [source, setSource] = useState('');
  const [gender, setGender] = useState(0)
  const [sido, setSido] = useState('');
  const [gugun, setGugun] = useState('');
  const [agreePersonal, setAgreePersonal] = useState(false);
  const [agreeAdvertisement, setAgreeAdvertisement] = useState(false);

  const handleSignUp = () => {
    if (!id || !email || !password || !passwordConfirm) {
      alert('필수 항목을 모두 입력해 주세요.');
      return;
    }
    if (password !== passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    if (!agreePersonal) {
      alert('개인정보 수집 동의를 완료해주세요');
      return;
    }

    // console.log('ID:', id, 'Email:', email, 'Password:', password, 'Password Confirm:', passwordConfirm, 'Nickname:', nickname, 'Birthdate:', birthdate, 'Source:', source, 'Gender:', gender, 'Sido:', sido, 'Gugun:', gugun, 'Agree Personal:', agreePersonal, 'Agree Advertisement:', agreeAdvertisement);
    alert('회원가입 완료');
  };

  return (
    <form onSubmit={e => e.preventDefault()}>
      <div>
        <InputField
          type="text" 
          placeholder="아이디" 
          value={id} 
          onChange={(e) => setId(e.target.value)}
          isRequired={true}
        />
        <AccountBtn 
          content='중복확인'
        />
      </div>
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
      <div>
        <InputField
          type="text" 
          placeholder="닉네임" 
          value={nickname} 
          onChange={(e) => setNickname(e.target.value)}
          isRequired={false}
        />
        <AccountBtn 
          content='추천받기'
        />
      </div>
      <div>
        <InputField
          type="date" 
          placeholder="생일" 
          value={birthdate} 
          onChange={(e) => setBirthdate(e.target.value)}
          isRequired={false}
        />
      </div>
      <SelectField
        value={source} 
        onChange={e => setSource(e.target.value)}
        options={['가입경로', '지인추천', '인터넷 검색']}
        isRequired={false}
      />
      <RadioField
        selectedValue={gender}
        onChange={setGender}
        options={[
          { value: 0, label: '선택하지 않음' },
          { value: 2, label: '여자' },
          { value: 1, label: '남자' }
        ]}
        isRequired={false}
      />
      <div>
        <label>지역</label>
        <SelectField
          value={sido}
          onChange={e => setSido(e.target.value)}
          options={['시/도']}
          isRequired={false}
        />
        <SelectField
          value={gugun}
          onChange={e => setGugun(e.target.value)}
          options={['구/군']}
          isRequired={false}
        />
      </div>
      <div>
        <input
          type="checkbox"
          checked={agreePersonal}
          onChange={e => setAgreePersonal(e.target.checked)}
          required
        />
        <span>(필수) 개인정보 수집 동의</span>
      </div>
      <div>
        <input
          type="checkbox"
          checked={agreeAdvertisement}
          onChange={e => setAgreeAdvertisement(e.target.checked)}
        />
        <span>(선택) 광고 수신 동의</span>
      </div>
      <Btn
        content="회원가입"
        onClick={handleSignUp}
      />
    </form>
  );
}

export default SignUpForm;
