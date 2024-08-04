import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import API from '../../apis/api';
import { sha256 } from 'js-sha256';

import Btn from '../../components/Common/Btn';
import InputField from '../../components/Common/InputField';
import './Account.css';

const PasswordChange = () => {
  const [currentPassword, setCurrentPassword] = useState('');
  const [passwordConfirmMsg, setPasswordConfirmMsg] = useState('');
  const navigate = useNavigate();

  const handlePasswordChange = async (event) => {
    event.preventDefault();

    // 현재 비밀번호 확인 요청
    try {
      const response = await API.post(`/user/password`, {
        password: sha256(currentPassword),
      });

      if (response.status === 200) {
        console.log('비밀번호 확인 성공!')
        const userId = response.data.userId;
        navigate('/password/update', { state: { userId } });
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        console.log('비밀번호 불일치!')
        setPasswordConfirmMsg('비밀번호가 일치하지 않습니다.');
      } else {
        console.log('비밀번호 확인 실패!')
        setPasswordConfirmMsg('비밀번호 확인에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  return (
    <div className="container">
      <h2 className="title">비밀번호 변경</h2>
      <form onSubmit={handlePasswordChange} className="form">
        <InputField
          type="password"
          placeholder="현재 비밀번호"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
          required
          className="input"
        />
        {/* TODO 머지 후 비밀번호 보이기 버튼 추가 */}
        {passwordConfirmMsg && <p className="error">{passwordConfirmMsg}</p>}
        <Btn content="비밀번호 확인" type="submit" className="button"/>
      </form>
    </div>
  );
};


export default PasswordChange;
