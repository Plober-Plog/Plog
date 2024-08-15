import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../apis/api";
import Btn from "../../components/Common/Btn";
import RadioField from "../../components/Common/RadioField";

const ProfilePushAlarm = () => {
  const [isPushAlarm, setIsPushAlarm] = useState(false); // 푸시 알림 상태 관리
  const navigate = useNavigate();

  useEffect(() => {
    // 푸시 알림 상태 조회
    const fetchPushAlarmStatus = async () => {
      try {
        const response = await API.get('user/push');
        setIsPushAlarm(response.data.pushNotificationEnabled);
      } catch (error) {
        console.error('푸시알림 조회 중 에러 발생:', error);
      }
    };

    fetchPushAlarmStatus();
  }, []);

  // 알림 설정 제출
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 리로드 방지

    try {
      const data = { "pushNotificationEnabled": isPushAlarm };
      const response = await API.patch('user/push', data);
      navigate('/setting');
    } catch (error) {
      console.error('푸시알림 설정 중 에러 발생:', error);
    }
  };

  return (
    <div className="account-container">
      <h2 className="account-title">푸시알림 설정</h2>
      <form className="account-form" onSubmit={handleSubmit}>
        <div className="password-container">
          <RadioField
            selectedValue={isPushAlarm}
            onChange={setIsPushAlarm}
            options={[
              { value: true, label: '알림 켜기' },
              { value: false, label: '알림 끄기' },
            ]}
            isRequired={false}
            className="profile-radio-field"
          />
        </div>
        <Btn content="변경하기" type="submit" className="account-button"/>
      </form>
    </div>
  );
}

export default ProfilePushAlarm;
