import { useState, useEffect } from 'react'; // useEffect 추가
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import axios from 'axios';
import sha256 from 'js-sha256';
import useAuthStore from '../../stores/member';
import Btn from '../Common/Btn';
import InputField from '../Common/InputField';
import ATag from '../Common/ATag';
import { requestForToken } from '../../firebase'; // FCM 관련 코드 추가

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loginError, setLoginError] = useState('');
  const [fcmToken, setFcmToken] = useState(''); // FCM 토큰 상태 추가
  const setToken = useAuthStore((state) => state.setToken);
  const setUserData = useAuthStore((state) => state.setUserData);
  const navigate = useNavigate();

  useEffect(() => {
    requestForToken().then((token) => {
      if (token) {
        setFcmToken(token);
      } else {
        console.error('FCM 토큰을 받지 못했습니다.');
      }
    });
  }, []);

  const handleLogin = async (event) => {
    event.preventDefault();

    const userInfo = {
      email,
      password: sha256(password),
      notificationToken: fcmToken, // FCM 토큰 추가
    };

    try {
      const response = await axios.post('https://i11b308.p.ssafy.io/api/user/login', userInfo);
      const { accessToken, refreshToken } = response.data;

      if (accessToken && refreshToken) {
        setToken(accessToken, refreshToken);
        const userResponse = await API.get('/user');
        setUserData(userResponse.data);
        navigate('/');
      } else {
        setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
      }
    } catch (error) {
      setLoginError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div>
      <form onSubmit={handleLogin} className="form">
        <div>
          <InputField
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            isRequired={true}
            className="input"
          />
        </div>
        <div>
          <InputField
            type={showPassword ? 'text' : 'password'}
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
        </div>
        {loginError && <p>{loginError}</p>}
        <Btn
          content="로그인"
          disabled={!email || !password}
          onClick={handleLogin}
          className="button"
        />
      </form>
    </div>
  );
};

export default LoginForm;
