import React, { useState, useEffect } from 'react';
import API from '../../apis/api';
import axios from 'axios';
import sha256 from 'js-sha256';
import { useNavigate, useLocation } from 'react-router-dom';

import Btn from '../../components/Common/Btn';
import InputField from '../../components/Common/InputField';
import ATag from '../../components/Common/ATag';
import ModalComplete from '../../components/Common/ModalComplete';
import './Account.css';

const PasswordUpdate = () => {
  const navigate = useNavigate();
  const URI = 'https://i11b308.p.ssafy.io/api'
  // location
  const location = useLocation();
  const { userId } = location.state;
  // input fields
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  // 변경 버튼 클릭
  const [openModal, setOpenModal] = useState(false);
  // 비밀번호 유효성 확인
  const [passwordError, setPasswordError] = useState('');
  const [passwordConfirmError, setPasswordConfirmError] = useState('');
  const [isFormValid, setIsFormValid] = useState(false);


  // 비밀번호 유효성 검사
  useEffect(() => {
    
    const validatePassword = () => {
      if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/.test(password)) {
        setPasswordError('올바른 비밀번호 형식이 아닙니다');
      } else {
        setPasswordError('');
      }
    };

    const validatePasswordConfirm = () => {
      if (password !== passwordConfirm) {
        setPasswordConfirmError('비밀번호가 일치하지 않습니다');
      } else {
        setPasswordConfirmError('');
      }
    };

    validatePassword();
    validatePasswordConfirm();

    setIsFormValid(!passwordError && !passwordConfirmError && password && passwordConfirm);
  }, [password, passwordConfirm, passwordError, passwordConfirmError]);


  // 비밀번호 변경 버튼 클릭
  const handlePasswordUpdate = async (event) => {
    event.preventDefault();
    
    // 비밀번호 변경 요청
    try {
      console.log('userId: ',userId)
      const response = await axios.patch(`${URI}/user/password`, {
        userId,
        password: sha256(password)
      })
      console.log('비밀번호 변경 성공:', response.data);
      setOpenModal(true);
    } catch (error) {
      console.error('비밀번호 변경 실패:', error);
    }
  };
  
  // 모달
  const closeModal = () => {
    setOpenModal(false);
    navigate('/setting');
  };
  

  return (
    <div className="account-container">
      <h2 className="title">비밀번호 변경</h2>
      <form onSubmit={e => e.preventDefault()} className="form">
        <div>
          <InputField
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)}
            isRequired={true} 
            className="input"
          />
          <ATag
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
          {passwordError && <p className="error">{passwordError}</p>}
        </div>
        <div>
          <InputField
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호 확인" 
            value={passwordConfirm} 
            onChange={(e) => setPasswordConfirm(e.target.value)}
            isRequired={true} 
            className="input"
          />
          <ATag
            onClick={() => setShowPassword(!showPassword)}
            content={showPassword ? '숨기기' : '보기'}
          />
          {passwordConfirmError && <p className="error">{passwordConfirmError}</p>}
        </div>
        <Btn
          content="비밀번호 변경"
          disabled={!isFormValid}
          onClick={handlePasswordUpdate}
          className="button"
        />
      </form>
      <ModalComplete
        title={'비밀번호 변경 완료'}
        content={'비밀번호 변경이 완료되었습니다'}
        open={openModal}
        onClose={closeModal}
      />
    </div>
  )
}

export default PasswordUpdate;